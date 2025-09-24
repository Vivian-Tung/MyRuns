package dev.viviantung.myruns

import android.content.Intent
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
        }

        // start tab ============================================================================================
        val inputTypes = resources.getStringArray(R.array.input_array)
        val inputSpinner = findViewById<Spinner>(R.id.input_spinner)



        // settings tab ============================================================================================
        // when the user profile is clicked, we want to launch a new activity
        fun onClick(view: View) {
            val intent: Intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
}
