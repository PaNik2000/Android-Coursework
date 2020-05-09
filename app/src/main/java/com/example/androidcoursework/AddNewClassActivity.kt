package com.example.androidcoursework

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class AddNewClassActivity : AppCompatActivity() {

    lateinit var toolbar : Toolbar
    //Заполнить их из БД
    var classNumber = arrayOf("1 пара", "2 пара", "3 пара", "4 пара", "5 пара")
    var teachers = arrayOf("Первый преподаватель", "Второй преподаватель", "Третий преподаватель", "Четвертый преподаватель", "Пятый преподаватель", "Шестой преподаватель")

    lateinit var mStartDate : TextView
    lateinit var mEndDate : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_class)

        //Получаем ID subject'a - val subjectID = intent.getIntExtra("subjectID")
        //Получаем name subject'a - val subjectName = intent.getStringExtra("subjectName")
        //Получаем ID class'a - val classID = intent.getIntExtra("classID")
        //Получаем name class'a - val className = intent.getStringExtra("className")

        mStartDate = findViewById<TextView>(R.id.editStart)
        mEndDate = findViewById<TextView>(R.id.editEnd)
        var curDate = Date()
        val dateFormat = SimpleDateFormat("dd.MM.yyy")
        mStartDate.text = dateFormat.format(curDate)
        curDate = addMonthToDate(curDate, 3)
        mEndDate.text = dateFormat.format(curDate)

        toolbar = findViewById<Toolbar>(R.id.addClassToolBar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        if(intent.getStringExtra("Create or change") == "create"){
            supportActionBar?.title = "New class"
        } else if(intent.getStringExtra("Create or change") == "change"){
            supportActionBar?.title = "Subject"// = subjectName
            val classType = findViewById<TextView>(R.id.editTypeClass)
            classType.text = "TYPE" // = classType
        }

        var aa = ArrayAdapter(this, R.layout.my_simple_spinner_item, classNumber)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(editPositionClass){
            adapter = aa
            prompt = "Select number of the class"
            gravity = Gravity.CENTER
        }
        aa = ArrayAdapter(this, R.layout.my_simple_spinner_item, teachers)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(editTeacher){
            adapter = aa
            prompt = "Select number of the class"
            gravity = Gravity.CENTER
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add -> {
                //ЕСЛИ СОЗДАЕМ ТО
                    if(findViewById<EditText>(R.id.editTypeClass).text.isEmpty()){
                        Toast.makeText(this, "Fill in the gaps!", Toast.LENGTH_SHORT).show()
                    } else if(!findViewById<ToggleButton>(R.id.monday).isChecked && !findViewById<ToggleButton>(R.id.tuesday).isChecked &&
                        !findViewById<ToggleButton>(R.id.wednesday).isChecked && !findViewById<ToggleButton>(R.id.thursday).isChecked &&
                        !findViewById<ToggleButton>(R.id.friday).isChecked && !findViewById<ToggleButton>(R.id.saturday).isChecked &&
                        !findViewById<ToggleButton>(R.id.sunday).isChecked){
                        Toast.makeText(this, "Choose the day of a week!", Toast.LENGTH_SHORT).show()
                    } else {
                        //По-видимому делаем ID = null, тк он autoincrement
                        val newClass = MyClass(
                            null,
                            11/*intent.getIntExtra("subjectID")*/,
                            findViewById<Spinner>(R.id.editTypeClass).selectedItemPosition,
                            findViewById<Spinner>(R.id.editTeacher).selectedItemPosition,
                            findViewById<EditText>(R.id.editTypeClass).text.toString(),
                            findViewById<TextView>(R.id.editStart).text.toString(),
                            findViewById<TextView>(R.id.editEnd).text.toString(),
                            1010101,
                            findViewById<Spinner>(R.id.editRepeatType).selectedItem.toString(),
                            findViewById<Spinner>(R.id.editFreqType).selectedItem.toString()
                        )
                        //db.insert(newTerm)
                    //ИНАЧЕ
                        //db.update(classID,
                        //intent.getIntExtra("subjectID"),
                        //findViewById<Spinner>(R.id.editTypeClass).selectedItemPosition,
                        //findViewById<Spinner>(R.id.editTeacher).selectedItemPosition,
                        //findViewById<EditText>(R.id.editTypeClass).text.toString(),
                        //findViewById<TextView>(R.id.editStart).text.toString(),
                        //findViewById<TextView>(R.id.editEnd).text.toString(),
                        //1010101,
                        //findViewById<Spinner>(R.id.editRepeatType).selectedItem.toString(),
                        //findViewById<Spinner>(R.id.editFreqType).selectedItem.toString())
                    //Возвращаемся к fragment_term
                    Toast.makeText(this, "Я устал", Toast.LENGTH_SHORT).show()
                    finish()
                }
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
            // Display Selected date in startDate
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
