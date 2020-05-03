package com.example.androidcoursework

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

//TODO Исправить ошибку с отображением в ViewPager

class MyPageAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val fragment : Fragment
        when (position) {
            0 -> {
                fragment = ScheduleFragment()
            }
            1 -> {
                fragment = TermFragment()
            }
            2 -> {
                fragment = TeacherFragment()
            }
            else -> {
                fragment = TermFragment()
            }
        }

        Log.d("abc", "get item $position")

        return fragment
    }

    override fun getCount(): Int {
        return 3
    }
}