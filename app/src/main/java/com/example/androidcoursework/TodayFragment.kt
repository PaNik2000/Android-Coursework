package com.example.androidcoursework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*
import kotlin.collections.ArrayList

class TodayFragment : Fragment(){

    lateinit var myAdapter : TodayPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_today, container, false)
        myAdapter = TodayPagerAdapter(requireActivity().supportFragmentManager)
        (view.findViewById(R.id.todayViewPager) as ViewPager).adapter = myAdapter

        val beginDate = Calendar.getInstance()
        beginDate.set(TodayPagerAdapter.BEGIN_YEAR, TodayPagerAdapter.BEGIN_MONTH, TodayPagerAdapter.BEGIN_DAY)
        val endDate = Calendar.getInstance()
        endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH))


        var dayCount = 0
        while(true) {
            if (beginDate.get(Calendar.YEAR) == endDate.get(Calendar.YEAR) &&
                beginDate.get(Calendar.MONTH) == endDate.get(Calendar.MONTH) &&
                beginDate.get(Calendar.DAY_OF_MONTH) == endDate.get(Calendar.DAY_OF_MONTH)) {
                break
            }
            beginDate.add(Calendar.DAY_OF_MONTH, 1)
            dayCount++
        }

        (view.findViewById(R.id.todayViewPager) as ViewPager).currentItem = dayCount

        return view
    }

    fun removeFragments() {
        myAdapter.removeFragments()
    }
}

class TodayPagerAdapter(val fm: FragmentManager) : FragmentPagerAdapter(fm) {

    companion object {
        const val BEGIN_YEAR = 2015
        const val BEGIN_MONTH = Calendar.JANUARY
        const val BEGIN_DAY = 1

        const val END_YEAR = 2030
        const val END_MONTH = Calendar.JANUARY
        const val END_DAY = 1
    }

    val fragments = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        val fragment : Fragment
        val date = Calendar.getInstance()
        date.set(BEGIN_YEAR, BEGIN_MONTH, BEGIN_DAY)

        date.add(Calendar.DAY_OF_MONTH, position)

        fragment = DayFragment(date)
        fragments.add(fragment)

        return fragment
    }

    override fun getCount(): Int {
        val beginDate = Calendar.getInstance()
        beginDate.set(BEGIN_YEAR, BEGIN_MONTH, BEGIN_DAY)
        val endDate = Calendar.getInstance()
        endDate.set(END_YEAR, END_MONTH, END_DAY)

        var dayCount = 0
        while(true) {
            if (beginDate.get(Calendar.YEAR) == endDate.get(Calendar.YEAR) &&
                beginDate.get(Calendar.MONTH) == endDate.get(Calendar.MONTH) &&
                beginDate.get(Calendar.DAY_OF_MONTH) == endDate.get(Calendar.DAY_OF_MONTH)) {
                break
            }
            beginDate.add(Calendar.DAY_OF_MONTH, 1)
            dayCount++
        }

        return dayCount
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val date = Calendar.getInstance()
        val currentDate = Calendar.getInstance()
        var pageTitle : String
        date.set(BEGIN_YEAR, BEGIN_MONTH, BEGIN_DAY)

        date.add(Calendar.DAY_OF_MONTH, position)

        val pageTitleDay : String = if(date.get(Calendar.DAY_OF_MONTH) <= 9) "0${date.get(Calendar.DAY_OF_MONTH)}" else "${date.get(Calendar.DAY_OF_MONTH)}"
        val pageTitleMonth : String = if(date.get(Calendar.MONTH) + 1 <= 9) "0${date.get(Calendar.MONTH) + 1}" else "${date.get(Calendar.MONTH) + 1}"
        val pageTitleYear : String = date.get(Calendar.YEAR).toString()

        if(date.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH) &&
            date.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
            date.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR))
            pageTitle = "Сегодня $pageTitleDay.$pageTitleMonth.$pageTitleYear"
        else pageTitle = "$pageTitleDay.$pageTitleMonth.$pageTitleYear"

        return pageTitle
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