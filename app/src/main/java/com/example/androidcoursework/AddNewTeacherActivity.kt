package com.example.androidcoursework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

class AddNewTeacherActivity : AppCompatActivity() {

    lateinit var toolbar : Toolbar
    lateinit var teacherName: EditText

    var createNewTeacher = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_teacher)

        if(intent.getStringExtra("Create or change") == "change")
            createNewTeacher = false

        toolbar = findViewById<Toolbar>(R.id.teacherToolBar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        teacherName = findViewById(R.id.teacherNameText)

        if(createNewTeacher){
            supportActionBar?.title = "New teacher"
        } else {
            supportActionBar?.title = "Teacher"
            teacherName.setText(DBHelper(this).getTeachersById(intent.getIntExtra("teacherID", -1))?.name, TextView.BufferType.EDITABLE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(createNewTeacher)
            getMenuInflater().inflate(R.menu.menu_add, menu)
        else
            getMenuInflater().inflate(R.menu.menu_add_and_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add -> {
                if(teacherName.text.isEmpty()){
                    Toast.makeText(this, "Fill in the gaps!", Toast.LENGTH_SHORT).show()
                }
                else if(createNewTeacher){
                    DBHelper(this).insertTeachers(teacherName.text.toString())
                    Toast.makeText(this, "Хачу питсу", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    DBHelper(this).updateTeachers(intent.getIntExtra("teacherID", -1), teacherName.text.toString())
                    Toast.makeText(this, "Хачу питсу", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            R.id.delete->{
                DBHelper(this).deleteTeachersById(intent.getIntExtra("teacherID", -1))
                finish()
            }
            android.R.id.home ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
