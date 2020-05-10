package com.example.androidcoursework

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyPageAdapter(val fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    val SCHEDULE_POSITION = 0
    val TERM_POSITION = 1
    val TEACHER_POSITION = 2

    val FRAGMENT_COUNT = 3

    private val scheduleFragment = ScheduleFragment()
    private val termFragment = TermFragment()
    private val teacherFragment = TeacherFragment()

    override fun getItem(position: Int): Fragment {
        val fragment : Fragment
        when (position) {
            SCHEDULE_POSITION -> {
                fragment = scheduleFragment
            }
            TERM_POSITION -> {
                fragment = termFragment
            }
            TEACHER_POSITION -> {
                fragment = teacherFragment
            }
            else -> {
                fragment = termFragment
            }
        }

        return fragment
    }

    override fun getCount(): Int {
        return FRAGMENT_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            SCHEDULE_POSITION -> {
                return "Schedule"
            }
            TERM_POSITION -> {
                return "Term"
            }
            TEACHER_POSITION -> {
                return "Teacher"
            }
            else -> {
                return "Error"
            }
        }
    }

    fun removeFragments() {
        fm.beginTransaction().remove(scheduleFragment)
            .remove(termFragment)
            .remove(teacherFragment)
            .commit()
    }
}