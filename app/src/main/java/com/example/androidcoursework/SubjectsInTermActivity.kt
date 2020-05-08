package com.example.androidcoursework

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar

class SubjectInfo(val color : Drawable, val name : String)

class SubjectListAdapter(context : Context, val resource: Int, objects: MutableList<SubjectInfo>)
    : ArrayAdapter<SubjectInfo>(context, resource, objects){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val subject = getItem(position)
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null)
        }

        //НЕ ЗАБВАЕМ ПРО 3 ЧАСА D:
        (view?.findViewById(R.id.subjColor) as View).background = subject?.color
        (view?.findViewById(R.id.subjName) as TextView).text = subject?.name

        return view
    }

}

class SubjectsInTermActivity : AppCompatActivity() {

    private val subjArray = ArrayList<SubjectInfo>()
    // Кастомный тулбар наверху
    lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subjects_in_term)

        //Получаем ID term'a - val termID = intent.getIntExtra("termID")
        //Получаем name term'a - val termName = intent.getStringExtra("termName")

        toolbar = findViewById<Toolbar>(R.id.subjToolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "NAME_OF_TERM" // = termName
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        val subjectsList = findViewById(R.id.subjectsList) as ListView

        //Получаем все subject'ы запросом по termID
        //while(coursor.hasNext()) { subjArray<Subject>.add(coursor.next()) }

        //Потом удаляем
        val drawable4 = resources.getDrawable(R.drawable.light_orange)
        subjArray.add(SubjectInfo(drawable4, "МБП"))
        ////////////////

        subjectsList.adapter = SubjectListAdapter(this, R.layout.subject_list_element, subjArray)
        subjectsList.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, itemClicked: View, position: Int, id: Long) {
                //Магическим образом достаем ID и имя subject'a из БД
                val intentToClass = Intent(this@SubjectsInTermActivity, ClassesInSubjectActivity::class.java)
                //intentToClass.putExtra("subjectID", subjectID)
                //intentToClass.putExtra("subjectName", subjectName)
                startActivity(intentToClass)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_subjects_in_term, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.changeTerm -> {
                //Открываем addNewTermActivity, чтобы настроить/изменить его
                val intentToAddNeTermAactivity = Intent(this, AddNewTermActivity::class.java)
                intentToAddNeTermAactivity.putExtra("Create or change", "change")
                //intentToAddNeTermAactivity.putExtra("termName", termName)
                //intentToAddNeTermAactivity.putExtra("termID", termID)
                startActivity(intentToAddNeTermAactivity)
            }
            R.id.deleteTerm->{
                //Удаляем term из БД и возвращаемся в планер
                //db.delete(termID)
                finish()
            }
            android.R.id.home ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onAddSubjectButtonClick(view: View){
        //Переходим к добавлению нового subject'a
        val intentToSubjAdd = Intent(this, AddNewSubjectActivity::class.java)
        intentToSubjAdd.putExtra("Create or change", "create")
        //intentToSubjAdd.putExtra("termID", intent.getIntExtra("termID"))
        startActivity(intentToSubjAdd)
    }
}
