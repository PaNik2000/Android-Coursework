package com.example.androidcoursework

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_subjects_in_term.*

class SubjectListAdapter(context : Context, val resource: Int, objects: MutableList<Subject>)
    : ArrayAdapter<Subject>(context, resource, objects){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val subject = getItem(position)
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null)
        }
        //Очень красивая строчка <3
        (view?.findViewById(R.id.subjColor) as View).background = getDrawable(context, colorResources[idsOfColors.indexOf(subject?.color)])
        (view?.findViewById(R.id.subjName) as TextView).text = subject?.name

        return view
    }

}

class SubjectsInTermActivity : AppCompatActivity() {

    lateinit var subjArray: MutableList<Subject>
    lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subjects_in_term)

        toolbar = findViewById<Toolbar>(R.id.subjToolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = intent.getStringExtra("termName")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        val subjectsList = findViewById(R.id.subjectsList) as ListView

        //Получаем все subject'ы запросом по termID
        subjArray = DBHelper(this@SubjectsInTermActivity).getSubjects()
        subjArray.clear()
        val tempArray = DBHelper(this@SubjectsInTermActivity).getSubjects()
        for (subj in tempArray){
            if(subj.termID == intent.getIntExtra("termID", -1))
                subjArray.add(subj)
        }

        subjectsList.adapter = SubjectListAdapter(this, R.layout.subject_list_element, subjArray)
        subjectsList.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, itemClicked: View, position: Int, id: Long) {
                val intentToClass = Intent(this@SubjectsInTermActivity, ClassesInSubjectActivity::class.java)
                val subjectID = subjArray[position].ID
                intentToClass.putExtra("subjectID", subjectID)
                intentToClass.putExtra("subjectName", subjArray[position].name)
                intentToClass.putExtra("color", subjArray[position].color)
                intentToClass.putExtra("termID", intent.getIntExtra("termID", -1))
                startActivity(intentToClass)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        subjArray.clear()
        val tempArray = DBHelper(this@SubjectsInTermActivity).getSubjects()
        for (subj in tempArray){
            if(subj.termID == intent.getIntExtra("termID", -1))
                subjArray.add(subj)
        }
        subjectsList.adapter = SubjectListAdapter(this, R.layout.subject_list_element, subjArray)
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
                intentToAddNeTermAactivity.putExtra("termID", intent.getIntExtra("termID", -1))
                intentToAddNeTermAactivity.putExtra("termName", intent.getStringExtra("termName"))
                startActivity(intentToAddNeTermAactivity)
            }
            R.id.deleteTerm->{
                DBHelper(this).deleteTermByID(intent.getIntExtra("termID", -1))
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
        intentToSubjAdd.putExtra("termID", intent.getIntExtra("termID", -1))
        startActivity(intentToSubjAdd)
    }
}
