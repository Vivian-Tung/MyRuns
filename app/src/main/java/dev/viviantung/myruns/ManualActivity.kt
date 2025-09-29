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
import android.view.View
import android.widget.TimePicker


class ManualActivity: AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val ENTRY = arrayOf(
        "Date", "Time", "Duration", "Distance", "Calories", "Heart Rate", "Comment"
    )

    private lateinit var myListView: ListView
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private var TAG = "dialog tag"

    val calendar = Calendar.getInstance()

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

        cancelButton.setOnClickListener() {
            finish();
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = "$year-${month + 1}-$dayOfMonth"
        Toast.makeText(this, "Selected: $selectedDate", Toast.LENGTH_SHORT).show()
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val selectedTime = "$hourOfDay : $minute"
        Toast.makeText(this, "Selected: $selectedTime", Toast.LENGTH_SHORT).show()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
}