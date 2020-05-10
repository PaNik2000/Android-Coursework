package com.example.androidcoursework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodayFragment : Fragment(){

    lateinit var myAdapter : TodayPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = TodayPagerAdapter(requireActivity().supportFragmentManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_today, container, false)
        (view.findViewById(R.id.todayViewPager) as ViewPager).adapter = myAdapter

        val beginDate = Calendar.getInstance()
        beginDate.set(2015, 0, 1, 0, 0)
        val endDate = Calendar.getInstance()
        endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH), 0, 0)

        val diff = endDate.timeInMillis - beginDate.timeInMillis
        val dayCount = diff / (24 * 60 * 60 * 1000)

        (view.findViewById(R.id.todayViewPager) as ViewPager).currentItem = dayCount.toInt()

        return view
    }

    fun removeFragments() {
        myAdapter.removeFragments()
    }
}

class TodayPagerAdapter(val fm: FragmentManager) : FragmentPagerAdapter(fm) {

    val fragments = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        val fragment : Fragment
        val temp = Calendar.getInstance()
        temp.set(2015, 0, 1, 0, 0)

        temp.add(Calendar.DAY_OF_MONTH, position)

        fragment = DayFragment(temp)
        fragments.add(fragment)

        return fragment
    }

    override fun getCount(): Int {
        val beginDate = Calendar.getInstance()
        beginDate.set(2015, 0, 1, 0, 0)
        val endDate = Calendar.getInstance()
        endDate.set(2030, 0, 1, 0, 0)

        val diff = endDate.timeInMillis - beginDate.timeInMillis
        val dayCount = diff / (24 * 60 * 60 * 1000)

        return dayCount.toInt()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val date = Calendar.getInstance()
        date.set(2015, 0, 1, 0, 0)

        date.add(Calendar.DAY_OF_MONTH, position)

        val str = date.get(Calendar.DAY_OF_MONTH).toString() +
                "." + (date.get(Calendar.MONTH) + 1).toString() +
                "." + date.get(Calendar.YEAR).toString()

        return str
    }

    fun removeFragments() {
        val temp = fm.beginTransaction()
        for (frag in fragments)
        {
            temp.remove(frag)
        }
        temp.commit()
    }
}