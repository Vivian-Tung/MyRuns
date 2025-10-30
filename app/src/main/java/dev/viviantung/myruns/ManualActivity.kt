package dev.viviantung.myruns

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import java.util.Calendar
import android.widget.DatePicker
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.text.TextUtils.indexOf
import android.util.Log
import android.view.View
import android.widget.TimePicker
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import dev.viviantung.myruns.BaseDialog.Companion
import dev.viviantung.myruns.BaseDialog.Companion.CALORIES_DIALOG
import dev.viviantung.myruns.BaseDialog.Companion.COMMENT_DIALOG
import dev.viviantung.myruns.BaseDialog.Companion.DISTANCE_DIALOG
import dev.viviantung.myruns.BaseDialog.Companion.DURATION_DIALOG
import dev.viviantung.myruns.BaseDialog.Companion.HR_DIALOG
import java.util.Date


class ManualActivity: AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInputListener {
    private val ENTRY = arrayOf(
        "Date", "Time", "Duration", "Distance", "Calories", "Heart Rate", "Comment"
    )

    // mapping between index and activity type
    private val ACTIVITYTYPE = arrayOf(
        "Run", "Ultimate Frisbee", "Pickleball", "Swim", "Strength", "Bike", "Badminton", "Basketball", "Volleyball", "Golf", "Standup Paddleboard"
    )

    private lateinit var myListView: ListView
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private var TAG = "dialog tag"

    val calendar = Calendar.getInstance()

    private lateinit var database: ExerciseDatabase
    private lateinit var databaseDao: ExerciseDatabaseDao
    private lateinit var repository: ExerciseRepository
    private lateinit var viewModelFactory: ExerciseViewModelFactory
    private lateinit var exerciseViewModel: ExerciseViewModel

    // date time variables
    private var selectedYear: Int = 0
    private var selectedMonth: Int = 0
    private var selectedDay: Int = 0
    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0

    // var for exercise input
    private lateinit var activityType: String // need to create a map from activity to ints
    private var inputType: Int = 0 // 0 for manual, 1 for GPS, 2 for auto
    private lateinit var selectedDateTime: Date
    private var duration: Double = 0.0
    private var distance: Double = 0.0
    private var pace: Double = 0.0
    private var speed: Double = 0.0
    private var calories: Double = 0.0
    private var heartRate: Double = 0.0
    private var comment: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual)
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)
        val deleteButton = toolbar.findViewById<Button>(R.id.btn_delete)
        deleteButton.visibility = View.GONE

        activityType = getIntent().getStringExtra("EXTRA_ACTIVITY_TYPE").toString()
        // TODO: need to use map to get convert to integer
        val activityTypeInt = ACTIVITYTYPE.indexOf(activityType)
        Log.d("activity_type", "activity type int: $activityTypeInt")

        myListView = findViewById(R.id.manual_entry)
        saveButton = findViewById(R.id.btnSave)
        cancelButton = findViewById(R.id.btnCancel)

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1, ENTRY
        )
        myListView.adapter = arrayAdapter
        myListView.setOnItemClickListener() { parent, view, position, id ->
            when (position) {
                0 -> DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
                1 -> TimePickerDialog(this, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
                2 -> onDurationClick()
                3 -> onDistanceClick()
                4 -> onCaloriesClick()
                5 -> onHRClick()
                6 -> onCommentClick()
            }
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

        saveButton.setOnClickListener() {
            selectedDateTime = getCombinedDateTime(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)
            pace = if (distance > 0) duration / distance else 0.0
            speed = if (duration > 0) distance / duration else 0.0

            val newExercise = Exercise(
                inputType = inputType,
                activityType = activityTypeInt,
                dateTime = selectedDateTime,
                duration = duration,
                distance = distance,
                avgPace = pace,
                avgSpeed = speed,
                calorie = calories,
                heartRate = heartRate,
                comment = comment,
                location = ""
            )
            exerciseViewModel.insert(newExercise)
            Toast.makeText(this, "Exercise saved!", Toast.LENGTH_SHORT).show()
            finish();
        }

        cancelButton.setOnClickListener() {
            finish();
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        selectedYear = year
        selectedMonth = month
        selectedDay = dayOfMonth
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        selectedHour = hourOfDay
        selectedMinute = minute
    }

    fun onDurationClick() {
        val dialog = BaseDialog()
        val args = Bundle()
        args.putInt(BaseDialog.DIALOG_KEY, BaseDialog.DURATION_DIALOG)
        dialog.arguments = args
        dialog.show(supportFragmentManager, TAG)
    }

    fun onDistanceClick() {
        val dialog = BaseDialog()
        val args = Bundle()
        args.putInt(BaseDialog.DIALOG_KEY, BaseDialog.DISTANCE_DIALOG)
        dialog.arguments = args
        dialog.show(supportFragmentManager, TAG)
    }
    fun onCaloriesClick() {
        val dialog = BaseDialog()
        val args = Bundle()
        args.putInt(BaseDialog.DIALOG_KEY, BaseDialog.CALORIES_DIALOG)
        dialog.arguments = args
        dialog.show(supportFragmentManager, TAG)
    }

    fun onHRClick() {
        val dialog = BaseDialog()
        val args = Bundle()
        args.putInt(BaseDialog.DIALOG_KEY, BaseDialog.HR_DIALOG)
        dialog.arguments = args
        dialog.show(supportFragmentManager, TAG)
    }

    fun onCommentClick() {
        val dialog = BaseDialog()
        val args = Bundle()
        args.putInt(BaseDialog.DIALOG_KEY, BaseDialog.COMMENT_DIALOG)
        dialog.arguments = args
        dialog.show(supportFragmentManager, TAG)
    }

    override fun onDialogInputReceived(dialogId: Int, data: Bundle) {
        when (dialogId) {
            DURATION_DIALOG -> duration = data.getDouble("duration")
            DISTANCE_DIALOG -> distance = data.getDouble("distance")
            CALORIES_DIALOG -> calories = data.getDouble("calories")
            HR_DIALOG -> heartRate = data.getDouble("heartRate")
            COMMENT_DIALOG -> comment = data.getString("comment").toString()
        }
        // Toast.makeText(this, "Updated variables: duration=$duration, distance=$distance, calories=$calories, hr=$heartRate, comment=$comment", Toast.LENGTH_SHORT).show()
    }

    // helper function to combine date and time
    fun getCombinedDateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val dateTime = calendar.time
        return dateTime
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
}