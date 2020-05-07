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

        toolbar = findViewById<Toolbar>(R.id.subjToolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "NAME_OF_TERM" //Взять с БД название семестра
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        val subjectsList = findViewById(R.id.subjectsList) as ListView

        //НА ЭТО Я КСТА ПОТРАТИЛ 3 ЧАСА))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))
        val resource = resources
        val drawable1 = resource.getDrawable(R.drawable.light_blue)
        val drawable2 = resource.getDrawable(R.drawable.blue)
        val drawable3 = resource.getDrawable(R.drawable.yellow)
        val drawable4 = resource.getDrawable(R.drawable.light_yellow)

        //Тут нужно будет наполнить subjList предметами из БД
        subjArray.add(SubjectInfo(drawable1, "МБП"))
        subjArray.add(SubjectInfo(drawable2, "ФИЛОСОФИя"))
        subjArray.add(SubjectInfo(drawable3, "Системная и программная Жопа"))
        subjArray.add(SubjectInfo(drawable4, "Мне подойти? МММ?"))
        /////////////////////////////////////////////////////

        subjectsList.adapter = SubjectListAdapter(this, R.layout.subject_list_element, subjArray)
        subjectsList.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, itemClicked: View, position: Int, id: Long) {
                //TODO intent
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
                val intentToANTA = Intent(this, AddNewTermActivity::class.java)
                intentToANTA.putExtra("Create or change", "change")
                startActivity(intentToANTA)
            }
            R.id.deleteTerm->{
                //Удаляем term из БД и возвращаемся в планер
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onAddSubjectButtonClick(view: View){
        //Переходим к добавлению нового subject'a
        val intentToSubAdd = Intent(this, AddNewSubjectActivity::class.java)
        startActivity(intentToSubAdd)
    }
}
