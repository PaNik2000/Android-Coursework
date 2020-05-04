package com.example.androidcoursework

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

//TODO Исправить ошибку с отображением в ViewPager

class MyPageAdapter(val fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private val scheduleFragment = ScheduleFragment()
    private val termFragment = TermFragment()
    private val teacherFragment = TeacherFragment()

    override fun getItem(position: Int): Fragment {
        val fragment : Fragment
        when (position) {
            0 -> {
                fragment = scheduleFragment
            }
            1 -> {
                fragment = termFragment
            }
            2 -> {
                fragment = teacherFragment
            }
            else -> {
                fragment = termFragment
            }
        }

        Log.d("abc", "get item $position")

        return fragment
    }

    override fun getCount(): Int {
        return 3
    }

    fun removeFragments() {
        fm.beginTransaction().remove(scheduleFragment)
            .remove(termFragment)
            .remove(teacherFragment)
            .commit()
    }
}