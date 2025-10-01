package dev.viviantung.myruns

import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


class BaseDialog(
    private val onOptionSelected: ((option: Int) -> Unit)? = null
): DialogFragment(), DialogInterface.OnClickListener {

    companion object{
        const val DIALOG_KEY = "DIALOG KEY"
        const val UNIT_PREFERENCE_DIALOG = 1
        const val COMMENT_DIALOG = 2
        const val GALLERY_DIALOG = 3
        const val DURATION_DIALOG = 4
        const val DISTANCE_DIALOG = 5
        const val CALORIES_DIALOG = 6
        const val HR_DIALOG = 7


        const val CAMERA_OPTION = 1
        const val GALLERY_OPTION = 2
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var dialog: Dialog
        val bundle = arguments
        val dialogId = bundle?.getInt(DIALOG_KEY)
        if(dialogId == UNIT_PREFERENCE_DIALOG) {
            var builder = AlertDialog.Builder(requireActivity())
            val view = requireActivity().layoutInflater.inflate(R.layout.dialog_unit_preferences, null)

            val prefs = requireContext().getSharedPreferences("ProfilePreferences", Context.MODE_PRIVATE)
            val savedUnit = prefs.getString("unit_preference", "") // no default

            if (savedUnit == getString(R.string.metric)) {
                view.findViewById<RadioButton>(R.id.radioMetric).isChecked = true
            } else if (savedUnit == getString(R.string.imperial)) {
                view.findViewById<RadioButton>(R.id.radioImperial).isChecked = true
            }

            val unitRadioGroup =  view.findViewById<RadioGroup>(R.id.unitRadioGroup)
            unitRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId != -1) { // -1 means nothing selected
                    val selected = view.findViewById<RadioButton>(checkedId).text.toString()
                    val prefs = requireActivity().getSharedPreferences("ProfilePreferences", Context.MODE_PRIVATE)
                    prefs.edit()
                        .putString("unit_preference", selected)
                        .apply()
                    dismiss()
                }
            }

            builder.setView(view)
            builder.setTitle("Select which unit to use")
            builder.setNegativeButton("Cancel", this)
            dialog = builder.create()

        } else if(dialogId == COMMENT_DIALOG) {
            var builder = AlertDialog.Builder(requireActivity())
            val view = requireActivity().layoutInflater.inflate(R.layout.dialog_comment, null)

            val commentEditText = view.findViewById<EditText>(R.id.comment)
            val prefs = requireContext().getSharedPreferences("ProfilePreferences", Context.MODE_PRIVATE)
            val savedComment = prefs.getString("comments", "") ?: ""// no default
            commentEditText.setText(savedComment)

            builder.setView(view)
            builder.setTitle("Comment")
            builder.setPositiveButton("Ok") { _, _ ->
                val comment = commentEditText.text.toString()
                prefs.edit()
                    .putString("comments", comment)
                    .apply()
            }
            builder.setNegativeButton("Cancel") { _, _ ->
                // Do nothing (no save)
            }
            dialog = builder.create()
        } else if(dialogId == GALLERY_DIALOG) {
            var builder = AlertDialog.Builder(requireActivity())
            val view = requireActivity().layoutInflater.inflate(R.layout.dialog_gallery, null)

            // init views
            val cameraView = view.findViewById<View>(R.id.camera)
            val galleryView = view.findViewById<View>(R.id.gallery)

            cameraView.setOnClickListener {
                onOptionSelected?.invoke(CAMERA_OPTION)
                dismiss()
            }

            galleryView.setOnClickListener {
                onOptionSelected?.invoke(GALLERY_OPTION)
                dismiss()
            }

            builder.setView(view)
            builder.setTitle("Select profile image")
            dialog = builder.create()

        } else if(dialogId == DURATION_DIALOG) {
            var builder = AlertDialog.Builder(requireActivity())
            val view = requireActivity().layoutInflater.inflate(R.layout.dialog_num_input, null)

            builder.setView(view)
            builder.setTitle("Duration")
            builder.setPositiveButton("Ok", this)
            builder.setNegativeButton("Cancel", this)
            dialog = builder.create()
        }
        else if(dialogId == DISTANCE_DIALOG) {
            var builder = AlertDialog.Builder(requireActivity())
            val view = requireActivity().layoutInflater.inflate(R.layout.dialog_num_input, null)

            builder.setView(view)
            builder.setTitle("Distance")
            builder.setPositiveButton("Ok", this)
            builder.setNegativeButton("Cancel", this)
            dialog = builder.create()
        }
        else if(dialogId == CALORIES_DIALOG) {
            var builder = AlertDialog.Builder(requireActivity())
            val view = requireActivity().layoutInflater.inflate(R.layout.dialog_num_input, null)

            builder.setView(view)
            builder.setTitle("Calories")
            builder.setPositiveButton("Ok", this)
            builder.setNegativeButton("Cancel", this)
            dialog = builder.create()
        }
        else if(dialogId == HR_DIALOG) {
            var builder = AlertDialog.Builder(requireActivity())
            val view = requireActivity().layoutInflater.inflate(R.layout.dialog_num_input, null)

            builder.setView(view)
            builder.setTitle("Heart rate")
            builder.setPositiveButton("Ok", this)
            builder.setNegativeButton("Cancel", this)
            dialog = builder.create()
        }
        return dialog
    }

    override fun onClick(dialog: DialogInterface?, item: Int) {
        if(item == DialogInterface.BUTTON_POSITIVE)
            Toast.makeText(requireActivity(), "Ok clicked", Toast.LENGTH_SHORT).show()
        else if(item == DialogInterface.BUTTON_NEGATIVE)
            Toast.makeText(requireActivity(), "Cancel clicked", Toast.LENGTH_SHORT).show()
    }
}