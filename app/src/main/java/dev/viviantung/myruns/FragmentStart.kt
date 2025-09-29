package dev.viviantung.myruns

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.widget.Spinner

class FragmentStart : Fragment() {
    private lateinit var startButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startButton = view.findViewById(R.id.btnStart)

        val inputSpinner = view.findViewById<Spinner>(R.id.input_spinner)
        val input = resources.getStringArray(R.array.input_array)

        val inputAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            input
        )
        inputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        inputSpinner.adapter = inputAdapter


        val activitySpinner = view.findViewById<Spinner>(R.id.activity_spinner)
        val activities = resources.getStringArray(R.array.activity_array)

        val activityAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            activities
        )
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activitySpinner.adapter = activityAdapter

        startButton.setOnClickListener() {
            val selectedInput = inputSpinner.selectedItem.toString()
            val selectedActivity = activitySpinner.selectedItem.toString()

            if (selectedInput == "Manual" && selectedActivity.isNotEmpty()) {
                val intent: Intent = Intent(getActivity(), ManualActivity::class.java)
                intent.putExtra("EXTRA_ACTIVITY_TYPE", selectedActivity)
                startActivity(intent)
            } else if (selectedInput == "GPS") {
                val intent: Intent = Intent(getActivity(), MapActivity::class.java)
                startActivity(intent)
            } else if (selectedInput == "Automatic") {
                val intent: Intent = Intent(getActivity(), MapActivity::class.java)
                startActivity(intent)
            }
        }
    }
}