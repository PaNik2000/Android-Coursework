package com.example.androidcoursework

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
import java.util.*

class AddNewScheduleActivity : AppCompatActivity() {

    lateinit var scheduleList : MutableList<Schedule>
    lateinit var sortedScheduleList : MutableList<Schedule>

    lateinit var toolbar : Toolbar
    lateinit var mStartDate : TextView
    lateinit var mEndDate : TextView

    var createNewScedule = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_schedule)

        if(intent.getStringExtra("Create or change") == "change")
            createNewScedule = false

        scheduleList = DBHelper(this).getSchedule()
        sortedScheduleList = sortSchedules(scheduleList)

        toolbar = findViewById<Toolbar>(R.id.addScheduleToolBar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)


        mStartDate = findViewById<TextView>(R.id.startScheduleTime)
        mEndDate = findViewById<TextView>(R.id.endScheduleTime)

        if(createNewScedule){
            supportActionBar?.title = "Новая пара"
            var curDate = Date()
            val dateFormat = SimpleDateFormat("HH:mm")
            mStartDate.text = dateFormat.format(curDate)
            mEndDate.text = dateFormat.format(curDate)
        } else {
            supportActionBar?.title = "Пара"
            mStartDate.text = DBHelper(this).getScheduleById(intent.getIntExtra("scheduleID", -1))?.startTime
            mEndDate.text = DBHelper(this).getScheduleById(intent.getIntExtra("scheduleID", -1))?.endTime
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(createNewScedule)
            getMenuInflater().inflate(R.menu.menu_add, menu)
        else
            getMenuInflater().inflate(R.menu.menu_add_and_delete, menu)
        return true
    }

    //При нажатии на кнопку "ADD"
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add -> {
                //Проверка на попадание начального времени в уже существующую пару
                if(!checkStartDate()){
                    Toast.makeText(this, "Выранная начальная дата пересекается с существующим расписанием", Toast.LENGTH_SHORT).show()
                }
                //Проверка на попадание конечного времени в уже существующую пару
                else if (!checkEndDate()){
                    Toast.makeText(this, "Выранная конечная дата пересекается с существующим расписанием", Toast.LENGTH_SHORT).show()
                }
                else if(createNewScedule){
                    //Расстановка новых позиций
                    val newSchedulePosition = getNewPosition()
                    DBHelper(this).insertSchedule(newSchedulePosition,
                        SimpleDateFormat("HH:mm").parse(mStartDate.text.toString()),
                        SimpleDateFormat("HH:mm").parse(mEndDate.text.toString()))
                    updateAllSchedules(newSchedulePosition, deleteSchedule = false)
                    finish()

                } else {
                    //Расстановка новых позиций
                    val newSchedulePosition = getNewPosition()
                    updateAllSchedules(newSchedulePosition, deleteSchedule = false)
                    finish()
                }
            }
            R.id.delete->{
                val deleteSchedulePosition = DBHelper(this).getScheduleById(intent.getIntExtra("scheduleID", -1))!!.position
                DBHelper(this).deleteScheduleById(intent.getIntExtra("scheduleID", -1))
                updateAllSchedules(deleteSchedulePosition, deleteSchedule = true)
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
        cal.time = SimpleDateFormat("HH:mm").parse(mStartDate.text.toString())
        val curHour = cal.get(Calendar.HOUR_OF_DAY)
        val curMin = cal.get(Calendar.MINUTE)
        val mTimeDialog= TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            if(9 <= minute)
                mStartDate.text = "$hour:${minute}"
            else{
                mStartDate.text = "$hour:0${minute}"
            }
            if(SimpleDateFormat("HH:mm").parse(mStartDate.text.toString()) >= SimpleDateFormat("HH:mm").parse(mEndDate.text.toString())){
                cal.time = SimpleDateFormat("HH:mm").parse(mStartDate.text.toString())
                cal.add(Calendar.MINUTE, 1)
                mEndDate.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
        }, curHour, curMin, true)
        mTimeDialog.show()
    }

    fun onEndScheduleTimeClick(view: View){
        val cal = Calendar.getInstance()
        cal.time = SimpleDateFormat("HH:mm").parse(mEndDate.text.toString())
        val curHour = cal.get(Calendar.HOUR_OF_DAY)
        val curMin = cal.get(Calendar.MINUTE)
        val mTimeDialog= TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            if(9 <= minute)
                mEndDate.text = "$hour:${minute}"
            else{
                mEndDate.text = "$hour:0${minute}"
            }
        }, curHour, curMin, true)
        mTimeDialog.show()
    }

    fun sortSchedules(scheduleList: MutableList<Schedule>) : MutableList<Schedule>{
        val sortedScheduleList = mutableListOf<Schedule>()

        for(i in 1..scheduleList.size){
            for(schedule in scheduleList){
                if(schedule.position == i){
                    sortedScheduleList.add(schedule)
//                    break
                }
            }
        }
        return sortedScheduleList
    }

    fun checkStartDate() : Boolean{
        val timeFormat = SimpleDateFormat("HH:mm")
        for (schedule in sortedScheduleList){
            if(timeFormat.parse(schedule.startTime) <= timeFormat.parse(mStartDate.text.toString()) &&
                timeFormat.parse(schedule.endTime) >= timeFormat.parse(mStartDate.text.toString()) &&
                schedule.ID != intent.getIntExtra("scheduleID", -1)){
                return false
            }
        }
        return true
    }

    fun checkEndDate() : Boolean{
        val timeFormat = SimpleDateFormat("HH:mm")
        for (schedule in sortedScheduleList){
            if(timeFormat.parse(schedule.startTime) <= timeFormat.parse(mEndDate.text.toString()) &&
                timeFormat.parse(schedule.endTime) >= timeFormat.parse(mEndDate.text.toString()) &&
                schedule.ID != intent.getIntExtra("scheduleID", -1)){
                return false
            }
        }
        return true
    }

    fun getNewPosition() : Int {
        val timeFormat = SimpleDateFormat("HH:mm")
        for(schedule in sortedScheduleList){
            if(timeFormat.parse(mEndDate.text.toString()) < timeFormat.parse(schedule.startTime)){
                if(createNewScedule)
                    return schedule.position
                else{
                    if(schedule.ID == intent.getIntExtra("scheduleID", -1))
                        return DBHelper(this).getScheduleById(intent.getIntExtra("scheduleID", -1))!!.position
                    else
                        return return schedule.position - 1
                }

            }
        }
        //Дошли до конца списка, но ничего не нашли
        if(createNewScedule) {
            if (!sortedScheduleList.isEmpty())
                return sortedScheduleList.get(sortedScheduleList.size - 1).position + 1
            else return 1
        } else {
            return sortedScheduleList.get(sortedScheduleList.size - 1).position
        }
    }

    fun updateAllSchedules(schedulePosition: Int, deleteSchedule: Boolean) {
        //Если создали новый элемент расписания
        if(createNewScedule) {
            for (schedule in sortedScheduleList) {
                if (schedule.position >= schedulePosition) {
                    DBHelper(this).updateSchedule(
                        schedule.ID!!, schedule.position + 1,
                        SimpleDateFormat("HH:mm").parse(schedule.startTime),
                        SimpleDateFormat("HH:mm").parse(schedule.endTime)
                    )
                }
            }
        }
        //Удаляем
        else if(deleteSchedule){
            sortedScheduleList = DBHelper(this).getSchedule()
            for(schedule in sortedScheduleList){
                if(schedule.position >= schedulePosition){
                    DBHelper(this).updateSchedule(
                        schedule.ID!!,
                        schedule.position-1,
                        SimpleDateFormat("HH:mm").parse(schedule.startTime),
                        SimpleDateFormat("HH:mm").parse(schedule.endTime)
                    )
                }
            }
        }
        //Изменяем существующий на меньшую позицию и переносим его наверх
        else if (DBHelper(this).getScheduleById(intent.getIntExtra("scheduleID", -1))!!.position > schedulePosition){
            val range = schedulePosition..DBHelper(this).getScheduleById(intent.getIntExtra("scheduleID", -1))!!.position
            DBHelper(this).updateSchedule(intent.getIntExtra("scheduleID", -1), schedulePosition,
                SimpleDateFormat("HH:mm").parse(mStartDate.text.toString()), SimpleDateFormat("HH:mm").parse(mEndDate.text.toString()))
            for(schedule in sortedScheduleList){
                if(schedule.position in range && schedule.ID != DBHelper(this).getScheduleById(intent.getIntExtra("scheduleID", -1))!!.ID)
                    DBHelper(this).updateSchedule(schedule.ID!!, schedule.position+1,
                        SimpleDateFormat("HH:mm").parse(schedule.startTime), SimpleDateFormat("HH:mm").parse(schedule.endTime))
            }
        }
        //изменяем существущий на большую позицию и переносим его вниз
        else if(DBHelper(this).getScheduleById(intent.getIntExtra("scheduleID", -1))!!.position < schedulePosition){
            val range = DBHelper(this).getScheduleById(intent.getIntExtra("scheduleID", -1))!!.position..schedulePosition
            DBHelper(this).updateSchedule(intent.getIntExtra("scheduleID", -1), schedulePosition,
                SimpleDateFormat("HH:mm").parse(mStartDate.text.toString()), SimpleDateFormat("HH:mm").parse(mEndDate.text.toString()))
            for(schedule in sortedScheduleList){
                if(schedule.position in range && schedule.ID != DBHelper(this).getScheduleById(intent.getIntExtra("scheduleID", -1))!!.ID)
                    DBHelper(this).updateSchedule(schedule.ID!!, schedule.position-1,
                        SimpleDateFormat("HH:mm").parse(schedule.startTime), SimpleDateFormat("HH:mm").parse(schedule.endTime))
            }
        }
        //Изменяем только время
        else if(DBHelper(this).getScheduleById(intent.getIntExtra("scheduleID", -1))!!.position == schedulePosition){
            DBHelper(this).updateSchedule(intent.getIntExtra("scheduleID", -1), schedulePosition,
                SimpleDateFormat("HH:mm").parse(mStartDate.text.toString()), SimpleDateFormat("HH:mm").parse(mEndDate.text.toString()))
        }
    }
}
