package dev.viviantung.myruns

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.widget.Spinner

class FragmentStart : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inputSpinner = view.findViewById<Spinner>(R.id.input_spinner)
        val input = resources.getStringArray(R.array.input_array)

        val inputAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            input
        )
        inputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        inputSpinner.adapter = inputAdapter

        inputSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(
                    requireContext(),
                    "${getString(R.string.input_type)} ${input[position]}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // nothing needed here
            }
        }

        val activitySpinner = view.findViewById<Spinner>(R.id.activity_spinner)
        val activities = resources.getStringArray(R.array.activity_array)

        val activityAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            activities
        )
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activitySpinner.adapter = activityAdapter

        activitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(
                    requireContext(),
                    "${getString(R.string.activity_type)} ${activities[position]}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // nothing needed here
            }
        }
    }
}