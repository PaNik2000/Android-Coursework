package com.example.androidcoursework

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {

    private val currentClasses = ArrayList<MyClass>() // Список тех занятий, что мы отображаем
    val DBList = ArrayList<MyClass>()                 // Список всех занятий из БД
    lateinit var adapter : CalendarListAdapter
    val currentDate = Calendar.getInstance()            // Текущая выбранная дата в календаре
    lateinit var dbHelper : DBHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        adapter = CalendarListAdapter(activity as Context, R.layout.calendar_list_element, currentClasses)
        dbHelper = DBHelper(activity as Context)

        val calendarView = view.findViewById(R.id.calendarView) as CalendarView
        val listView = view.findViewById(R.id.calendarList) as ListView

        DBList.clear()
        DBList.addAll(dbHelper.getClasses())
        calendarView.setOnDateChangeListener(object : CalendarView.OnDateChangeListener{
            override fun onSelectedDayChange(
                view: CalendarView,
                year: Int,
                month: Int,
                dayOfMonth: Int
            ) {
                currentClasses.clear()
                currentDate.set(year, month, dayOfMonth, 0, 0)

                for (clas in DBList) {
                    if (SimpleDateFormat("dd.MM.yyyy").parse(clas.endDate).before(currentDate.time)) {
                        continue
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

                        while (true) {
                            if (dayOfWeek.contains(firstClass.get(Calendar.DAY_OF_WEEK))) {
                                break
                            }
                            firstClass.add(Calendar.DAY_OF_MONTH, 1)
                        }

                        while(true) {
                            if (currentDate.get(Calendar.YEAR) == firstClass.get(Calendar.YEAR) &&
                                currentDate.get(Calendar.MONTH) == firstClass.get(Calendar.MONTH) &&
                                currentDate.get(Calendar.DAY_OF_MONTH) == firstClass.get(Calendar.DAY_OF_MONTH)) {
                                currentClasses.add(clas)
                                break
                            }
                            if (firstClass.after(currentDate)) break
                            firstClass.add(Calendar.DAY_OF_MONTH, clas.repeatFreq * 7)
                        }
                    }
                    else {
                        while(true) {
                            if (currentDate.get(Calendar.YEAR) == firstClass.get(Calendar.YEAR) &&
                                currentDate.get(Calendar.MONTH) == firstClass.get(Calendar.MONTH) &&
                                currentDate.get(Calendar.DAY_OF_MONTH) == firstClass.get(Calendar.DAY_OF_MONTH)) {
                                currentClasses.add(clas)
                                break
                            }
                            if (firstClass.after(currentDate)) break
                            firstClass.add(Calendar.DAY_OF_MONTH, clas.repeatFreq)
                        }
                    }
                }

                currentClasses.sortWith(TimeClassComparator())
                adapter.notifyDataSetChanged()
            }
        })

        calendarView.date = currentDate.timeInMillis

        listView.adapter = adapter

        initiateCalendar()

        return view
    }

    private fun initiateCalendar() {
        currentClasses.clear()

        for (clas in DBList) {
            if (SimpleDateFormat("dd.MM.yyyy").parse(clas.endDate).before(currentDate.time)) {
                continue
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

                while (true) {
                    if (dayOfWeek.contains(firstClass.get(Calendar.DAY_OF_WEEK))) {
                        break
                    }
                    firstClass.add(Calendar.DAY_OF_MONTH, 1)
                }

                while(true) {
                    if (currentDate.get(Calendar.YEAR) == firstClass.get(Calendar.YEAR) &&
                        currentDate.get(Calendar.MONTH) == firstClass.get(Calendar.MONTH) &&
                        currentDate.get(Calendar.DAY_OF_MONTH) == firstClass.get(Calendar.DAY_OF_MONTH)) {
                        currentClasses.add(clas)
                        break
                    }
                    if (firstClass.after(currentDate)) break
                    firstClass.add(Calendar.DAY_OF_MONTH, clas.repeatFreq * 7)
                }
            }
            else {
                while(true) {
                    if (currentDate.get(Calendar.YEAR) == firstClass.get(Calendar.YEAR) &&
                        currentDate.get(Calendar.MONTH) == firstClass.get(Calendar.MONTH) &&
                        currentDate.get(Calendar.DAY_OF_MONTH) == firstClass.get(Calendar.DAY_OF_MONTH)) {
                        currentClasses.add(clas)
                        break
                    }
                    if (firstClass.after(currentDate)) break
                    firstClass.add(Calendar.DAY_OF_MONTH, clas.repeatFreq)
                }
            }
        }

        currentClasses.sortWith(TimeClassComparator())
        adapter.notifyDataSetChanged()
    }

}

class CalendarListAdapter(context : Context, val resource: Int, objects: MutableList<MyClass>) : ArrayAdapter<MyClass>(context, resource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val clas = getItem(position)
        var view = convertView

        val dbHelper = DBHelper(context)
        val subject = dbHelper.getSubjectsById(clas!!.subjectID)
        val schedule = dbHelper.getScheduleById(clas.scheduleID)
        val teacher = dbHelper.getTeachersById(clas.teacherID)

        val subjTypeStr = "${subject?.name} ${clas.type}"

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null)
        }

        (view?.findViewById(R.id.calendarListPosition) as TextView).text = schedule?.position.toString()
        (view.findViewById(R.id.calendarListColor) as View).background = getDrawable(context, colorResources[idsOfColors.indexOf(subject?.color)])
        (view.findViewById(R.id.calendarListSubjType) as TextView).text = subjTypeStr
        (view.findViewById(R.id.calendarListTeacher) as TextView).text = teacher?.name

        return view
    }
}