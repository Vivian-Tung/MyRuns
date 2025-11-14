package dev.viviantung.myruns

import android.app.FragmentContainer
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.launch
import androidx.fragment.app.FragmentContainerView

class DisplayEntryActivity : AppCompatActivity(), OnMapReadyCallback {
    // mapping
    private val INPUTTYPE = arrayOf("Manual Entry", "GPS Entry", "Automatic Entry")

    // mapping between index and activity type
    private val ACTIVITYTYPE = arrayOf(
        "Run", "Ultimate Frisbee", "Pickleball", "Swim", "Strength", "Bike", "Badminton", "Basketball", "Volleyball", "Golf", "Standup Paddleboard"
    )
    private lateinit var viewModel: ExerciseViewModel
    private var exerciseId: Long = 0L

    private var map: GoogleMap? = null
    private var gpsPoints: List<LatLng> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_detail)

        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)

        val deleteButton = toolbar.findViewById<Button>(R.id.btn_delete)
        deleteButton.visibility = View.VISIBLE // only on detail screens

        val mapContainer = findViewById<FragmentContainerView>(R.id.detail_map)
        val textContainer = findViewById<View>(R.id.detail_text_container)

        exerciseId = intent.getLongExtra("exercise_id", 0L)

        // load database + viewmodel
        val dao = ExerciseDatabase.getInstance(this).exerciseDatabaseDao
        val repository = ExerciseRepository(dao)
        val factory = ExerciseViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ExerciseViewModel::class.java)

        deleteButton.setOnClickListener {
//            Log.d("ExerciseDetail", "Delete button clicked for ID $exerciseId")
            lifecycleScope.launch {
                viewModel.delete(exerciseId)
//                Log.d("ExerciseDetail", "Delete called on ViewModel for ID $exerciseId")
                finish()
            }
        }

        lifecycleScope.launch {
            viewModel.getExerciseById(exerciseId).collect { exercise ->
                if (exercise == null) {
                    finish()
                    return@collect
                }

                // Show map only if GPS entry
                if (exercise.inputType == 1 && exercise.location.isNotEmpty()) {
                    mapContainer.visibility = View.VISIBLE

                    gpsPoints = decodePathString(exercise.location)

                    // Dynamically create the map fragment only once
                    var mapFragment = supportFragmentManager.findFragmentById(R.id.detail_map) as? SupportMapFragment
                    if (mapFragment == null) {
                        mapFragment = SupportMapFragment.newInstance()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.detail_map, mapFragment)
                            .commit()
                    }

                    mapFragment.getMapAsync(this@DisplayEntryActivity)
                } else {
                    mapContainer.visibility = View.GONE
                    textContainer.visibility = View.VISIBLE

                    // Populate UI fields
                    val inputTypeStr = INPUTTYPE.elementAt(exercise.inputType)
                    val activityTypeStr = ACTIVITYTYPE.elementAt(exercise.activityType)
                    findViewById<EditText>(R.id.input_edittext).setText(inputTypeStr) // might need map
                    findViewById<EditText>(R.id.activity_edittext).setText(activityTypeStr) // might need map
                    findViewById<EditText>(R.id.datetime_edittext).setText(exercise.dateTime.toString())
                    findViewById<EditText>(R.id.duration_edittext).setText(exercise.duration.toString())
                    findViewById<EditText>(R.id.distance_edittext).setText(exercise.distance.toString())
                    findViewById<EditText>(R.id.cals_edittext).setText(exercise.calorie.toString())
                    findViewById<EditText>(R.id.hr_edittext).setText(exercise.heartRate.toString())

                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

     override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        if (gpsPoints.isEmpty()) return

        // Draw polyline
        googleMap.addPolyline(
            PolylineOptions()
                .addAll(gpsPoints)
                .width(8f)
        )

        // Add start marker
        googleMap.addMarker(
            MarkerOptions()
                .position(gpsPoints.first())
                .title("Start")
        )

        // Add end marker
        googleMap.addMarker(
            MarkerOptions()
                .position(gpsPoints.last())
                .title("End")
        )

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gpsPoints.last(), 16f))
    }
    private fun decodePathString(pathString: String): List<LatLng> {
        return pathString.split(";")
            .mapNotNull { pair ->
                val parts = pair.split(",")
                if (parts.size == 2) {
                    parts[0].toDoubleOrNull()?.let { lat ->
                        parts[1].toDoubleOrNull()?.let { lng ->
                            LatLng(lat, lng)
                        }
                    }
                } else null
            }
    }
}
