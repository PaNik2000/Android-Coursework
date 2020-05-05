package com.example.androidcoursework

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_term)

        mStartDate = findViewById<TextView>(R.id.startDate)
        mEndDate = findViewById<TextView>(R.id.endDate)
        var curDate = Date()
        val dateFormat = SimpleDateFormat("dd.MM.yyy")
        mStartDate.text = dateFormat.format(curDate)
        curDate = addMonthToDate(curDate, 3)
        mEndDate.text = dateFormat.format(curDate)

        toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)
        toolbar.title = "Add new term"
        //Не уверен, что это сработает (делает кнопку назад видимой)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
//        toolbar.navigationIcon = //Иконка Конпки назад

    }

    //Показываем содержимое меню на тулбаре (показываем кнопку "ADD")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_add_term, menu)
        return true
    }

    //При нажатии на кнопку "ADD"
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add -> {
                //Сохраняем введенные данные в базу данных
                val sDate = mStartDate.text
                val eDate = mEndDate.text
                //db.put(sDate)
                //db.put(eDate)

                //Возвращаемся к fragment_term
                Toast.makeText(this, "Why we still here?", Toast.LENGTH_SHORT).show()
                finish()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    //Нажатие на startDate
    fun onStartDateClick(view : View){
        val cal = Calendar.getInstance()
        val curYear = cal.get(Calendar.YEAR)
        val curMonth = cal.get(Calendar.MONTH)
        val curDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val mDateDialog= DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in startDate
            mStartDate.text = "$dayOfMonth.${monthOfYear+1}.$year"
        }, curYear, curMonth, curDayOfMonth)
        mDateDialog.show()
    }

    //Нажатие на endDate
    fun onEndDateClick(view: View){
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, 3)
        val curYear = cal.get(Calendar.YEAR)
        val curMonth = cal.get(Calendar.MONTH)
        val curDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val mDateDialog= DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            mEndDate.text = "$dayOfMonth.${monthOfYear+1}.$year"
        }, curYear, curMonth, curDayOfMonth)
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