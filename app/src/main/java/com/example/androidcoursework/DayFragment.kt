package com.example.androidcoursework

import android.content.Context
import android.os.Bundle
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
    lateinit var adapter : DayListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)

        currentClasses.clear()

        val listView = view.findViewById(R.id.dayList) as ListView

        // Временно записал даты начала и конца занятий
        val date1 = Calendar.getInstance()
        date1.set(2020, 1, 2)
        val strDate1 = date1.get(Calendar.DAY_OF_MONTH).toString() + "." +
                                (date1.get(Calendar.MONTH) + 1).toString() + "." +
                                date1.get(Calendar.YEAR).toString()
        val date2 = Calendar.getInstance()
        date2.set(2020, 1, 9)
        val strDate2 = date2.get(Calendar.DAY_OF_MONTH).toString() + "." +
                (date2.get(Calendar.MONTH) + 1).toString() + "." +
                date2.get(Calendar.YEAR).toString()
        val date3 = Calendar.getInstance()
        date3.set(2020, 4, 30)
        val strDate3 = date3.get(Calendar.DAY_OF_MONTH).toString() + "." +
                (date3.get(Calendar.MONTH) + 1).toString() + "." +
                date3.get(Calendar.YEAR).toString()

        // TODO Вставить выборку из БД
        DBList.clear()
        DBList.add(MyClass(1, 1, 2, 1, "Lecture", strDate1, strDate3, 10000, "1", "1"))
        DBList.add(MyClass(2, 1, 1, 1, "Practice", strDate1, strDate3, 10000, "1", "2"))
        DBList.add(MyClass(3, 2, 1, 3, "Lecture", strDate2, strDate3, 1000000, "1", "2"))

        for (clas in DBList) {

            if (SimpleDateFormat("dd.MM.yyyy").parse(clas.endDate).before(date.time)) {
                continue
            }

            val firstClass = Calendar.getInstance()
            firstClass.time = SimpleDateFormat("dd.MM.yyyy").parse(clas.startDate)

            if (clas.repeatType == "1") {
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
                    if (date.get(Calendar.YEAR) == firstClass.get(Calendar.YEAR) &&
                        date.get(Calendar.MONTH) == firstClass.get(Calendar.MONTH) &&
                        date.get(Calendar.DAY_OF_MONTH) == firstClass.get(Calendar.DAY_OF_MONTH)) {
                        currentClasses.add(clas)
                        break
                    }
                    if (firstClass.after(date)) break
                    firstClass.add(Calendar.DAY_OF_MONTH, clas.repeatFreq.toInt() * 7)
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
                    firstClass.add(Calendar.DAY_OF_MONTH, clas.repeatFreq.toInt())
                }
            }
        }

        currentClasses.sortWith(TimeClassComparator())

        adapter = DayListAdapter(activity as Context, R.layout.day_list_element, currentClasses)
        listView.adapter = adapter

        return view
    }
}

class DayListAdapter(context : Context, val resource: Int, objects: MutableList<MyClass>) : ArrayAdapter<MyClass>(context, resource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val clas = getItem(position)
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null)
        }

        // TODO Выбор цвета в соответствии с id цвета
        val drawable = context.resources.getDrawable(R.drawable.blue)

        // TODO Вставка названия предмета и имени преподавателя в соответсвии с id
        (view?.findViewById(R.id.dayListColor) as View).background = drawable
        (view.findViewById(R.id.dayListSubjType) as TextView).text = clas?.subjectID.toString() + " " + clas?.type
        (view.findViewById(R.id.dayListTeacher) as TextView).text = clas?.teacherID.toString()

        return view
    }
}