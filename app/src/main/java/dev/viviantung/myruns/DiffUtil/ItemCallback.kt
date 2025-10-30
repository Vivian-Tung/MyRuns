package dev.viviantung.myruns.DiffUtil

import androidx.recyclerview.widget.DiffUtil
import dev.viviantung.myruns.Exercise

// helper class to just render the new entries
class ExerciseDiffCallback : DiffUtil.ItemCallback<Exercise>() {
    override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        // returns true if two items represent the same object (by ID)
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        // Returns true if the contents of the item are the same
        return oldItem == newItem
    }
}