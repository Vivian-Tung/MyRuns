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
import android.util.Log
import android.view.View
import android.widget.TimePicker
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import dev.viviantung.myruns.BaseDialog.Companion
import dev.viviantung.myruns.BaseDialog.Companion.COMMENT_DIALOG
import dev.viviantung.myruns.BaseDialog.Companion.DURATION_DIALOG


class ManualActivity: AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val ENTRY = arrayOf(
        "Date", "Time", "Duration", "Distance", "Calories", "Heart Rate", "Comment"
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual)
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)

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

        // leave this for now since we arent rendering, i just want to test that its been inserted
//        exerciseViewModel.allExerciseLiveData.observe(this, Observer { it ->
//            // need to set up a exercise entry array adapter to display (for later
//
//        })

        saveButton.setOnClickListener() {
            // how do i collect all the data and insert it in?



            finish();
        }

        cancelButton.setOnClickListener() {
            finish();
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = "$year-${month + 1}-$dayOfMonth"
        // Toast.makeText(this, "Selected: $selectedDate", Toast.LENGTH_SHORT).show()
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val selectedTime = "$hourOfDay : $minute"
        // Toast.makeText(this, "Selected: $selectedTime", Toast.LENGTH_SHORT).show()
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
        onDialogInputReceived(BaseDialog.DISTANCE_DIALOG, args)

    }
    fun onCaloriesClick() {
        val dialog = BaseDialog()
        val args = Bundle()
        args.putInt(BaseDialog.DIALOG_KEY, BaseDialog.CALORIES_DIALOG)
        dialog.arguments = args
        dialog.show(supportFragmentManager, TAG)
        onDialogInputReceived(BaseDialog.CALORIES_DIALOG, args)

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

    fun onDialogInputReceived(dialogId: Int, data: Bundle) {
        when (dialogId) {
            DURATION_DIALOG -> {
                val duration = data.getString("et_duration")
                Toast.makeText(this, "Duration: $duration", Toast.LENGTH_SHORT).show()
            }
            COMMENT_DIALOG -> {
                val comment = data.getString("comment")
                Toast.makeText(this, "Comment: $comment", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Log.d("DialogInput", "Received: $data")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
}