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

    private val scheduleList = ArrayList<String>()

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

        val listView = view.findViewById(R.id.scheduleList) as ListView

        if (scheduleList.isEmpty()) {
            scheduleList.add("9:00 - 10:30")
            scheduleList.add("10:40 - 12:10")
            scheduleList.add("13:10 - 14:40")
            scheduleList.add("14:50 - 16:20")
            scheduleList.add("4:20 - 69")
        }

        listView.adapter = ScheduleListAdapter(activity as Context, R.layout.schedule_list_element, scheduleList)
        listView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, itemClicked: View, position: Int, id: Long) {
                val intentToAddTeacher = Intent(activity, AddNewScheduleActivity::class.java)
                intentToAddTeacher.putExtra("Create or change", "change")
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
}

class ScheduleListAdapter(context : Context, val resource: Int, objects: MutableList<String>)
    : ArrayAdapter<String>(context, resource, objects){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val schedule = getItem(position)
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null)
        }

        (view?.findViewById(R.id.scheduleID) as TextView).text = (position + 1).toString()
        (view.findViewById(R.id.scheduleTime) as TextView).text = schedule

        return view
    }
}