package com.example.androidcoursework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.util.*
import kotlin.collections.ArrayList

class ScheduleFragment : Fragment() {

    lateinit var scheduleList: MutableList<Schedule>
    lateinit var  listView: ListView
    lateinit var sortedScheduleList: MutableList<Schedule>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("abc", "schedule fragment")

        val view = inflater.inflate(R.layout.fragment_schedule, container, false)

        listView = view.findViewById(R.id.scheduleList) as ListView
        scheduleList = DBHelper(activity as Context).getSchedule()
        sortedScheduleList = sortSchedules(scheduleList)

        listView.adapter = ScheduleListAdapter(activity as Context, R.layout.schedule_list_element, sortedScheduleList)
        listView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, itemClicked: View, position: Int, id: Long) {
                val intentToAddTeacher = Intent(activity, AddNewScheduleActivity::class.java)
                intentToAddTeacher.putExtra("Create or change", "change")
                intentToAddTeacher.putExtra("scheduleID", sortedScheduleList[position].ID)
                startActivity(intentToAddTeacher)
            }
        })

        (view.findViewById(R.id.addScheduleButton) as FloatingActionButton).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intentToAddTeacher = Intent(activity, AddNewScheduleActivity::class.java)
                intentToAddTeacher.putExtra("Create or change", "create")
                startActivity(intentToAddTeacher)
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        scheduleList = DBHelper(activity as Context).getSchedule()
        sortedScheduleList = sortSchedules(scheduleList)
        listView.adapter = ScheduleListAdapter(activity as Context, R.layout.schedule_list_element, sortedScheduleList)
        for(schedule in sortedScheduleList){
            Log.d("ttt", "ID: ${schedule.ID} pos: ${schedule.position}")
        }
    }

    fun sortSchedules(scheduleList: MutableList<Schedule>) : MutableList<Schedule>{
        val sortedSchedule = mutableListOf<Schedule>()

        for(i in 1..scheduleList.size){
            for(schedule in scheduleList){
                if(schedule.position == i){
                    sortedSchedule.add(schedule)
                    break
                }
            }
        }
        return sortedSchedule
    }

}

class ScheduleListAdapter(context : Context, val resource: Int, objects: MutableList<Schedule>)
    : ArrayAdapter<Schedule>(context, resource, objects){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val schedule = getItem(position)
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null)
        }

        (view?.findViewById(R.id.scheduleID) as TextView).text = schedule?.position.toString()
        (view.findViewById(R.id.scheduleTime) as TextView).text = "${schedule?.startTime} ${schedule?.endTime}"

        return view
    }
}