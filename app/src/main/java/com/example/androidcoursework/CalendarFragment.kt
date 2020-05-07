package com.example.androidcoursework

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.*
import kotlin.collections.ArrayList

// TODO Использовать класс для занятий из другого активити
class TempClass(val id : Int, val subjectId : Int, val type : String, val position : Int,
                val startDate : Calendar, val endDate : Calendar, val weekDay : Int, val repeatType : Int,
                val repeatFreq : Int, val teacherId : Int) : Comparable<TempClass> {
    override fun compareTo(other: TempClass): Int {
        if (this.position < other.position) return -1
        if (this.position > other.position) return 1
        return 0
    }
}

class CalendarFragment : Fragment() {

    private val currentClasses = ArrayList<TempClass>() // Список тех занятий, что мы отображаем
    val DBList = ArrayList<TempClass>()                 // Список всех занятий из БД
    lateinit var adapter : CalendarListAdapter
    val currentDate = Calendar.getInstance()            // Текущая выбранная дата в календаре

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CalendarListAdapter(activity as Context, R.layout.calendar_list_element, currentClasses)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val calendarView = view.findViewById(R.id.calendarView) as CalendarView
        val listView = view.findViewById(R.id.calendarList) as ListView

        // Временно записал даты начала и конца занятий
        val date1 = Calendar.getInstance()
        date1.set(2020, 1, 2)
        val date2 = Calendar.getInstance()
        date2.set(2020, 1, 9)
        val date3 = Calendar.getInstance()
        date3.set(2020, 4, 30)

        // TODO Вставить выборку из БД
        DBList.clear()
        DBList.add(TempClass(1, 1, "Lecture", 2, date1, date3, 10000, 1, 1, 1))
        DBList.add(TempClass(2, 1, "Practice", 1, date1, date3, 10000, 1, 2, 1))
        DBList.add(TempClass(3, 2, "Lecture", 1, date2, date3, 1000000, 1, 2, 3))

        calendarView.setOnDateChangeListener(object : CalendarView.OnDateChangeListener{
            override fun onSelectedDayChange(
                view: CalendarView,
                year: Int,
                month: Int,
                dayOfMonth: Int
            ) {
                currentClasses.clear()
                currentDate.set(year, month, dayOfMonth)

                for (clas in DBList) {
                    if (clas.endDate.before(currentDate)) {
                        continue
                    }

                    val firstClass = Calendar.getInstance()
                    firstClass.set(clas.startDate.get(Calendar.YEAR), clas.startDate.get(Calendar.MONTH), clas.startDate.get(Calendar.DAY_OF_MONTH))

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

                    val diff = currentDate.timeInMillis - firstClass.timeInMillis
                    val dayCount = diff/ (24 * 60 * 60 * 1000)

                    if (clas.repeatType == 1 && dayCount % (clas.repeatFreq * 7) == 0L) {
                        currentClasses.add(clas)
                    }
                    else if (clas.repeatType == 2 && dayCount % clas.repeatFreq == 0L) {
                        currentClasses.add(clas)
                    }
                }

                currentClasses.sort()
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
            if (clas.endDate.before(currentDate)) {
                continue
            }

            val firstClass = Calendar.getInstance()
            firstClass.set(clas.startDate.get(Calendar.YEAR), clas.startDate.get(Calendar.MONTH), clas.startDate.get(Calendar.DAY_OF_MONTH))

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

            val diff = currentDate.timeInMillis - firstClass.timeInMillis
            val dayCount = diff/ (24 * 60 * 60 * 1000)

            if (clas.repeatType == 1 && dayCount % (clas.repeatFreq * 7) == 0L) {
                currentClasses.add(clas)
            }
            else if (clas.repeatType == 2 && dayCount % clas.repeatFreq == 0L) {
                currentClasses.add(clas)
            }
        }

        currentClasses.sort()
        adapter.notifyDataSetChanged()
    }

}

class CalendarListAdapter(context : Context, val resource: Int, objects: MutableList<TempClass>) : ArrayAdapter<TempClass>(context, resource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val clas = getItem(position)
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null)
        }

        // TODO Выбор цвета в соответствии с id цвета
        val drawable = context.resources.getDrawable(R.drawable.blue)

        // TODO Вставка названия предмета и имени преподавателя в соответсвии с id
        (view?.findViewById(R.id.calendarListPosition) as TextView).text = clas?.position.toString()
        (view.findViewById(R.id.calendarListColor) as View).background = drawable
        (view.findViewById(R.id.calendarListSubjType) as TextView).text = clas?.subjectId.toString() + " " + clas?.type
        (view.findViewById(R.id.calendarListTeacher) as TextView).text = clas?.teacherId.toString()

        return view
    }
}