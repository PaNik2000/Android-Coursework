package com.example.androidcoursework

import android.content.Context
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

class TeacherFragment : Fragment() {

    private val teacherList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("abc", "teacher fragment")

        val view = inflater.inflate(R.layout.fragment_teacher, container, false)

        val listView = view.findViewById(R.id.teacherList) as ListView

        if (teacherList.isEmpty()) {
            teacherList.add("Берков Николай Андреевич")
            teacherList.add("Платонова Ольга Владимировна")
            teacherList.add("Оамохин Гелс РоманоВИЧ")
        }

        listView.adapter = TeacherListAdapter(activity as Context, R.layout.teacher_list_element, teacherList)
        listView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, itemClicked: View, position: Int, id: Long) {
                //TODO intent
            }
        })

        (view.findViewById(R.id.addTeacherButton) as FloatingActionButton).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //TODO intent
            }
        })

        return view
    }

}

class TeacherListAdapter(context : Context, val resource: Int, objects: MutableList<String>)
    : ArrayAdapter<String>(context, resource, objects){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val schedule = getItem(position)
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null)
        }

        (view?.findViewById(R.id.teacherName) as TextView).text = schedule

        return view
    }
}