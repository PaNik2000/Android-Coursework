package com.example.androidcoursework

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class ClassInfo(val color: Drawable, val classType: String, val teacherID: String, val positionID: String, val weekDay: String)

class ClassListAdapter(context : Context, val resource: Int, objects: MutableList<ClassInfo>)
    : ArrayAdapter<ClassInfo>(context, resource, objects){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val mClass = getItem(position)
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null)
        }

        (view?.findViewById(R.id.classColor) as View).background = mClass?.color
        (view?.findViewById(R.id.classType) as TextView).text = mClass?.classType
        (view?.findViewById(R.id.teacherID) as TextView).text = mClass?.teacherID
        (view?.findViewById(R.id.positionID) as TextView).text = mClass?.positionID
        (view?.findViewById(R.id.weekDay) as TextView).text = mClass?.weekDay

        return view
    }
}

class ClassesInSubjectActivity : AppCompatActivity() {

    private val classesArray = ArrayList<ClassInfo>()
    // Кастомный тулбар наверху
    lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classes_in_subject)

        toolbar = findViewById(R.id.classToolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "NAME_OF_TERM" //Взять с БД название
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        val drawable1 = resources.getDrawable(R.drawable.light_blue)

        classesArray.add(ClassInfo(drawable1, "Лекция", "Шашлыков Н.А.", "1 Пара", "Пн, Вт"))
        classesArray.add(ClassInfo(drawable1, "Практика", "Лох ААаА.", "2 Пара", "Ср, Пт"))
        classesArray.add(ClassInfo(drawable1, "Лекция", "Жаба Краба", "69 Пара", "Чт, Сб, Вс"))

        val classesList = findViewById(R.id.classesList) as ListView
        classesList.adapter = ClassListAdapter(this, R.layout.class_list_element, classesArray)
        classesList.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, itemClicked: View, position: Int, id: Long) {
                //TODO intent
                Log.d("weekType", "${itemClicked.findViewById<TextView>(R.id.weekDay).text}")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_classes_in_subject, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.changeClass -> {
                //Открываем addNewSubjectActivity, чтобы настроить/изменить его
                val intentToSubjAdd = Intent(this, AddNewSubjectActivity::class.java)
                intentToSubjAdd.putExtra("Create or change", "change")
                startActivity(intentToSubjAdd)
            }
            R.id.deleteClass->{
                //Удаляем term из БД и возвращаемся в планер
                finish()
            }
            android.R.id.home ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onAddClassButtonClick(view: View){
        //Переходим к добавлению нового class'a
//        val intentToSubAdd = Intent(this, AddNewSubjectActivity::class.java)
//        startActivity(intentToSubAdd)
    }
}
