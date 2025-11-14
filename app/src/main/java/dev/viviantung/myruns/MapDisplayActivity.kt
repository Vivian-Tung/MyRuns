package dev.viviantung.myruns

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import java.util.Date


class MapDisplayActivity: AppCompatActivity(), OnMapReadyCallback  {
    private lateinit var mMap: GoogleMap
    private var mapReady = false
    private var locationPermissionGranted = false
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private lateinit var statsView: TextView
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private lateinit var mapViewModel: MapViewModel
    private lateinit var trackingServiceBinder: TrackingService.MyBinder
    private var startMarker: Marker? = null
    private var endMarker: Marker? = null
    private var pathPolyline: Polyline? = null

    // database
    private lateinit var database: ExerciseDatabase
    private lateinit var databaseDao: ExerciseDatabaseDao
    private lateinit var repository: ExerciseRepository
    private lateinit var viewModelFactory: ExerciseViewModelFactory
    private lateinit var exerciseViewModel: ExerciseViewModel

    private lateinit var activityType: String // need to create a map from activity to ints
    private var duration = 0.0
    private var distance = 0.0
    private var pace = 0.0
    private var speed = 0.0
    private var calories = 0.0
    private var heartRate = 0.0
    private var comment = ""

    private val ACTIVITYTYPE = arrayOf(
        "Run", "Ultimate Frisbee", "Pickleball", "Swim", "Strength", "Bike", "Badminton", "Basketball", "Volleyball", "Golf", "Standup Paddleboard"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_display)
//        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.title = "Map"
//        val deleteButton = toolbar.findViewById<Button>(R.id.btn_delete)
//        deleteButton.visibility = View.GONE


        activityType = getIntent().getStringExtra("EXTRA_ACTIVITY_TYPE").toString()
        val activityTypeInt = ACTIVITYTYPE.indexOf(activityType)

        // set up the database stuff
        database = ExerciseDatabase.getInstance(this)
        databaseDao = database.exerciseDatabaseDao
        repository = ExerciseRepository(databaseDao)
        viewModelFactory = ExerciseViewModelFactory(repository)
        exerciseViewModel = ViewModelProvider(this, viewModelFactory).get(ExerciseViewModel::class.java)

        exerciseViewModel.allExerciseLiveData.observe(this) { list ->
            Log.d("ExerciseList", "Current entries: $list")
        }

        mapViewModel = ViewModelProvider(this)[MapViewModel::class.java]

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Start tracking service
        val serviceIntent = Intent(this, TrackingService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else startService(serviceIntent)

        // Bind to service
        bindService(serviceIntent, object : android.content.ServiceConnection {
            override fun onServiceConnected(name: android.content.ComponentName?, binder: android.os.IBinder?) {
                trackingServiceBinder = binder as TrackingService.MyBinder
            }
            override fun onServiceDisconnected(name: android.content.ComponentName?) {}
        }, BIND_AUTO_CREATE)


        mapViewModel.pathPoints.observe(this) { points ->
            if (points.isEmpty() || !::mMap.isInitialized) return@observe

            val latestLatLng = points.last()

            // Start marker
            if (startMarker == null) {
                startMarker = mMap.addMarker(
                    MarkerOptions().position(points.first()).title("Start")
                )
            }

            // End marker
            if (endMarker == null) {
                endMarker = mMap.addMarker(
                    MarkerOptions().position(latestLatLng).title("Current")
                )
            } else {
                endMarker?.position = latestLatLng
            }

            // Polyline
            if (pathPolyline == null) {
                pathPolyline = mMap.addPolyline(
                    PolylineOptions().addAll(points).width(8f)
                )
            } else {
                pathPolyline?.points = points
            }

            // Center map on latest location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latestLatLng, 16f))
        }

        // Start tracking service (after permissions)
        checkAllPermissions()

        // when this activity gets launched, it should start the tracker service and bind to it
        // so that it can update the map in here i think

        // map stats
        statsView = findViewById(R.id.type_stats)

        saveButton = findViewById(R.id.btnSave)
        cancelButton = findViewById(R.id.btnCancel)

        // Save button: persist exercise to DB
        saveButton.setOnClickListener {
            val distance = trackingServiceBinder.getDistance() // meters
            val duration = trackingServiceBinder.getDuration() // seconds
            val path = trackingServiceBinder.getPath()
            val pathPointsString = mapViewModel.pathPoints.value
                ?.joinToString(";") { "${it.latitude},${it.longitude}" } ?: ""

            pace = if (distance > 0) duration / (distance / 1000) else 0.0 // sec per km
            speed = if (duration > 0) (distance / 1000) / (duration / 3600) else 0.0 // km/h

            val locationStr = path.joinToString(";") { "${it.latitude},${it.longitude}" }

            val durationSec = trackingServiceBinder.getDuration()
            val durationMin = durationSec / 60.0 // store as minutes

            val newExercise = Exercise(
                inputType = 1, // 1 is gps
                activityType = activityTypeInt,
                dateTime = Date(),
                duration = durationMin,
                distance = distance,
                avgPace = pace,
                avgSpeed = speed,
                calorie = calories,
                heartRate = heartRate,
                comment = comment,
                location = locationStr
            )

            exerciseViewModel.insert(newExercise)
            Toast.makeText(this, "Exercise saved!", Toast.LENGTH_SHORT).show()
            finish()

        }

        cancelButton.setOnClickListener() {
            val intent = Intent()
            intent.action = TrackingService.STOP_SERVICE_ACTION
            sendBroadcast(intent)
            Toast.makeText(this, "No data was saved!", Toast.LENGTH_SHORT).show()
            finish();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapReady = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        if (locationPermissionGranted) {
            enableMyLocation()
        }
        // Observe pathPoints LiveData for updating markers and polyline
        val serviceIntent = Intent(this, TrackingService::class.java)
        bindService(serviceIntent, object : android.content.ServiceConnection {
            override fun onServiceConnected(name: android.content.ComponentName?, binder: android.os.IBinder?) {
                val binderService = binder as TrackingService.MyBinder
                binderService.getPath().let { points ->
                    if (points.isNotEmpty()) {
                        startMarker = mMap.addMarker(
                            com.google.android.gms.maps.model.MarkerOptions()
                                .position(points.first())
                                .title("Start")
                        )

                        pathPolyline = mMap.addPolyline(
                            PolylineOptions().addAll(points).width(8f)
                        )
                    }
                }

                // Update polyline and markers dynamically
                val pathLiveData = (binder as TrackingService.MyBinder)::getPath // access pathPoints
                // Could observe LiveData if exposed
            }

            override fun onServiceDisconnected(name: android.content.ComponentName?) {}
        }, BIND_AUTO_CREATE)
    }

    private fun checkAllPermissions() {
        val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.FOREGROUND_SERVICE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                notGranted.toTypedArray(),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // All permissions granted → start service
            startTrackingService()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startTrackingService()
                if (mapReady) enableMyLocation()
            } else {
                println("Permission denied")
            }
        }
    }

    private fun enableMyLocation() {
        try {
            mMap.isMyLocationEnabled = true
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun startTrackingService() {
        val intent = Intent(this, TrackingService::class.java)
//        bindService(intent, mapViewModel, Context.BIND_AUTO_CREATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, TrackingService::class.java)
        bindService(intent, mapViewModel, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(mapViewModel)
    }
}