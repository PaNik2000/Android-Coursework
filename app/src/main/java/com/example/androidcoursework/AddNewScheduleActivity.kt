package com.example.androidcoursework

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

class AddNewScheduleActivity : AppCompatActivity() {

    lateinit var toolbar : Toolbar
    lateinit var mStartDate : TextView
    lateinit var mEndDate : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_schedule)

        mStartDate = findViewById<TextView>(R.id.startScheduleTime)
        mEndDate = findViewById<TextView>(R.id.endScheduleTime)
        var curDate = Date()
        val dateFormat = SimpleDateFormat("hh:mm")
        mStartDate.text = dateFormat.format(curDate)
        mEndDate.text = dateFormat.format(curDate)

        toolbar = findViewById<Toolbar>(R.id.addScheduleToolBar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        if(intent.getStringExtra("Create or change") == "create"){
            supportActionBar?.title = "New schedule"
        } else if(intent.getStringExtra("Create or change") == "change"){
            supportActionBar?.title = "Schedule"
            //ToDo
            //Поставить время из существующего расписания
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_add, menu)
        return true
    }

    //При нажатии на кнопку "ADD"
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add -> {
                //Сохраняем введенные данные в базу данных

                //Возвращаемся к fragment_term
                Toast.makeText(this, "Why we still here?", Toast.LENGTH_SHORT).show()
                finish()
            }
            android.R.id.home ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onStartScheduleTimeClick(view: View){
        val cal = Calendar.getInstance()
        val curHour = cal.get(Calendar.HOUR_OF_DAY)
        val curMin = cal.get(Calendar.MINUTE)
        val mTimeDialog= TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            if(9 < minute)
                mStartDate.text = "$hour:${minute}"
            else{
                mStartDate.text = "$hour:0${minute}"
            }
        }, curHour, curMin, true)
        mTimeDialog.show()
    }

    fun onEndScheduleTimeClick(view: View){
        val cal = Calendar.getInstance()
        val curHour = cal.get(Calendar.HOUR_OF_DAY)
        val curMin = cal.get(Calendar.MINUTE)
        val mTimeDialog= TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            if(9 < minute)
                mEndDate.text = "$hour:${minute}"
            else{
                mEndDate.text = "$hour:0${minute}"
            }
        }, curHour, curMin, true)
        mTimeDialog.show()
    }
}
