package com.example.androidcoursework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.thebluealliance.spectrum.SpectrumPalette

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
            Log.d("aaa", "$color")
        }
        palette.setSelectedColor(resources.getColor(R.color.white))
        color = resources.getColor(R.color.white)

        editText = findViewById(R.id.newSubjectName)

        toolbar = findViewById<Toolbar>(R.id.addSubjToolBar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "New subject"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add -> {
                //Сохраняем введенные данные в базу данных
                Log.d("aaa", "$color ${editText.text}")
                //db.put(color)
                //db.put(editText.text)

                //Возвращаемся к fragment_term
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
