package dev.viviantung.myruns

import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

interface DialogInputListener {
    fun onDialogInputReceived(dialogId: Int, data: Bundle)
}


class BaseDialog(
    private val onOptionSelected: ((option: Int) -> Unit)? = null
): DialogFragment(), DialogInterface.OnClickListener {

    private lateinit var currentDialogView: View
    private var dialogId: Int = -1
    private var listener: DialogInputListener? = null

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
        dialogId = arguments?.getInt(DIALOG_KEY) ?: -1  // safe call

        var builder = AlertDialog.Builder(requireActivity())

        if(dialogId == UNIT_PREFERENCE_DIALOG) {
//            var builder = AlertDialog.Builder(requireActivity())
            currentDialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_unit_preferences, null)

            val prefs = requireContext().getSharedPreferences("ProfilePreferences", Context.MODE_PRIVATE)
            val savedUnit = prefs.getString("unit_preference", "") // no default

            if (savedUnit == getString(R.string.metric)) {
                currentDialogView.findViewById<RadioButton>(R.id.radioMetric).isChecked = true
            } else if (savedUnit == getString(R.string.imperial)) {
                currentDialogView.findViewById<RadioButton>(R.id.radioImperial).isChecked = true
            }

            val unitRadioGroup = currentDialogView.findViewById<RadioGroup>(R.id.unitRadioGroup)
            unitRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId != -1) { // -1 means nothing selected
                    val selected = currentDialogView.findViewById<RadioButton>(checkedId).text.toString()
                    val prefs = requireActivity().getSharedPreferences("ProfilePreferences", Context.MODE_PRIVATE)
                    prefs.edit()
                        .putString("unit_preference", selected)
                        .apply()
                    dismiss()
                }
            }

            builder.setView(currentDialogView)
            builder.setTitle("Select which unit to use")
            builder.setNegativeButton("Cancel", this)
            dialog = builder.create()

        } else if(dialogId == COMMENT_DIALOG) {
//            var builder = AlertDialog.Builder(requireActivity())
            currentDialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_comment, null)

            val commentEditText = currentDialogView.findViewById<EditText>(R.id.comment)
            val prefs = requireContext().getSharedPreferences("ProfilePreferences", Context.MODE_PRIVATE)
            val savedComment = prefs.getString("comments", "") ?: ""// no default
            commentEditText.setText(savedComment)

            builder.setView(currentDialogView)
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
//            var builder = AlertDialog.Builder(requireActivity())
            currentDialogView= requireActivity().layoutInflater.inflate(R.layout.dialog_gallery, null)

            // init views
            val cameraView = currentDialogView.findViewById<View>(R.id.camera)
            val galleryView = currentDialogView.findViewById<View>(R.id.gallery)

            cameraView.setOnClickListener {
                onOptionSelected?.invoke(CAMERA_OPTION)
                dismiss()
            }

            galleryView.setOnClickListener {
                onOptionSelected?.invoke(GALLERY_OPTION)
                dismiss()
            }

            builder.setView(currentDialogView)
            builder.setTitle("Select profile image")
            dialog = builder.create()

        } else if(dialogId == DURATION_DIALOG) {
//            var builder = AlertDialog.Builder(requireActivity())
            currentDialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_num_input, null)

            builder.setView(currentDialogView)
            builder.setTitle("Duration")
            builder.setPositiveButton("Ok", this)
            builder.setNegativeButton("Cancel", this)
            dialog = builder.create()
        }
        else if(dialogId == DISTANCE_DIALOG) {
//            var builder = AlertDialog.Builder(requireActivity())
            currentDialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_num_input, null)

            builder.setView(currentDialogView)
            builder.setTitle("Distance")
            builder.setPositiveButton("Ok", this)
            builder.setNegativeButton("Cancel", this)
            dialog = builder.create()
        }
        else if(dialogId == CALORIES_DIALOG) {
//            var builder = AlertDialog.Builder(requireActivity())
            currentDialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_num_input, null)

            builder.setView(currentDialogView)
            builder.setTitle("Calories")
            builder.setPositiveButton("Ok", this)
            builder.setNegativeButton("Cancel", this)
            dialog = builder.create()
        }
        else if(dialogId == HR_DIALOG) {
//            var builder = AlertDialog.Builder(requireActivity())
            currentDialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_num_input, null)

            builder.setView(currentDialogView)
            builder.setTitle("Heart rate")
            builder.setPositiveButton("Ok", this)
            builder.setNegativeButton("Cancel", this)
            dialog = builder.create()
        }
        return dialog
    }

    override fun onClick(dialog: DialogInterface?, item: Int) {
        if (item == DialogInterface.BUTTON_POSITIVE) {
            // extract the input and pass it back to the caller
            val data = collectInputs(currentDialogView)
            listener?.onDialogInputReceived(dialogId, data)
            Toast.makeText(requireActivity(), "$data", Toast.LENGTH_SHORT).show()
        } else if (item == DialogInterface.BUTTON_NEGATIVE) {
            Toast.makeText(requireActivity(), "Cancel clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as DialogInputListener
        } catch (e: ClassCastException) {
            null
        }
    }

    private fun collectInputs(view: View?): Bundle {
        val bundle = Bundle()
        if (view == null) return bundle

        fun traverse(v: View) {
            when (v) {
                is EditText -> {
                    // get the id and put the string into a text
                    val key = v.resources.getResourceEntryName(v.id)
                    bundle.putString(key, v.text.toString())
                }
                is CheckBox -> {
                    // get id and put bool into the data
                    val key = v.resources.getResourceEntryName(v.id)
                    bundle.putBoolean(key, v.isChecked)
                }
                is RadioGroup -> {
                    // see which button is checked then put it as data
                    val checkedId = v.checkedRadioButtonId
                    if (checkedId != -1) {
                        val radioButton = v.findViewById<RadioButton>(checkedId)
                        val key = v.resources.getResourceEntryName(v.id)
                        bundle.putString(key, radioButton.text.toString())
                    }
                }
            }

            // If this view is a ViewGroup (like LinearLayout, ConstraintLayout, etc.), go deeper
            if (v is ViewGroup) {
                for (i in 0 until v.childCount) {
                    traverse(v.getChildAt(i))
                }
            }
        }

        traverse(view)
        return bundle
    }
}