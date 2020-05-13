package com.example.androidcoursework

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.text.SimpleDateFormat
import java.util.*


class AddNewTermActivity : AppCompatActivity() {
    // Кастомный тулбар наверху
    lateinit var toolbar : Toolbar
    // Поле выбора начала семестра
    lateinit var mStartDate : TextView
    // Поле выбора начала семестра
    lateinit var mEndDate : TextView

    var createNewTerm = true

    val db = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_term)

        if(intent.getStringExtra("Create or change") == "change")
            createNewTerm = false

        mStartDate = findViewById<TextView>(R.id.startDate)
        mEndDate = findViewById<TextView>(R.id.endDate)

        toolbar = findViewById<Toolbar>(R.id.termToolBar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        if(createNewTerm) {
            var curDate = Date()
            val dateFormat = SimpleDateFormat("dd.MM.yyyy")
            mStartDate.text = dateFormat.format(curDate)
            curDate = addMonthToDate(curDate, 3)
            mEndDate.text = dateFormat.format(curDate)
            supportActionBar?.title = "Новый семестр"
        } else {
            supportActionBar?.title = "Семестр"
            val textView = findViewById(R.id.termName) as TextView;
            textView.text = intent.getStringExtra("termName")
            mStartDate.text = db.getTermByID(intent.getIntExtra("termID", -1))?.startDate
            mEndDate.text = db.getTermByID(intent.getIntExtra("termID", -1))?.endDate
        }

    }

    //Показываем содержимое меню на тулбаре (показываем кнопку "ADD")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_add, menu)
        return true
    }

    //При нажатии на кнопку "ADD"
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add -> {
                if(findViewById<EditText>(R.id.termName).text.isEmpty()){
                    Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show()
                } else if(createNewTerm){
                    val startDate = SimpleDateFormat("dd.MM.yyyy").parse(mStartDate.text.toString())
                    val endDate = SimpleDateFormat("dd.MM.yyyy").parse(mEndDate.text.toString())
                    db.insertTerm(findViewById<EditText>(R.id.termName).text.toString(), startDate, endDate)
                    finish()
                } else {
                    val startDate = SimpleDateFormat("dd.MM.yyyy").parse(mStartDate.text.toString())
                    val endDate = SimpleDateFormat("dd.MM.yyyy").parse(mEndDate.text.toString())
                    db.updateTerm(intent.getIntExtra("termID", -1), findViewById<EditText>(R.id.termName).text.toString(), startDate, endDate)
                    finish()
                }
            }
            android.R.id.home ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Нажатие на startDate
    fun onStartDateClick(view : View){
        val cal = Calendar.getInstance()
        cal.time = SimpleDateFormat("dd.MM.yyyy").parse(mStartDate.text.toString())
        val curYear = cal.get(Calendar.YEAR)
        val curMonth = cal.get(Calendar.MONTH)
        val curDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val mDateDialog= DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in startDate
            val day = if(9 > dayOfMonth) "0${dayOfMonth}" else "${dayOfMonth}"
            val month = if(9 > (monthOfYear+1)) "0${monthOfYear + 1}" else "${monthOfYear+1}"
            mStartDate.text = "$day.${month}.$year"
            cal.set(year, monthOfYear+3, dayOfMonth)
            val endDate: Date = cal.time
            mEndDate.text = SimpleDateFormat("dd.MM.yyyy").format(endDate)
        }, curYear, curMonth, curDayOfMonth)
        mDateDialog.show()
    }

    //Нажатие на endDate
    fun onEndDateClick(view: View){
        val cal = Calendar.getInstance()
        cal.time = SimpleDateFormat("dd.MM.yyyy").parse(mEndDate.text.toString())
        val curYear = cal.get(Calendar.YEAR)
        val curMonth = cal.get(Calendar.MONTH)
        val curDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val mDateDialog= DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val day = if(9 > dayOfMonth) "0${dayOfMonth}" else "${dayOfMonth}"
            val month = if(9 > (monthOfYear+1)) "0${monthOfYear + 1}" else "${monthOfYear+1}"
            mEndDate.text = "$day.${month}.$year"
        }, curYear, curMonth, curDayOfMonth)
        mDateDialog.datePicker.minDate = SimpleDateFormat("dd.MM.yyyy").parse(mStartDate.text.toString()).time
        mDateDialog.show()
    }

    //Добавить месяцы к текущей дате
    fun addMonthToDate(date: Date, num: Int) : Date{
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, num)
        return cal.time
    }

}