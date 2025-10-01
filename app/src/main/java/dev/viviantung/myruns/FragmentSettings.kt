package dev.viviantung.myruns

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import androidx.fragment.app.Fragment
class FragmentSettings: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var privacyCheckBox: CheckBox = view.findViewById<CheckBox>(R.id.privacy_cb)
        val prefs = requireContext().getSharedPreferences("ProfilePreferences", Context.MODE_PRIVATE)
        val savedPrivacy = prefs.getBoolean("privacy", false)
        privacyCheckBox.isChecked = savedPrivacy

        privacyCheckBox.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit()
                .putBoolean("privacy", isChecked)
                .apply()
        }
    }
}