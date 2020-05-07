package com.example.androidcoursework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

class AddNewTeacherActivity : AppCompatActivity() {

    lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_teacher)

        toolbar = findViewById<Toolbar>(R.id.teacherToolBar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        if(intent.getStringExtra("Create or change") == "create"){
            supportActionBar?.title = "New teacher"
        } else if(intent.getStringExtra("Create or change") == "change"){
            supportActionBar?.title = "Term" // Взять имя из БД
            val textView = findViewById(R.id.teacherNameText) as TextView;
            textView.text = "TEACHER_NAME"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add -> {
                //Сохраняем введенные данные в базу данных
                //db.put(sDate)
                //db.put(eDate)

                //Возвращаемся к fragment_teacher
                Toast.makeText(this, "Хачу питсу", Toast.LENGTH_SHORT).show()
                finish()
            }
            android.R.id.home ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
