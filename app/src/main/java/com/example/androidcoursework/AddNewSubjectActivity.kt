package com.example.androidcoursework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.thebluealliance.spectrum.SpectrumPalette

class AddNewSubjectActivity : AppCompatActivity() {

    lateinit var toolbar : Toolbar
    lateinit var editText : EditText
    var createNewSubject = true
    var color = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_subject)

        if(intent.getStringExtra("Create or change") == "change")
            createNewSubject = false

        val palette = findViewById(R.id.palette) as SpectrumPalette
        palette.setOnColorSelectedListener {
            clr -> color = clr
        }

        editText = findViewById(R.id.newSubjectName)

        toolbar = findViewById<Toolbar>(R.id.addSubjToolBar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        if(createNewSubject){
            supportActionBar?.title = "Новая дисциплина"
            palette.setSelectedColor(resources.getColor(R.color.red))
            color = resources.getColor(R.color.red)
        } else {
            supportActionBar?.title = "Дисциплина"
            val textView = findViewById(R.id.newSubjectName) as TextView
            textView.text = intent.getStringExtra("subjectName")
            val colorsFromRes = resources.getIntArray(R.array.demo_colors)
            color = intent.getIntExtra("color", -1)
            palette.setSelectedColor(colorsFromRes[idsOfColors.indexOf(color)])
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add -> {
                if(findViewById<EditText>(R.id.newSubjectName).text.isEmpty()){
                    Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show()
                } else {
                    //Сохраняем введенные данные в базу данных
                    if(createNewSubject) {
                        DBHelper(this).insertSubjects(findViewById<EditText>(R.id.newSubjectName).text.toString(),
                                                              color,
                                                              intent.getIntExtra("termID", -1))
                    } else {
                        DBHelper(this).updateSubjects(intent.getIntExtra("subjectID", -1),
                                                              findViewById<EditText>(R.id.newSubjectName).text.toString(),
                                                              color,
                                                              intent.getIntExtra("termID", -1))
                    }
                    //Возвращаемся к subjectsInTermActivity
                    finish()
                }

            }
            android.R.id.home ->{
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
