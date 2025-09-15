package dev.viviantung.myruns

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import dev.viviantung.myruns.ui.theme.MyRunsTheme
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.widget.RadioGroup
import android.widget.RadioButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import java.io.File


// id probably put the camera logic here
class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    // private lateinit var textView: TextView
    private lateinit var changeButton: Button
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var tempImgUri: Uri
    private lateinit var myViewModel: MyViewModel

    private lateinit var cameraResult: ActivityResultLauncher<Intent>

    private val tempImgFileName = "temp_img.jpg"
    private var line: String? = "..."
    // private val TEXTVIEW_KEY = "textview_key"
    private val profileHelper = Profile()

    // var for edit texts
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var classEditText: EditText
    private lateinit var majorEditText: EditText

    val saved_profile = "Your profile data is saved!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        imageView = findViewById(R.id.imageProfile)
        // textView = findViewById(R.id.textView) // why do we need this?
        changeButton = findViewById(R.id.btnChangePhoto)
        saveButton = findViewById(R.id.btnSave)
        cancelButton = findViewById(R.id.btnCancel)
        genderRadioGroup = findViewById<RadioGroup>(R.id.genderGroup)

        // Initialize views
        nameEditText = findViewById(R.id.editName)
        emailEditText = findViewById(R.id.editEmail)
        phoneEditText = findViewById(R.id.editPhone)
        classEditText = findViewById(R.id.editClass)
        majorEditText = findViewById(R.id.editMajor)

        // load the profile + set the text to the shared preferences
        val profileData = profileHelper.loadProfile(this)
        nameEditText.setText(profileData.name)
        emailEditText.setText(profileData.email)
        phoneEditText.setText(profileData.phone)
        when (profileData.gender) {
            1 -> genderRadioGroup.check(R.id.radioFemale)
            2 -> genderRadioGroup.check(R.id.radioMale)
            else -> genderRadioGroup.clearCheck() // nothing selected
        }
        if (profileData.userClass != 0) {
            classEditText.setText(profileData.userClass.toString())
        } else {
            classEditText.text.clear() // show hint if no class saved
        }
        majorEditText.setText(profileData.major)

        val tempImgFile = File(
            getExternalFilesDir(null),
            tempImgFileName
        )
        tempImgUri = FileProvider.getUriForFile(
            this,
            "dev.viviantung.myruns", tempImgFile
        )

        Util.checkPermissions(this)
        changeButton.setOnClickListener() {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImgUri)
            cameraResult.launch(intent)
        }

        cameraResult = registerForActivityResult(StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = Util.getBitmap(this, tempImgUri)
                myViewModel.userImage.value = bitmap

                line = tempImgUri.path.toString()
                // textView.setText(line)
            }
        }

        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        myViewModel.userImage.observe(this, { it ->
            imageView.setImageBitmap(it)
        })

        if (tempImgFile.exists()) {
            val bitmap = Util.getBitmap(this, tempImgUri)
            imageView.setImageBitmap(bitmap)
        }
        // ignore the text view for now
//        if(savedInstanceState != null)
//            line = savedInstanceState.getString(TEXTVIEW_KEY);
//
//        textView.setText(line)

        saveButton.setOnClickListener() {
            var genderValue = -1
            val selectedId = genderRadioGroup.getCheckedRadioButtonId()
            if (selectedId != -1) {
                val selectedButton = findViewById<RadioButton>(selectedId)
                genderValue = selectedButton.tag.toString().toInt()
            }

            val updatedProfile = profileData.copy(
                name = nameEditText.text.toString(),
                email = emailEditText.text.toString(),
                phone = phoneEditText.text.toString(),
                gender = genderValue,
                userClass = classEditText.text.toString().toIntOrNull() ?: 0,
                major = majorEditText.text.toString()
            )
            profileHelper.saveProfile(this, updatedProfile)

            Toast.makeText(this, saved_profile, Toast.LENGTH_LONG).show()
        }

        cancelButton.setOnClickListener() {
            finish();
        }
    }
}