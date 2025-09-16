package dev.viviantung.myruns

import android.app.Activity
import android.content.Context

class Profile {

    data class ProfileData(
        val name: String,
        val email: String,
        val phone: String,
        val gender: Int,
        val userClass: Int,
        val major: String
    )

    fun saveProfile(context: Context?, profileData: ProfileData){
        val sharedPref = context?.getSharedPreferences("ProfilePreferences",Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putString("user_name", profileData.name)
        editor?.putString("user_email", profileData.email)
        editor?.putString("user_phone", profileData.phone)
        editor?.putInt("user_gender", profileData.gender)
        editor?.putInt("user_class", profileData.userClass)
        editor?.putString("user_major", profileData.major)

        editor?.apply()
    }


    fun loadProfile(context: Context?): ProfileData  {
        val sharedPrefs = context?.getSharedPreferences("ProfilePreferences", Context.MODE_PRIVATE)
        return ProfileData(
            sharedPrefs?.getString("user_name", "") ?: "",
            sharedPrefs?.getString("user_email", "") ?: "",
            sharedPrefs?.getString("user_phone", "") ?: "",
            sharedPrefs?.getInt("user_gender", -1) ?: -1,
            sharedPrefs?.getInt("user_class", 0) ?: 0,
            sharedPrefs?.getString("user_major", "") ?: ""
        )
    }
}