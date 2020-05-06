package com.example.androidcoursework

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.Time
import java.util.*


internal class DBHelper(context: Context)
    : SQLiteOpenHelper(context, "courseWorkDB", null, 1) {

    val LOG_TAG = "DBLogs"

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(LOG_TAG, "--- onCreate database ---")

        // TERM
        db.execSQL(("create table term ("
                + "id integer primary key auto_increment,"
                + "name text,"
                + "start_date date,"
                + "end_date date"
                + ");"))
        Log.d(LOG_TAG, "Term created")

        //SCHEDULE
        db.execSQL(("create table schedule ("
                + "id integer primary key auto_increment,"
                + "start_time time not null,"
                + "end_time time not null"
                + ");"))
        Log.d(LOG_TAG, "Schedule created")

        //TEACHERS
        db.execSQL(("create table teachers ("
                + "id integer primary key auto_increment,"
                + "name text not null"
                + ");"))
        Log.d(LOG_TAG, "Teachers created")

        //SUBJECTS
        db.execSQL(("create table subjects ("
                + "id integer primary key auto_increment,"
                + "name text,"
                + "color integer,"
                + "term_id int,"

                + "foreign key(term_id) references term(id) on delete cascade"

                + ");"))
        Log.d(LOG_TAG, "Subjects created")

        //SUB_TEACHERS
        db.execSQL(("create table sub_teachers("
                + "id integer primary key auto_increment,"
                + "subject_id integer,"
                + "teacher_id integer,"

                + "foreign key(subject_id) references subjects(id) on delete cascade,"
                + "foreign key(teacher_id) references teachers(id) on delete cascade"

                + ");"))
        Log.d(LOG_TAG, "Sub_teachers created")

        //CLASSES
        db.execSQL(("create table classes ("
                + "id integer primary key auto_increment,"
                + "subject_id int,"
                + "type text,"
                + "position int,"
                + "start_date date,"
                + "end_date date,"
                + "week_day int,"
                + "repeat_type int,"
                + "repeat_freq int,"
                + "teacher_id int,"

                + "foreign key(subject_id) references subjects(id) on delete cascade,"
                + "foreign key(position) references schedule(id) on delete set null"
                + "foreign key(teacher_id) references teachers(id) on delete set null"

                + ");"))
        Log.d(LOG_TAG, "Classes created")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion:Int, newVersion:Int) {
        Log.d(LOG_TAG, "--- onUpdate database ---")
    }

    //INSERTS
    fun insertClasses(
        subject_id: Int, type: String, position: Int,
        start_date: Date?, end_date: Date?,
        week_day: Int?, repeat_type: Int?, repeat_freq: Int?,
        teacher_id: Int?
    )
    {
        Log.d(LOG_TAG, "--- insertClasses ---")

        //TODO null processing in insert classes

        val record = ContentValues()
        with(record)
        {
            put("subject_id", subject_id)
            put("type", type)
            put("position", position)
            put("start_date", start_date.toString())
            put("end_date", end_date.toString())
            put("week_day", week_day)
            put("repeat_type", repeat_type)
            put("repeat_freq", repeat_freq)
            put("teacher_id", teacher_id)
        }

        writableDatabase.insert("classes", null, record)
        Log.d(LOG_TAG, "inserted into classes")
    }

    fun insertTerm(name: String, start_date: Date, end_date: Date)
    {
        Log.d(LOG_TAG, "--- insertTerm ---")
        val record = ContentValues()
        with(record)
        {
            put("name", name)
            put("start_date", start_date.toString())
            put("end_date", end_date.toString())
        }

        writableDatabase.insert("term", null, record)
        Log.d(LOG_TAG, "inserted into term")
    }

    fun insertSubjects(name: String, color: Int?, term_id: Int)
    {
        // TODO null processing in insert subjects

        Log.d(LOG_TAG, "--- insertSubjects ---")
        val record = ContentValues()
        with(record)
        {
            put("name",  name)
            put("color", color)
            put("term_id", term_id)
        }

        writableDatabase.insert("subjects", null, record)
        Log.d(LOG_TAG, "inserted into subjects")
    }

    fun insertSchedule(start_time: Time, end_time: Time)
    {
        Log.d(LOG_TAG, "--- insertSchedule ---")
        val record = ContentValues()
        with(record)
        {
            put("start_time", start_time.toString())
            put("end_time", end_time.toString())
        }

        writableDatabase.insert("schedule", null, record)
        Log.d(LOG_TAG, "inserted into schedule")
    }

    fun insertTeachers(name: String)
    {
        Log.d(LOG_TAG, "--- insertTeachers ---")
        val record = ContentValues()
        with(record)
        {
            put("name", name)
        }

        writableDatabase.insert("teachers", null, record)
        Log.d(LOG_TAG, "inserted into teachers")
    }

    fun insertSubTeach(subject_id: Int, teacher_id: Int)
    {
        Log.d(LOG_TAG, "--- insertSubTeach ---")
        val record = ContentValues()
        with(record)
        {
            put("subject_id", subject_id)
            put("teacher_id", teacher_id)
        }

        writableDatabase.insert("sub_teachers", null, record)
        Log.d(LOG_TAG, "inserted into sub-teachers")
    }

    //GETTERS by ID

    //TODO return values to by id getters

    fun getClassesByID(id: Int)
    {

    }

    fun getTermByID(id: Int)
    {

    }

    fun getSubjectsById(id: Int)
    {

    }

    fun getSheduleById(id: Int)
    {

    }

    fun getTeachersById(id: Int)
    {

    }

    fun getSubTeachByID(id: Int)
    {

    }

    //GETTERS

    //TODO return values to getters

    fun getClasses()
    {
       val request = writableDatabase.query("classes", null, null, null, null, null, null)
    }

    fun getTerm()
    {
        val request = writableDatabase.query("term", null, null, null, null, null, null)
    }

    fun getSubjects()
    {
        val request = writableDatabase.query("subjects", null, null, null, null, null, null)
    }

    fun getShedule()
    {
        val request = writableDatabase.query("schedule", null, null, null, null, null, null)
    }

    fun getTeachers()
    {
        val request = writableDatabase.query("teachers", null, null, null, null, null, null)
    }

    fun getSubTeach()
    {
        val request = writableDatabase.query("sub_teachers", null, null, null, null, null, null)
    }

    //UPDATE
    fun updateClasses(
        id: Int,
        subject_id: Int, type: String, position: Int,
        start_date: Date, end_date: Date, week_day: Int,
        repeat_type: Int, repeat_freq: Int,
        teacher_id: Int?
    )
    {
        Log.d(LOG_TAG, "--- updateClasses ---")

        //TODO null processing for arguments in update classes

        val record = ContentValues()
        with(record)
        {
            put("subject_id", subject_id)
            put("type", type)
            put("position", position)
            put("start_date", start_date.toString())
            put("end_date", end_date.toString())
            put("week_day", week_day)
            put("repeat_type", repeat_type)
            put("repeat_freq", repeat_freq)
            put("teacher_id", teacher_id)
        }

        writableDatabase.update("classes", record, "id = $id", null)
        Log.d(LOG_TAG, "updated Classes")
    }

    fun updateTerm(id: Int, name: String?, start_date: Date?, end_date: Date?)
    {
        Log.d(LOG_TAG, "--- updateTerm ---")

        //TODO null processing for arguments in update term

        val record = ContentValues()
        with(record)
        {
            put("name", name)
            put("start_date", start_date.toString())
            put("end_date", end_date.toString())
        }

        writableDatabase.update("term", record, "id = $id", null)
        Log.d(LOG_TAG, "updated term")
    }

    fun updateSubjects(id: Int, name: String, color: Int?, term_id: Int)
    {
        Log.d(LOG_TAG, "--- updateSubjects ---")

        //TODO null processing for arguments in update subjects

        val record = ContentValues()
        with(record)
        {
            put("name", name)
            put("color", color)
            put("term_id", term_id)
        }

        writableDatabase.update("subjects", record, "id = $id", null)
        Log.d(LOG_TAG, "updated subjects")
    }

    fun updateSchedule(id: Int, start_time: Time, end_time: Time)
    {
        Log.d(LOG_TAG, "--- updateSchedule ---")
        val record = ContentValues()
        with(record)
        {
            put("start_time", start_time.toString())
            put("end_time", end_time.toString())
        }

        writableDatabase.update("schedule", record, "id = $id", null)
        Log.d(LOG_TAG, "updated schedule")
    }

    fun updateTeachers(id: Int, name: String)
    {
        Log.d(LOG_TAG, "--- updateTeachers ---")
        val record = ContentValues()
        with(record)
        {
            put("name", name)
        }

        writableDatabase.update("teachers", record, "id = $id", null)
        Log.d(LOG_TAG, "updated teachers")
    }

    fun updateSubTeach()
    {
        // TODO Sub_Teachers update
    }

}