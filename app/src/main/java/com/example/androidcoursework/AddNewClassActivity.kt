package com.example.androidcoursework

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_add_new_class.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddNewClassActivity : AppCompatActivity() {

    lateinit var toolbar : Toolbar
    //Заполнить из БД
    lateinit var sortedSchedules: MutableList<Schedule>
    lateinit var schedulePositions: ArrayList<String>
    lateinit var teachers: MutableList<Teacher>
    lateinit var teacherNames: ArrayList<String>

    lateinit var typeClass: EditText
    lateinit var mStartDate : TextView
    lateinit var mEndDate : TextView
    lateinit var scheduleSpinner: Spinner
    lateinit var frequencySpinner: Spinner
    lateinit var repeatSpinner: Spinner
    lateinit var teacherSpinner: Spinner

    var createNewClass = true
    var weekSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_class)

        val dbHelper = DBHelper(this)

        if(intent.getStringExtra("Create or change") == "change")
            createNewClass = false

        mStartDate = findViewById<TextView>(R.id.editStart)
        mEndDate = findViewById<TextView>(R.id.editEnd)
        typeClass = findViewById<EditText>(R.id.editTypeClass)
        scheduleSpinner = findViewById<Spinner>(R.id.editPositionClass)
        frequencySpinner = findViewById<Spinner>(R.id.editFreqType)
        repeatSpinner = findViewById<Spinner>(R.id.editRepeatType)
        teacherSpinner = findViewById<Spinner>(R.id.editTeacher)

        toolbar = findViewById<Toolbar>(R.id.addClassToolBar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        //Заполнение спиннера пар
        sortedSchedules = sortSchedules(dbHelper.getSchedule())
        schedulePositions = ArrayList<String>()
        for(schedule in sortedSchedules){
            schedulePositions.add("${schedule.position} пара")
        }
        var aa = ArrayAdapter(this, R.layout.my_simple_spinner_item, schedulePositions)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(editPositionClass){
            adapter = aa
            prompt = "Выберете номер пары"
            gravity = Gravity.CENTER
        }
        //Заполнение спиннера учителей
        teachers = dbHelper.getTeachers()
        teacherNames = ArrayList<String>()
        for(teacher in teachers)
            teacherNames.add(teacher.name)
        aa = ArrayAdapter(this, R.layout.my_simple_spinner_item, teacherNames)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(editTeacher){
            adapter = aa
            prompt = "Выберете преподавателя"
            gravity = Gravity.CENTER
        }
        //Доступность выбора дня в зависимости от выбранного режима повторения
        repeatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                val dayPickerLayout = findViewById<LinearLayout>(R.id.daypicker_layout)

                if (selectedItem == "Day") {
                    dayPickerLayout.visibility = View.GONE
                    weekSelected = false
                }
                else if (selectedItem == "Week") {
                    dayPickerLayout.visibility = View.VISIBLE
                    weekSelected = true
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        if(createNewClass){
            supportActionBar?.title = "Новое занятие"
            mStartDate.text = DBHelper(this).getTermByID(DBHelper(this).getSubjectsById(intent.getIntExtra("subjectID", -1))!!.termID)!!.startDate
            mEndDate.text = DBHelper(this).getTermByID(DBHelper(this).getSubjectsById(intent.getIntExtra("subjectID", -1))!!.termID)!!.endDate
        } else {
            supportActionBar?.title = dbHelper.getSubjectsById(intent.getIntExtra("subjectID", -1))?.name
            typeClass.setText(dbHelper.getClassesByID(intent.getIntExtra("classID", -1))?.type, TextView.BufferType.EDITABLE)
            scheduleSpinner.setSelection(schedulePositions.indexOf("${dbHelper.getScheduleById(dbHelper.getClassesByID(intent.getIntExtra("classID", -1))!!.scheduleID)?.position} пара"))
            mStartDate.text = dbHelper.getClassesByID(intent.getIntExtra("classID", -1))!!.startDate
            mEndDate.text = dbHelper.getClassesByID(intent.getIntExtra("classID", -1))!!.endDate
            frequencySpinner.setSelection(dbHelper.getClassesByID(intent.getIntExtra("classID", -1))!!.repeatFreq - 1)
            repeatSpinner.setSelection(dbHelper.getClassesByID(intent.getIntExtra("classID", -1))!!.repeatType)
            teacherSpinner.setSelection(teacherNames.indexOf(dbHelper.getTeachersById(dbHelper.getClassesByID(intent.getIntExtra("classID", -1))!!.teacherID)?.name))

            var weekDay = dbHelper.getClassesByID(intent.getIntExtra("classID", -1))!!.weekDay
            if(weekDay/1000000 == 1) findViewById<ToggleButton>(R.id.monday).isChecked = true
            weekDay%=1000000
            if(weekDay/100000 == 1) findViewById<ToggleButton>(R.id.tuesday).isChecked = true
            weekDay%=100000
            if(weekDay/10000 == 1) findViewById<ToggleButton>(R.id.wednesday).isChecked = true
            weekDay%=10000
            if(weekDay/1000 == 1) findViewById<ToggleButton>(R.id.thursday).isChecked = true
            weekDay%=1000
            if(weekDay/100 == 1) findViewById<ToggleButton>(R.id.friday).isChecked = true
            weekDay%=100
            if(weekDay/10 == 1) findViewById<ToggleButton>(R.id.saturday).isChecked = true
            weekDay%=10
            if(weekDay/1 == 1) findViewById<ToggleButton>(R.id.sunday).isChecked = true
        }
        dbHelper.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(createNewClass) {
            getMenuInflater().inflate(R.menu.menu_add, menu)
        } else {
            getMenuInflater().inflate(R.menu.menu_add_and_delete, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add -> {
                var weekDay = 0
                if(weekSelected){//Если дни недели не показываются, то никакие дни не выбраны
                    if (findViewById<ToggleButton>(R.id.monday).isChecked) weekDay = weekDay * 10 + 1
                    else weekDay = weekDay * 10
                    if (findViewById<ToggleButton>(R.id.tuesday).isChecked) weekDay = weekDay * 10 + 1
                    else weekDay = weekDay * 10
                    if (findViewById<ToggleButton>(R.id.wednesday).isChecked) weekDay = weekDay * 10 + 1
                    else weekDay = weekDay * 10
                    if (findViewById<ToggleButton>(R.id.thursday).isChecked) weekDay = weekDay * 10 + 1
                    else weekDay = weekDay * 10
                    if (findViewById<ToggleButton>(R.id.friday).isChecked) weekDay = weekDay * 10 + 1
                    else weekDay = weekDay * 10
                    if (findViewById<ToggleButton>(R.id.saturday).isChecked) weekDay = weekDay * 10 + 1
                    else weekDay = weekDay * 10
                    if (findViewById<ToggleButton>(R.id.sunday).isChecked) weekDay = weekDay * 10 + 1
                    else weekDay = weekDay * 10
                }

                if(createNewClass) {
                    if (typeClass.text.isEmpty()) {
                        Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show()
                    } else if (weekDay == 0 && weekSelected) {
                        Toast.makeText(this, "Выберете номер пары!", Toast.LENGTH_SHORT).show()
                    } else {
                        DBHelper(this).insertClasses(intent.getIntExtra("subjectID", -1),
                            typeClass.text.toString(),
                            if(!sortedSchedules.isEmpty()) sortedSchedules.elementAt(scheduleSpinner.selectedItemPosition).ID!! else null,
                            SimpleDateFormat("dd.MM.yyyy").parse(mStartDate.text.toString()),
                            SimpleDateFormat("dd.MM.yyyy").parse(mEndDate.text.toString()),
                            weekDay,
                            if ((repeatSpinner.selectedItemPosition == 0)) RepeatTypes.DAY.TYPE else RepeatTypes.WEEK.TYPE,
                            frequencySpinner.selectedItemPosition + 1,
                            if(!teachers.isEmpty()) teachers.elementAt(teacherSpinner.selectedItemPosition).ID else null)
                        finish()
                    }
                } else {
                    if(typeClass.text.isEmpty())
                        Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                    if(weekDay == 0 && weekSelected){
                        Toast.makeText(this, "CВыберете номер пары!", Toast.LENGTH_SHORT).show()
                    } else {
                        DBHelper(this).updateClasses(
                            intent.getIntExtra("classID", -1),
                            intent.getIntExtra("subjectID", -1),
                            typeClass.text.toString(),
                            if(!sortedSchedules.isEmpty()) sortedSchedules.elementAt(scheduleSpinner.selectedItemPosition).ID!! else null,
                            SimpleDateFormat("dd.MM.yyyy").parse(mStartDate.text.toString()),
                            SimpleDateFormat("dd.MM.yyyy").parse(mEndDate.text.toString()),
                            weekDay,
                            if ((repeatSpinner.selectedItemPosition == 0)) RepeatTypes.DAY.TYPE else RepeatTypes.WEEK.TYPE,
                            frequencySpinner.selectedItemPosition + 1,
                            if(!teachers.isEmpty()) teachers.elementAt(teacherSpinner.selectedItemPosition).ID else null
                        )
                        finish()
                    }
                }
            }
            R.id.delete->{
                DBHelper(this).deleteClassesByID(intent.getIntExtra("classID", -1))
                finish()
            }
            android.R.id.home ->{
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun onEditStartClick(view: View){
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
            if(SimpleDateFormat("dd.MM.yyy").parse(mStartDate.text.toString()) >= SimpleDateFormat("dd.MM.yyy").parse(mEndDate.text.toString())) {
                cal.set(year, monthOfYear, dayOfMonth)
                val endDate: Date = cal.time
                mEndDate.text = SimpleDateFormat("dd.MM.yyyy").format(endDate)
            }
        }, curYear, curMonth, curDayOfMonth)
        val mMinDate = DBHelper(this).getTermByID(DBHelper(this).getSubjectsById(intent.getIntExtra("subjectID", -1))!!.termID)!!.startDate
        val mMaxDate = DBHelper(this).getTermByID(DBHelper(this).getSubjectsById(intent.getIntExtra("subjectID", -1))!!.termID)!!.endDate
        mDateDialog.datePicker.minDate = SimpleDateFormat("dd.MM.yyyy").parse(mMinDate).time
        mDateDialog.datePicker.maxDate = SimpleDateFormat("dd.MM.yyyy").parse(mMaxDate).time
        mDateDialog.show()
    }

    fun onEditEndClick(view: View){
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
        val mMaxDate = DBHelper(this).getTermByID(DBHelper(this).getSubjectsById(intent.getIntExtra("subjectID", -1))!!.termID)!!.endDate
        mDateDialog.datePicker.minDate = SimpleDateFormat("dd.MM.yyyy").parse(mStartDate.text.toString()).time
        mDateDialog.datePicker.maxDate = SimpleDateFormat("dd.MM.yyyy").parse(mMaxDate).time
        mDateDialog.show()
    }

    fun addMonthToDate(date: Date, num: Int) : Date{
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, num)
        return cal.time
    }

    fun sortSchedules(scheduleList: MutableList<Schedule>) : MutableList<Schedule>{
        val sortedSchedule = mutableListOf<Schedule>()

        for(i in 1..scheduleList.size){
            for(schedule in scheduleList){
                if(schedule.position == i){
                    sortedSchedule.add(schedule)
                    break
                }
            }
        }
        return sortedSchedule
    }
}
