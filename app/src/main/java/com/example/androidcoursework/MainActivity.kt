package com.example.androidcoursework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.MenuItem
import androidx.fragment.app.Fragment


class MainActivity : AppCompatActivity() {

    private val todayFragment = TodayFragment()
    private val calendarFragment = CalendarFragment()
    private val plannerFragment = PlannerFragment()

    private val mNavigationListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.getItemId()) {
                    R.id.navigation_today -> {
                        plannerFragment.removeFragments()
                        loadFragment(todayFragment)
                        return true
                    }
                    R.id.navigation_calendar -> {
                        plannerFragment.removeFragments()
                        loadFragment(calendarFragment)
                        return true
                    }
                    R.id.navigation_planner -> {
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
    }
}
