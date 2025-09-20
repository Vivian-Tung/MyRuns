package dev.viviantung.myruns

import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import java.util.ArrayList
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    private lateinit var fragmentStart: FragmentStart
    private lateinit var fragmentHistory: FragmentHistory
    private lateinit var fragmentSettings: FragmentSettings
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout

    private lateinit var myMyFragmentStateAdapter: MyFragmentStateAdapter
    private lateinit var fragments: ArrayList<Fragment>
    private val tabTitles = arrayOf("Start", "History", "Settings") // Tab titles
    private lateinit var tabConfigurationStrategy: TabConfigurationStrategy
    private lateinit var tabLayoutMediator: TabLayoutMediator



//    private lateinit var imageView: ImageView
//    private lateinit var changeButton: Button
//    private lateinit var saveButton: Button
//    private lateinit var cancelButton: Button
//    private lateinit var tempImgUri: Uri
//    private lateinit var myViewModel: MyViewModel
//    private lateinit var cameraResult: ActivityResultLauncher<Intent>
//
//    private val tempImgFileName = "temp_img.jpg"
//
//    // var for inputs
//    private lateinit var nameEditText: EditText
//    private lateinit var emailEditText: EditText
//    private lateinit var phoneEditText: EditText
//    private lateinit var genderRadioGroup: RadioGroup
//    private lateinit var classEditText: EditText
//    private lateinit var majorEditText: EditText
//
//    val savedProfile = "Your profile data is saved!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        viewPager2 = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tab)

        fragmentStart = FragmentStart()
        fragmentHistory = FragmentHistory()
        fragmentSettings = FragmentSettings()

        fragments = ArrayList()
        fragments.add(fragmentStart)
        fragments.add(fragmentHistory)
        fragments.add(fragmentSettings)

        myMyFragmentStateAdapter = MyFragmentStateAdapter(this, fragments)
        viewPager2.adapter = myMyFragmentStateAdapter

        tabConfigurationStrategy = TabConfigurationStrategy {
            tab: TabLayout.Tab, position: Int ->
            tab.text = tabTitles[position] }
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2, tabConfigurationStrategy)
        tabLayoutMediator.attach()
        }


//        // Initialize views
//        imageView = findViewById(R.id.imageProfile)
//        changeButton = findViewById(R.id.btnChangePhoto)
//        saveButton = findViewById(R.id.btnSave)
//        cancelButton = findViewById(R.id.btnCancel)
//        genderRadioGroup = findViewById<RadioGroup>(R.id.genderGroup)
//
//        nameEditText = findViewById(R.id.editName)
//        emailEditText = findViewById(R.id.editEmail)
//        phoneEditText = findViewById(R.id.editPhone)
//        classEditText = findViewById(R.id.editClass)
//        majorEditText = findViewById(R.id.editMajor)
//
//
//        // observe live data
//        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
//        myViewModel.userImage.observe(this, { it ->
//            imageView.setImageBitmap(it)
//        })
//        myViewModel.profileData.observe(this, { profile ->
//            nameEditText.setText(profile.name)
//            emailEditText.setText(profile.email)
//            phoneEditText.setText(profile.phone)
//            when (profile.gender) {
//                1 -> genderRadioGroup.check(R.id.radioFemale)
//                2 -> genderRadioGroup.check(R.id.radioMale)
//                else -> genderRadioGroup.clearCheck() // nothing selected
//            }
//            if (profile.userClass != 0) {
//                classEditText.setText(profile.userClass.toString())
//            } else {
//                classEditText.text.clear() // show hint if no class saved
//            }
//            majorEditText.setText(profile.major)
//        })
//
//        // load the profile + set the text to the shared preferences
//        myViewModel.loadProfile(this)
//
//        // image setting
//        val tempImg = File(
//            getExternalFilesDir(null),
//            tempImgFileName
//        )
//        tempImgUri = FileProvider.getUriForFile(
//            this,
//            "dev.viviantung.myruns", tempImg
//        )
//
//        Util.checkPermissions(this)
//        changeButton.setOnClickListener() {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImgUri)
//            cameraResult.launch(intent)
//        }
//
//        cameraResult = registerForActivityResult(StartActivityForResult())
//        { result: ActivityResult ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val bitmap = Util.getBitmap(this, tempImgUri)
//                myViewModel.userImage.value = bitmap
//            }
//        }
//
//        if (tempImg.exists()) {
//            val bitmap = Util.getBitmap(this, tempImgUri)
//            imageView.setImageBitmap(bitmap)
//        }
//
//        // save profile
//        saveButton.setOnClickListener() {
//            var genderValue = -1
//            val selectedId = genderRadioGroup.getCheckedRadioButtonId()
//            if (selectedId != -1) {
//                val selectedButton = findViewById<RadioButton>(selectedId)
//                genderValue = selectedButton.tag.toString().toInt()
//            }
//
//            val updatedProfile = Profile.ProfileData(
//                name = nameEditText.text.toString(),
//                email = emailEditText.text.toString(),
//                phone = phoneEditText.text.toString(),
//                gender = genderValue,
//                userClass = classEditText.text.toString().toIntOrNull() ?: 0,
//                major = majorEditText.text.toString()
//            )
//            myViewModel.saveProfile(this, updatedProfile)
//
//            Toast.makeText(this, savedProfile, Toast.LENGTH_LONG).show()
//        }
//
//        cancelButton.setOnClickListener() {
//            finish();
//        }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
}
