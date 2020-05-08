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
import com.thebluealliance.spectrum.SpectrumPalette
import kotlinx.android.synthetic.main.activity_add_new_term.*
import javax.security.auth.Subject

class AddNewSubjectActivity : AppCompatActivity() {

    lateinit var toolbar : Toolbar
    lateinit var editText : EditText
    var color = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_subject)

        val palette = findViewById(R.id.palette) as SpectrumPalette
        //Ничего непонятно, но очень интересно
        palette.setOnColorSelectedListener {
            clr -> color = clr
            Log.d("aaa", "${color}")
        }
        palette.setSelectedColor(resources.getColor(R.color.red))
        color = resources.getColor(R.color.red)

        editText = findViewById(R.id.newSubjectName)

        toolbar = findViewById<Toolbar>(R.id.addSubjToolBar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        if(intent.getStringExtra("Create or change") == "create"){
            supportActionBar?.title = "New subject"
        } else if(intent.getStringExtra("Create or change") == "change"){
            supportActionBar?.title = "Subject"
            //Получаем ID subjecta'a - val subjectID = intent.getIntExtra("subjectID")
            //Получаем name subject'a - val subjectName = intent.getStringExtra("subjectName")
            val textView = findViewById(R.id.newSubjectName) as TextView
            textView.text = "SUBJ_NAME" // = subjectName
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
                    Toast.makeText(this, "Fill in the gaps!", Toast.LENGTH_SHORT).show()
                } else {
                    //Сохраняем введенные данные в базу данных
                    //ЕСЛИ ДОБАВЛЯЕМ НОВЫЙ
                        //По-видимому делаем ID = null, тк он autoincrement
                        val newSubj = Subject(null, findViewById<EditText>(R.id.termName).text.toString(), colorResources[idsOfColors.indexOf(color)], /*intent.getIntExtra("termID")*/11)
                        //db.insert(newTerm)

                        //Возвращаемся к fragment_term
                        Toast.makeText(this, "Why we still here?", Toast.LENGTH_SHORT).show()
                    //ИНАЧЕ
                    ////db.update(subjectID, findViewById<EditText>(R.id.newSubjectName).text.toString(), colorResources[idsOfColors.indexOf(color)], intent.getIntExtra("termID"))
                }

                //Возвращаемся к subjectsInTermActivity
                Toast.makeText(this, "Еще одна пара по МБП...", Toast.LENGTH_SHORT).show()
                finish()
            }
            android.R.id.home ->{
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
