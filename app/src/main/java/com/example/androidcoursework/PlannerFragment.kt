package com.example.androidcoursework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

class PlannerFragment : Fragment() {

    lateinit var myAdapter : PlannerPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_planner, container, false)
        myAdapter = PlannerPagerAdapter(requireActivity().supportFragmentManager)
        (view.findViewById(R.id.viewPager) as ViewPager).adapter = myAdapter
        (view.findViewById(R.id.viewPager) as ViewPager).currentItem = PlannerPagerAdapter.TERM_POSITION
        return view
    }

    fun removeFragments() {
        myAdapter.removeFragments()
    }

}

class PlannerPagerAdapter(val fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    companion object {
        const val SCHEDULE_POSITION = 0
        const val TERM_POSITION = 1
        const val TEACHER_POSITION = 2

        const val FRAGMENT_COUNT = 3
    }

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
                return "Расписание пар"
            }
            TERM_POSITION -> {
                return "Семестры"
            }
            TEACHER_POSITION -> {
                return "Преподаватели"
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