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

    private val todayFragment = TodayFragment()
    private val calendarFragment = CalendarFragment()
    private val plannerFragment = PlannerFragment()

    lateinit var toolBar : Toolbar

    private val mNavigationListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.getItemId()) {
                    R.id.navigation_today -> {
                        if (plannerFragment.isAdded) plannerFragment.removeFragments()
                        supportActionBar?.title = getString(R.string.title_today)
                        loadFragment(todayFragment)
                        return true
                    }
                    R.id.navigation_calendar -> {
                        if (plannerFragment.isAdded) plannerFragment.removeFragments()
                        if (todayFragment.isAdded) todayFragment.removeFragments()
                        supportActionBar?.title = getString(R.string.title_calendar)
                        loadFragment(calendarFragment)
                        return true
                    }
                    R.id.navigation_planner -> {
                        if (todayFragment.isAdded) todayFragment.removeFragments()
                        supportActionBar?.title = getString(R.string.title_planner)
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

        val dbHelper = DBHelper(this)
        val navigation : BottomNavigationView = findViewById(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mNavigationListener)
        toolBar = findViewById(R.id.mainToolBar)
        setSupportActionBar(toolBar)
        if (dbHelper.getClasses().isEmpty()) {
            navigation.selectedItemId = R.id.navigation_planner
            supportActionBar?.title = "Планировщик"
        }
        else {
            navigation.selectedItemId = R.id.navigation_today
            supportActionBar?.title = "Сегодня"
        }
    }

    fun onAddTermButtonClick(view: View){
        val intent = Intent(this, AddNewTermActivity::class.java)
        intent.putExtra("Create or change", "create")
        startActivity(intent)
    }
}