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
    private var initialCameraMoved = false

    // database
    private lateinit var database: ExerciseDatabase
    private lateinit var databaseDao: ExerciseDatabaseDao
    private lateinit var repository: ExerciseRepository
    private lateinit var viewModelFactory: ExerciseViewModelFactory
    private lateinit var exerciseViewModel: ExerciseViewModel
    private var activityType: String? = null// need to create a map from activity to ints
    private var activityTypeInt = -1
    private var duration = 0.0
    private var distance = 0.0
    private var pace = 0.0
    private var speed = 0.0
    private var calories = 0.0
    private var heartRate = 0.0
    private var comment = ""

    private val ACTIVITYTYPE = arrayOf(
        "Running", "Walking", "Standing", "Ultimate Frisbee", "Pickleball", "Swim", "Strength", "Bike", "Badminton", "Basketball", "Volleyball", "Golf", "Standup Paddleboard", "Others"
    )
    private var inputTypeInt = 1 // default to GPS


    private val serviceConnection = object : android.content.ServiceConnection {
        override fun onServiceConnected(
            name: android.content.ComponentName?,
            binder: android.os.IBinder?
        ) {
            trackingServiceBinder = binder as TrackingService.MyBinder

            trackingServiceBinder.setPredictedActivityUpdater { label ->
                runOnUiThread {
                    mapViewModel.updatePredictedActivity(label)
                }
            }

            // Initialize current path from service
            trackingServiceBinder.getPath().let { points ->
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
        }

        override fun onServiceDisconnected(name: android.content.ComponentName?) {}
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_display)
//        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.title = "Map"
//        val deleteButton = toolbar.findViewById<Button>(R.id.btn_delete)
//        deleteButton.visibility = View.GONE


        activityType = intent.getStringExtra("EXTRA_ACTIVITY_TYPE")
        if (activityType.isNullOrEmpty()) {
            inputTypeInt = 2 // auto
            activityTypeInt = ACTIVITYTYPE.size - 1 // default to others
        } else { // gps
            activityTypeInt = activityTypeInt.takeIf { it in ACTIVITYTYPE.indices } ?: ACTIVITYTYPE.indexOf("Others")
        }


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
            if (initialCameraMoved) mMap.animateCamera(CameraUpdateFactory.newLatLng(latestLatLng))
        }

        mapViewModel.predictedActivity.observe(this) { label ->
            Log.d("TESTING", "Service callback: $label")

            val index = ACTIVITYTYPE.indexOf(label ?: "Others").takeIf { it != -1 } ?: ACTIVITYTYPE.indexOf("Others")
            activityTypeInt = index
            activityType = ACTIVITYTYPE[index]
            Log.d("TESTING", "Predicted activity: $activityType")
//            updateStatsUI()
        }

        // Start tracking service (after permissions)
        checkAllPermissions()


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
                inputType = inputTypeInt,
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

//    fun updateStatsUI() {
//        val activityName = ACTIVITYTYPE[activityTypeInt]
//        val durationStr = formatDuration(duration)
//        val distanceStr = formatDistance(distance)
//
//        statsView.text =
//            "Activity: $activityName\n" +
//                    "Duration: $durationStr\n" +
//                    "Distance: $distanceStr"
//    }

    private fun formatDistance(totalDistanceMeters: Any) {}

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapReady = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        if (::trackingServiceBinder.isInitialized && checkLocationPermission()) {
            enableMyLocation()
        }

        mapViewModel.pathPoints.value?.lastOrNull()?.let { lastLatLng ->
            if (!initialCameraMoved) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 16f))
                initialCameraMoved = true
            }
        }
//        // Observe pathPoints LiveData for updating markers and polyline
//        val serviceIntent = Intent(this, TrackingService::class.java)
//        bindService(serviceIntent, object : android.content.ServiceConnection {
//            override fun onServiceConnected(name: android.content.ComponentName?, binder: android.os.IBinder?) {
//                val binderService = binder as TrackingService.MyBinder
//                binderService.getPath().let { points ->
//                    if (points.isNotEmpty()) {
//                        startMarker = mMap.addMarker(O
//                            com.google.android.gms.maps.model.MarkerOptions()
//                                .position(points.first())
//                                .title("Start")
//                        )
//
//                        pathPolyline = mMap.addPolyline(
//                            PolylineOptions().addAll(points).width(8f)
//                        )
//                    }
//                }
//
//                // Update polyline and markers dynamically
//                val pathLiveData = (binder as TrackingService.MyBinder)::getPath // access pathPoints
//                // Could observe LiveData if exposed
//            }
//
//            override fun onServiceDisconnected(name: android.content.ComponentName?) {}
//        }, BIND_AUTO_CREATE)
    }

    private fun checkAllPermissions() {
        val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) permissions.add(Manifest.permission.FOREGROUND_SERVICE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) permissions.add(Manifest.permission.POST_NOTIFICATIONS)

        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, notGranted.toTypedArray(), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            startAndBindTrackingService()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            startAndBindTrackingService()
            if (mapReady) enableMyLocation()
        }
    }

    private fun enableMyLocation() {
        try {
            mMap.isMyLocationEnabled = true
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun startAndBindTrackingService() {
        val intent = Intent(this, TrackingService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent) else startService(intent)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

//    override fun onStart() {
//        super.onStart()
//        val intent = Intent(this, TrackingService::class.java)
//        bindService(intent, mapViewModel, Context.BIND_AUTO_CREATE)
//    }

//    override fun onStop() {
//        super.onStop()
//        unbindService(mapViewModel)
//    }

    override fun onDestroy() {
        super.onDestroy()
        if (::trackingServiceBinder.isInitialized) {
            unbindService(serviceConnection)
        }
    }

    fun formatDuration(seconds: Double): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", mins, secs) // <- crash here
    }

    private fun formatDistance(meters: Double): String {
        return String.format("%.2f km", meters / 1000.0)
    }
}