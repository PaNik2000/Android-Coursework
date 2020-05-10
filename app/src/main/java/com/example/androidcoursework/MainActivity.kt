package com.example.androidcoursework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment


class MainActivity : AppCompatActivity() {

    val DEFAULT_TITLE = "Planner"

    private val todayFragment = TodayFragment()
    private val calendarFragment = CalendarFragment()
    private val plannerFragment = PlannerFragment()

    lateinit var toolBar : Toolbar

    private val mNavigationListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.getItemId()) {
                    R.id.navigation_today -> {
                        plannerFragment.removeFragments()
                        supportActionBar?.title = "Today"
                        loadFragment(todayFragment)
                        return true
                    }
                    R.id.navigation_calendar -> {
                        plannerFragment.removeFragments()
                        if (todayFragment.isAdded) todayFragment.removeFragments()
                        supportActionBar?.title = "Calendar"
                        loadFragment(calendarFragment)
                        return true
                    }
                    R.id.navigation_planner -> {
                        if (todayFragment.isAdded) todayFragment.removeFragments()
                        supportActionBar?.title = "Planner"
                        loadFragment(plannerFragment)
                        return true
                    }
                }
                return false
            }
        }

    private fun loadFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content, fragment)
        ft.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigation : BottomNavigationView = findViewById(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mNavigationListener)
        navigation.selectedItemId = R.id.navigation_planner

        toolBar = findViewById(R.id.mainToolBar)
        setSupportActionBar(toolBar)
        supportActionBar?.title = DEFAULT_TITLE
    }

    fun onAddTermButtonClick(view: View){
        val intent = Intent(this, AddNewTermActivity::class.java)
        intent.putExtra("Create or change", "create")
        startActivity(intent)
    }
}