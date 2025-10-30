package dev.viviantung.myruns

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DisplayEntryActivity : AppCompatActivity() {
    // mapping
    private val INPUTTYPE = arrayOf("Manual Entry", "GPS Entry", "Automatic Entry")

    // mapping between index and activity type
    private val ACTIVITYTYPE = arrayOf(
        "Run", "Ultimate Frisbee", "Pickleball", "Swim", "Strength", "Bike", "Badminton", "Basketball", "Volleyball", "Golf", "Standup Paddleboard"
    )
    private lateinit var viewModel: ExerciseViewModel
    private var exerciseId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_detail)

        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)

        exerciseId = intent.getLongExtra("exercise_id", 0L)

        // load database + viewmodel
        val dao = ExerciseDatabase.getInstance(this).exerciseDatabaseDao
        val repository = ExerciseRepository(dao)
        val factory = ExerciseViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ExerciseViewModel::class.java)

        lifecycleScope.launch {
            viewModel.getExerciseById(exerciseId).collect { exercise ->
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
}
