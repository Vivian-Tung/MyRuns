package dev.viviantung.myruns

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.viviantung.myruns.DiffUtil.ExerciseDiffCallback
import java.text.SimpleDateFormat
import java.util.Locale

class ExerciseAdapter(
    private val onClick: (Exercise) -> Unit,      // called on item click
) : ListAdapter<Exercise, ExerciseAdapter.ExerciseViewHolder>(
    ExerciseDiffCallback()
) {

    private val ACTIVITYTYPE = arrayOf(
        "Run", "Ultimate Frisbee", "Pickleball", "Swim", "Strength", "Bike", "Badminton", "Basketball", "Volleyball", "Golf", "Standup Paddleboard"
    )

    // mapping between index and input type
    private val INPUTTYPE = arrayOf("Manual Entry", "GPS Entry", "Automatic Entry")

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val inputTypeText: TextView = itemView.findViewById(R.id.textInputType)
        private val activityTypeText: TextView = itemView.findViewById(R.id.textActivityType)
        private val dateTimeText: TextView = itemView.findViewById(R.id.textDateTime)
        private val detailsText: TextView = itemView.findViewById(R.id.textDetails)

        // need to add units here somehow need to pass the data here and also change the view
        fun bind(exercise: Exercise) {
            // trying to convert int to string to render it
            val activityTypeStr = ACTIVITYTYPE.elementAt(exercise.activityType)
            val inputTypeStr = INPUTTYPE.elementAt(exercise.inputType)

            inputTypeText.text = "${inputTypeStr}:"
            activityTypeText.text = " ${activityTypeStr}"
            dateTimeText.text = " ${
                SimpleDateFormat(
                    "HH:mm yyyy-MM-dd",
                    Locale.getDefault()
                ).format(exercise.dateTime)}"
            detailsText.text = "Distance: ${exercise.distance} , Duration: ${exercise.duration}"

            // Handle click for detail view
            itemView.setOnClickListener {
                onClick(exercise)
            }
        }
    }

    // inflate the entries
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_entry, parent, false)
        return ExerciseViewHolder(view)
    }

    // update ?
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)
    }
}