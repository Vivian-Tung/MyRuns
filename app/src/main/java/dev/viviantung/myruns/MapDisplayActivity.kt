package dev.viviantung.myruns

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.TextView
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


class MapDisplayActivity: AppCompatActivity(), OnMapReadyCallback  {
    private lateinit var mMap: GoogleMap
    private var mapReady = false
    private var locationPermissionGranted = false
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private lateinit var statsView: TextView
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private lateinit var mapViewModel: MapViewModel

    private var startMarker: Marker? = null
    private var endMarker: Marker? = null
    private var pathPolyline: Polyline? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_display)
//        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.title = "Map"
//        val deleteButton = toolbar.findViewById<Button>(R.id.btn_delete)
//        deleteButton.visibility = View.GONE

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

        saveButton.setOnClickListener() {
            finish();
        }

        cancelButton.setOnClickListener() {
            val intent = Intent()
            intent.action = TrackingService.STOP_SERVICE_ACTION
            sendBroadcast(intent)
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