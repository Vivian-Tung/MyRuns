package dev.viviantung.myruns

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.io.FileOutputStream

class SettingsActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var changeButton: Button
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var tempImgUri: Uri
    private lateinit var myViewModel: MyViewModel
    private lateinit var cameraResult: ActivityResultLauncher<Intent>
    private val tempImgFileName = "temp_img.jpg"
    private lateinit var pickImageResult: ActivityResultLauncher<Intent>
    private var tempBitmap: Bitmap? = null // preview before save
    private var savedBitmap: Bitmap? = null // last saved image

    // var for inputs
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var classEditText: EditText
    private lateinit var majorEditText: EditText
    private var TAG = "dialog tag"
    val savedProfile = "Your profile data is saved!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
        setSupportActionBar(toolbar)
        val deleteButton = toolbar.findViewById<Button>(R.id.btn_delete)
        deleteButton.visibility = View.GONE

        // Initialize views
        imageView = findViewById(R.id.imageProfile)
        changeButton = findViewById(R.id.btnChangePhoto)
        saveButton = findViewById(R.id.btnSave)
        cancelButton = findViewById(R.id.btnCancel)
        genderRadioGroup = findViewById<RadioGroup>(R.id.genderGroup)

        nameEditText = findViewById(R.id.editName)
        emailEditText = findViewById(R.id.editEmail)
        phoneEditText = findViewById(R.id.editPhone)
        classEditText = findViewById(R.id.editClass)
        majorEditText = findViewById(R.id.editMajor)


        // observe live data
        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        myViewModel.userImage.observe(this, { it ->
            imageView.setImageBitmap(it)
        })
        myViewModel.profileData.observe(this, { profile ->
            nameEditText.setText(profile.name)
            emailEditText.setText(profile.email)
            phoneEditText.setText(profile.phone)
            when (profile.gender) {
                1 -> genderRadioGroup.check(R.id.radioFemale)
                2 -> genderRadioGroup.check(R.id.radioMale)
                else -> genderRadioGroup.clearCheck() // nothing selected
            }
            if (profile.userClass != 0) {
                classEditText.setText(profile.userClass.toString())
            } else {
                classEditText.text.clear() // show hint if no class saved
            }
            majorEditText.setText(profile.major)
        })

        // load the profile + set the text to the shared preferences
        myViewModel.loadProfile(this)

        // image setting
        val tempImg = File(
            getExternalFilesDir(null),
            tempImgFileName
        )
        tempImgUri = FileProvider.getUriForFile(
            this,
            "dev.viviantung.myruns", tempImg
        )

        Util.checkPermissions(this)
        // need to check permissions for access to media
        changeButton.setOnClickListener() {
            // need to call up dialog first
            val dialog = BaseDialog { option ->
                when(option) {
                    BaseDialog.CAMERA_OPTION -> {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImgUri)
                        cameraResult.launch(intent)
                    }
                    BaseDialog.GALLERY_OPTION -> {
                        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
                        pickImageResult.launch(intent)
                        Toast.makeText(this, "gallery clicked", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            val args = Bundle()
            args.putInt(BaseDialog.DIALOG_KEY, BaseDialog.GALLERY_DIALOG)
            dialog.arguments = args
            dialog.show(supportFragmentManager, TAG)
        }

        cameraResult = registerForActivityResult(StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = Util.getBitmap(this, tempImgUri)
                tempBitmap = bitmap
                myViewModel.userImage.value = bitmap
            }
        }

        pickImageResult = registerForActivityResult(StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    val bitmap = Util.getBitmap(this, it)
                    tempBitmap = bitmap
                    myViewModel.userImage.value = bitmap

                    // save image in storage
                    FileOutputStream(tempImg).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                    }
                    tempImgUri = FileProvider.getUriForFile(
                        this, "dev.viviantung.myruns", tempImg
                    )
                }
            }
        }
        if (tempImg.exists()) {
            val bitmap = Util.getBitmap(this, tempImgUri)
            imageView.setImageBitmap(bitmap)
        }

        // save profile
        saveButton.setOnClickListener() {
            var genderValue = -1
            val selectedId = genderRadioGroup.getCheckedRadioButtonId()
            if (selectedId != -1) {
                val selectedButton = findViewById<RadioButton>(selectedId)
                genderValue = selectedButton.tag.toString().toInt()
            }

            val updatedProfile = Profile.ProfileData(
                name = nameEditText.text.toString(),
                email = emailEditText.text.toString(),
                phone = phoneEditText.text.toString(),
                gender = genderValue,
                userClass = classEditText.text.toString().toIntOrNull() ?: 0,
                major = majorEditText.text.toString()
            )
            myViewModel.saveProfile(this, updatedProfile)

            // save image only if save is clicked
            tempBitmap?.let { bitmap ->
                FileOutputStream(tempImg).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }
                savedBitmap = bitmap
            }

            Toast.makeText(this, savedProfile, Toast.LENGTH_SHORT).show()
            finish();
        }

        cancelButton.setOnClickListener() {
            finish();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
}