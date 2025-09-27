package dev.viviantung.myruns

import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.app.Dialog
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.widget.TextView



class BaseDialog(
    private val onOptionSelected: ((option: Int) -> Unit)? = null
): DialogFragment(), DialogInterface.OnClickListener {

    companion object{
        const val DIALOG_KEY = "DIALOG KEY"
        const val UNIT_PREFERENCE_DIALOG = 1
        const val COMMENT_DIALOG = 2
        const val GALLERY_DIALOG = 3

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

            builder.setView(view)
            builder.setTitle("Select which unit to use")
            builder.setNegativeButton("Cancel", this)
            dialog = builder.create()

        } else if(dialogId == COMMENT_DIALOG) {
            var builder = AlertDialog.Builder(requireActivity())
            val view = requireActivity().layoutInflater.inflate(R.layout.dialog_comment, null)

            builder.setView(view)
            builder.setTitle("Comment")
            builder.setPositiveButton("Ok", this)
            builder.setNegativeButton("Cancel", this)
            dialog = builder.create()
        }

        return dialog
    }

    override fun onClick(dialog: DialogInterface?, item: Int) {
        if(item == DialogInterface.BUTTON_POSITIVE)
            Toast.makeText(requireActivity(), "ok clicked", Toast.LENGTH_SHORT).show()
        else if(item == DialogInterface.BUTTON_NEGATIVE)
            Toast.makeText(requireActivity(), "cancel clicked", Toast.LENGTH_SHORT).show()
    }
}