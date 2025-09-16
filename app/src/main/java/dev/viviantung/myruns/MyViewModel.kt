package dev.viviantung.myruns

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class MyViewModel: ViewModel() {
    private val profileStore = Profile()

    val userImage = MutableLiveData<Bitmap>()
    val _profileData = MutableLiveData<Profile.ProfileData>()

    val profileData: LiveData<Profile.ProfileData> get() = _profileData

    // middle layer functions to update the shared prefs
    fun loadProfile(context: Context) {
        _profileData.value = profileStore.loadProfile(context)
    }

    fun saveProfile(context: Context, newProfile: Profile.ProfileData) {
        _profileData.value = newProfile
        profileStore.saveProfile(context, newProfile)
    }

}