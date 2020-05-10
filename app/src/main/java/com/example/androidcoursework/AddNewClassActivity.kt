package com.example.androidcoursework

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.Size
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.size
import kotlinx.android.synthetic.main.activity_add_new_class.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddNewClassActivity : AppCompatActivity() {

    lateinit var toolbar : Toolbar
    //Заполнить из БД
    var classNumberValues = arrayOf("1 пара", "2 пара", "3 пара", "4 пара", "5 пара")
    lateinit var teachers: MutableList<Teacher>
    lateinit var teacherNames: ArrayList<String>

    lateinit var typeClass: EditText
    lateinit var mStartDate : TextView
    lateinit var mEndDate : TextView
    lateinit var classNumberSpinner: Spinner
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
        classNumberSpinner = findViewById<Spinner>(R.id.editPositionClass)
        frequencySpinner = findViewById<Spinner>(R.id.editFreqType)
        repeatSpinner = findViewById<Spinner>(R.id.editRepeatType)
        teacherSpinner = findViewById<Spinner>(R.id.editTeacher)

        toolbar = findViewById<Toolbar>(R.id.addClassToolBar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        //Получить расписание пар по времени | getSchedules()
        var aa = ArrayAdapter(this, R.layout.my_simple_spinner_item, classNumberValues)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(editPositionClass){
            adapter = aa
            prompt = "Select number of the class"
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
            prompt = "Select number of the class"
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
            supportActionBar?.title = "New class"
            var curDate = Date()
            val dateFormat = SimpleDateFormat("dd.MM.yyy")
            mStartDate.text = dateFormat.format(curDate)
            curDate = addMonthToDate(curDate, 3)
            mEndDate.text = dateFormat.format(curDate)
        } else {
            supportActionBar?.title = dbHelper.getSubjectsById(intent.getIntExtra("subjectID", -1))?.name
            typeClass.setText(dbHelper.getClassesByID(intent.getIntExtra("classID", -1))?.type, TextView.BufferType.EDITABLE)
            classNumberSpinner.setSelection(dbHelper.getClassesByID(intent.getIntExtra("classID", -1))!!.scheduleID)
            mStartDate.text = dbHelper.getClassesByID(intent.getIntExtra("classID", -1))!!.startDate
            mEndDate.text = dbHelper.getClassesByID(intent.getIntExtra("classID", -1))!!.endDate
            frequencySpinner.setSelection(dbHelper.getClassesByID(intent.getIntExtra("classID", -1))!!.repeatFreq)
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
                        Toast.makeText(this, "Fill in the gaps!", Toast.LENGTH_SHORT).show()
                    } else if (weekDay == 0 && weekSelected) {
                        Toast.makeText(this, "Choose the day of a week!", Toast.LENGTH_SHORT).show()
                    } else {
                        DBHelper(this).insertClasses(intent.getIntExtra("subjectID", -1),
                            typeClass.text.toString(),
                            classNumberSpinner.selectedItemPosition /*Какой position нужен?*/,
                            SimpleDateFormat("dd.MM.yyyy").parse(mStartDate.text.toString()),
                            SimpleDateFormat("dd.MM.yyyy").parse(mEndDate.text.toString()),
                            weekDay,
                            if ((repeatSpinner.selectedItemPosition == 0)) RepeatTypes.DAY.TYPE else RepeatTypes.WEEK.TYPE,
                            frequencySpinner.selectedItemPosition,
                            teachers.elementAt(teacherSpinner.selectedItemPosition).ID)
                        finish()
                    }
                } else {
                    if(typeClass.text.isEmpty())
                        Toast.makeText(this, "Fill in the gaps!", Toast.LENGTH_SHORT).show()
                    if(weekDay == 0 && weekSelected){
                        Toast.makeText(this, "Choose the day of a week!", Toast.LENGTH_SHORT).show()
                    } else {
                        DBHelper(this).updateClasses(
                            intent.getIntExtra("classID", -1),
                            intent.getIntExtra("subjectID", -1),
                            typeClass.text.toString(),
                            classNumberSpinner.selectedItemPosition,
                            SimpleDateFormat("dd.MM.yyyy").parse(mStartDate.text.toString()),
                            SimpleDateFormat("dd.MM.yyyy").parse(mEndDate.text.toString()),
                            weekDay,
                            repeatSpinner.selectedItemPosition,
                            frequencySpinner.selectedItemPosition,
                            teachers.elementAt(teacherSpinner.selectedItemPosition).ID
                        )
                        Toast.makeText(this, "Я устал", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
            R.id.delete->{
                DBHelper(this).deleteClassesByID(intent.getIntExtra("classID", -1))
                Toast.makeText(this, "Удалил)", Toast.LENGTH_SHORT).show()
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
        val curYear = cal.get(Calendar.YEAR)
        val curMonth = cal.get(Calendar.MONTH)
        val curDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val mDateDialog= DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            mStartDate.text = "$dayOfMonth.${monthOfYear+1}.$year"
        }, curYear, curMonth, curDayOfMonth)
        mDateDialog.show()
    }

    fun onEditEndClick(view: View){
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

    fun addMonthToDate(date: Date, num: Int) : Date{
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, num)
        return cal.time
    }
}
