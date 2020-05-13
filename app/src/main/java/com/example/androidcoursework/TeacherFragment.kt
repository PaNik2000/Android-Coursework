package com.example.androidcoursework

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

    lateinit var teacherList: MutableList<Teacher>
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_teacher, container, false)

        listView = view.findViewById(R.id.teacherList) as ListView
        teacherList = DBHelper(activity as Context).getTeachers()

        listView.adapter = TeacherListAdapter(activity as Context, R.layout.teacher_list_element, teacherList)
        listView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, itemClicked: View, position: Int, id: Long) {
                val intentToAddTeacher = Intent(activity, AddNewTeacherActivity::class.java)
                intentToAddTeacher.putExtra("Create or change", "change")
                intentToAddTeacher.putExtra("teacherID", teacherList[position].ID)
                startActivity(intentToAddTeacher)
            }
        })

        (view.findViewById(R.id.addTeacherButton) as FloatingActionButton).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intentToAddTeacher = Intent(activity, AddNewTeacherActivity::class.java)
                intentToAddTeacher.putExtra("Create or change", "create")
                startActivity(intentToAddTeacher)
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        teacherList = DBHelper(activity as Context).getTeachers()
        listView.adapter = TeacherListAdapter(activity as Context, R.layout.teacher_list_element, teacherList)
    }

}

class TeacherListAdapter(context : Context, val resource: Int, objects: MutableList<Teacher>)
    : ArrayAdapter<Teacher>(context, resource, objects){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val lilTicha = getItem(position)
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null)
        }

        (view?.findViewById(R.id.teacherName) as TextView).text = lilTicha?.name

        return view
    }
}