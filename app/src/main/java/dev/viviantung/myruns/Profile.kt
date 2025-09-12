package dev.viviantung.myruns

import android.app.Activity
import android.content.Context

// save profile() -> called in oncreate and checks shared pereference object and loads it into display data; if no data saved then put in empty strings
// load profile() -> saves user input into sharedpreference object; then shows toast to tell user data is saved

class Profile {

    data class ProfileData(
        val name: String,
        val email: String,
        val phone: String,
        val gender: Int,
        val userClass: Int,
        val major: String
    )

    fun saveProfile(activity: Activity?){
        // take the input from main activity and put it in a sharedpreference object
        // write inputs to object
//        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
//        val editor = sharedPref?.edit()
//        // retrieve user input and save it
//        editor.putString("user_name", nameEditText.test.toString())

    }


    fun loadProfile(activity: Activity?): ProfileData  {
        val sharedPrefs = activity?.getSharedPreferences("ProfilePreferences", Context.MODE_PRIVATE)
        return ProfileData(
            sharedPrefs?.getString("user_name", "") ?: "",
            sharedPrefs?.getString("user_email", "") ?: "",
            sharedPrefs?.getString("user_phone", "") ?: "",
            sharedPrefs?.getInt("user_gender", 0) ?: 0,
            sharedPrefs?.getInt("user_class", 0) ?: 0,
            sharedPrefs?.getString("user_major", "") ?: ""
        )
    }
}