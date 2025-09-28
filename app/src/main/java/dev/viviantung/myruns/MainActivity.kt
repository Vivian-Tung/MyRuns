package dev.viviantung.myruns

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
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
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Toast


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

    private var url = "https://www.sfu.ca/fas/computing.html"
    private var TAG = "dialog tag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.tool_bar)
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


        // start tab ============================================================================================
    }



    // settings tab ============================================================================================

    // when the user profile is clicked, we want to launch a new activity
    fun onProfileClick(view: View) {
        val intent: Intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    // when user clicks on unit preferences, open up dialog with 2 radio buttons
    fun onUnitPreferenceClick(view: View) {
        val dialog = BaseDialog()
        val args = Bundle()
        args.putInt(BaseDialog.DIALOG_KEY, BaseDialog.UNIT_PREFERENCE_DIALOG)
        dialog.arguments = args
        dialog.show(supportFragmentManager, TAG)
        // TODO: what do i do with the comments? do i have to save them to shared prefs?
    }


    // when user clicks on comments, open up dialog with text input
    fun onCommentClick(view: View) {
        val dialog = BaseDialog()
        val args = Bundle()
        args.putInt(BaseDialog.DIALOG_KEY, BaseDialog.COMMENT_DIALOG)
        dialog.arguments = args
        dialog.show(supportFragmentManager, TAG)
        // TODO: what do i do with the comments? do i have to save them to shared prefs?
    }

    // when user clicks on homepage, open in chrome
    fun onUrlClick(view: View) {
        val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(urlIntent)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
}
