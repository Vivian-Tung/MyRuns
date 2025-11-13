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
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


class MapDisplayActivity: AppCompatActivity(), OnMapReadyCallback  {
    private lateinit var mMap: GoogleMap
    private var mapReady = false
    private var locationPermissionGranted = false
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private lateinit var statsView: TextView
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private lateinit var mapViewModel: MapViewModel


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


        mapViewModel.mapper.observe(this) { latLng ->
            if (::mMap.isInitialized && latLng != null) {
                // Move/center map or extend the polyline
                mMap.animateCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(latLng, 16f))

                // Example: add marker (you’ll likely replace this with a single polyline)
                mMap.addMarker(com.google.android.gms.maps.model.MarkerOptions().position(latLng))
            }
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