package com.example.androidcoursework

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_classes_in_subject.*

class ClassListAdapter(context : Context, val resource: Int, objects: MutableList<MyClass>)
    : ArrayAdapter<MyClass>(context, resource, objects){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val mClass = getItem(position)
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null)
        }

        var weekDay = mClass?.weekDay
        var weekDayStr = ""
        var intArray = IntArray(7)
        var devider = 1000000
        for(i in 0..6){
            intArray[i] = weekDay!!/devider
            weekDay = weekDay % devider
            devider = devider / 10
        }
        if(intArray[0] == 1)
            weekDayStr += "Пн "
        if(intArray[1] == 1)
            weekDayStr += "Вт "
        if(intArray[2] == 1)
            weekDayStr += "Ср "
        if(intArray[3] == 1)
            weekDayStr += "Чт "
        if(intArray[4] == 1)
            weekDayStr += "Пт "
        if(intArray[5] == 1)
            weekDayStr += "Сб "
        if(intArray[6] == 1)
            weekDayStr += "Вс"

        val subjects = DBHelper(context).getSubjects()
        var subject = Subject(-1, "", 1, 1)
        for (subj in subjects) {
            if (mClass?.subjectID == subj.ID)
                subject = subj
        }

        (view?.findViewById(R.id.classColor) as View).background = getDrawable(context, colorResources[idsOfColors.indexOf(subject.color)])
        (view?.findViewById(R.id.classType) as TextView).text = mClass?.type
        (view?.findViewById(R.id.teacherID) as TextView).text = DBHelper(context).getTeachersById(mClass!!.teacherID)?.name
        Log.d("ttt", "${mClass!!.scheduleID}")
        (view?.findViewById(R.id.positionID) as TextView).text = "${DBHelper(context).getScheduleById(mClass!!.scheduleID)?.position} пара"
        (view?.findViewById(R.id.weekDay) as TextView).text = weekDayStr

        return view
    }
}

class ClassesInSubjectActivity : AppCompatActivity() {

    lateinit var classesArray: MutableList<MyClass>
    lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classes_in_subject)

        toolbar = findViewById(R.id.classToolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = intent.getStringExtra("subjectName")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        //Получаем все class'ы
        classesArray = DBHelper(this).getClasses()
        classesArray.clear()
        val tempArray = DBHelper(this).getClasses()
        for(mClass in tempArray){
            if(mClass.ID == intent.getIntExtra("subjectID", -1))
                classesArray.add(mClass)
        }

        val classesList = findViewById(R.id.classesList) as ListView
        classesList.adapter = ClassListAdapter(this, R.layout.class_list_element, classesArray)
        classesList.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, itemClicked: View, position: Int, id: Long) {
                val intentToClassAdd = Intent(this@ClassesInSubjectActivity, AddNewClassActivity::class.java)
                intentToClassAdd.putExtra("Create or change", "change")
                intentToClassAdd.putExtra("subjectID", intent.getIntExtra("subjectID", -1))
                intentToClassAdd.putExtra("classID", classesArray[position].ID)
                startActivity(intentToClassAdd)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        classesArray.clear()
        val tempArray = DBHelper(this).getClasses()
        for(mClass in tempArray){
            if(mClass.subjectID == intent.getIntExtra("subjectID", -1))
                classesArray.add(mClass)
        }
        classesList.adapter = ClassListAdapter(this, R.layout.class_list_element, classesArray)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_classes_in_subject, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.changeSubject -> {
                //Открываем addNewSubjectActivity, чтобы настроить/изменить его
                val intentToSubjAdd = Intent(this, AddNewSubjectActivity::class.java)
                intentToSubjAdd.putExtra("Create or change", "change")
                intentToSubjAdd.putExtra("subjectID", intent.getIntExtra("subjectID", -1))
                intentToSubjAdd.putExtra("subjectName", intent.getStringExtra("subjectName"))
                intentToSubjAdd.putExtra("color", intent.getIntExtra("color", -1))
                intentToSubjAdd.putExtra("termID", intent.getIntExtra("termID", -1))
                startActivity(intentToSubjAdd)
            }
            R.id.deleteSubject->{
                //Удаляем term из БД и возвращаемся в планер
                DBHelper(this).deleteSubjectsById(intent.getIntExtra("subjectID", -1))
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
        val intentToSubAdd = Intent(this, AddNewClassActivity::class.java)
        intentToSubAdd.putExtra("Create or change", "create")
        intentToSubAdd.putExtra("subjectID", intent.getIntExtra("subjectID", -1))
        intentToSubAdd.putExtra("subjectName", intent.getStringExtra("subjectName"))
        startActivity(intentToSubAdd)
    }
}
