package com.example.androidcoursework

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class DayFragment(val date : Calendar) : Fragment() {

    private val currentClasses = ArrayList<MyClass>() // Список тех занятий, что мы отображаем
    val DBList = ArrayList<MyClass>()                 // Список всех занятий из БД
    lateinit var adapter : CalendarListAdapter
    lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        dbHelper = DBHelper(activity as Context)

        currentClasses.clear()

        val listView = view.findViewById(R.id.dayList) as ListView

        DBList.clear()
        DBList.addAll(dbHelper.getClasses())
        for (clas in DBList) {

            val calEndDate = Calendar.getInstance()
            calEndDate.time = SimpleDateFormat("dd.MM.yyyy").parse(clas.endDate)
            if (date.get(Calendar.YEAR) > calEndDate.get(Calendar.YEAR)) {
                continue
            }
            else if (date.get(Calendar.YEAR) == calEndDate.get(Calendar.YEAR)) {
                if (date.get(Calendar.MONTH) > calEndDate.get(Calendar.MONTH)) {
                    continue
                }
                else if (date.get(Calendar.MONTH) == calEndDate.get(Calendar.MONTH)) {
                    if (date.get(Calendar.DAY_OF_MONTH) > calEndDate.get(Calendar.DAY_OF_MONTH)) {
                        continue
                    }
                }
            }

            val firstClass = Calendar.getInstance()
            firstClass.time = SimpleDateFormat("dd.MM.yyyy").parse(clas.startDate)

            if (clas.repeatType == RepeatTypes.WEEK.TYPE) {
                val dayOfWeek = ArrayList<Int>()
                var weekDay = clas.weekDay
                if (weekDay % 10 == 1) dayOfWeek.add(Calendar.SUNDAY)
                weekDay /= 10
                if (weekDay % 10 == 1) dayOfWeek.add(Calendar.SATURDAY)
                weekDay /= 10
                if (weekDay % 10 == 1) dayOfWeek.add(Calendar.FRIDAY)
                weekDay /= 10
                if (weekDay % 10 == 1) dayOfWeek.add(Calendar.THURSDAY)
                weekDay /= 10
                if (weekDay % 10 == 1) dayOfWeek.add(Calendar.WEDNESDAY)
                weekDay /= 10
                if (weekDay % 10 == 1) dayOfWeek.add(Calendar.TUESDAY)
                weekDay /= 10
                if (weekDay % 10 == 1) dayOfWeek.add(Calendar.MONDAY)

                for (day in dayOfWeek) {
                    while (true) {
                        if (day == firstClass.get(Calendar.DAY_OF_WEEK)) {
                            break
                        }

                        firstClass.add(Calendar.DAY_OF_MONTH, 1)
                    }

                    while(true) {
                        if (date.get(Calendar.YEAR) == firstClass.get(Calendar.YEAR) &&
                            date.get(Calendar.MONTH) == firstClass.get(Calendar.MONTH) &&
                            date.get(Calendar.DAY_OF_MONTH) == firstClass.get(Calendar.DAY_OF_MONTH)) {
                            currentClasses.add(clas)
                            break
                        }
                        if (firstClass.after(date)) break
                        firstClass.add(Calendar.DAY_OF_MONTH, clas.repeatFreq * 7)
                    }

                    firstClass.time = SimpleDateFormat("dd.MM.yyyy").parse(clas.startDate)
                }
            }
            else {
                while(true) {
                    if (date.get(Calendar.YEAR) == firstClass.get(Calendar.YEAR) &&
                        date.get(Calendar.MONTH) == firstClass.get(Calendar.MONTH) &&
                        date.get(Calendar.DAY_OF_MONTH) == firstClass.get(Calendar.DAY_OF_MONTH)) {
                        currentClasses.add(clas)
                        break
                    }
                    if (firstClass.after(date)) break
                    firstClass.add(Calendar.DAY_OF_MONTH, clas.repeatFreq)
                }
            }
        }

        currentClasses.sortWith(TimeClassComparator())

        adapter = CalendarListAdapter(activity as Context, R.layout.calendar_list_element, currentClasses)
        listView.adapter = adapter

        return view
    }
}