package com.example.androidcoursework

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*


class DBHelper(context: Context)
    : SQLiteOpenHelper(context, "courseWorkDB", null, DATABASE_VERSION) {

    companion object{ val DATABASE_VERSION = 2}

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys=ON;")
    }
    override fun onCreate(db: SQLiteDatabase) {

        // TERM////////////////////////////////////////////////////////////////////////////////////////////
        db.execSQL(("create table term ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "start_date date,"
                + "end_date date"
                + ");"))

        //SCHEDULE////////////////////////////////////////////////////////////////////////////////////////////
        db.execSQL(("create table schedule ("
                + "id integer primary key autoincrement,"
                + "position integer not null,"
                + "start_time time not null,"
                + "end_time time not null"
                + ");"))

        //TEACHERS////////////////////////////////////////////////////////////////////////////////////////////
        db.execSQL(("create table teachers ("
                + "id integer primary key autoincrement,"
                + "name text not null"
                + ");"))

        //SUBJECTS////////////////////////////////////////////////////////////////////////////////////////////
        db.execSQL(("create table subjects ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "color integer,"
                + "term_id int,"

                + "foreign key(term_id) references term(id) on delete cascade"

                + ");"))

        //CLASSES////////////////////////////////////////////////////////////////////////////////////////////
        db.execSQL(("create table classes ("
                + "id integer primary key autoincrement,"
                + "subject_id int,"
                + "type text,"
                + "position int,"
                + "start_date date,"
                + "end_date date,"
                + "week_day int,"
                + "repeat_type int,"
                + "repeat_freq int,"
                + "teacher_id int,"
                + "aud text,"

                + "foreign key(subject_id) references subjects(id) on delete cascade,"
                + "foreign key(position) references schedule(id) on delete set null,"
                + "foreign key(teacher_id) references teachers(id) on delete set null"

                + ");"))

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion:Int, newVersion:Int) {
        if(oldVersion < 2 && newVersion == 2){
            db.execSQL("ALTER TABLE classes RENAME TO classes_old")
            db.execSQL("create table classes ("
                    + "id integer primary key autoincrement,"
                    + "subject_id int,"
                    + "type text,"
                    + "position int,"
                    + "start_date date,"
                    + "end_date date,"
                    + "week_day int,"
                    + "repeat_type int,"
                    + "repeat_freq int,"
                    + "teacher_id int,"
                    + "aud text,"

                    + "foreign key(subject_id) references subjects(id) on delete cascade,"
                    + "foreign key(position) references schedule(id) on delete set null,"
                    + "foreign key(teacher_id) references teachers(id) on delete set null"

                    + ");")

            val COLUMS = arrayOf<String>("id, subject_id, type, position, start_date, end_date, week_day, repeat_type, repeat_freq, teacher_id")

            val cursor : Cursor = db.query(
                "classes_old",
                COLUMS,
                null,
                null,
                null,
                null,
                null
            )

            try{
                val idColumIndex : Int = cursor.getColumnIndex("id")
                val subject_idColumIndex : Int = cursor.getColumnIndex("subject_id")
                val typeColumIndex : Int = cursor.getColumnIndex("type")
                val positionColumIndex : Int = cursor.getColumnIndex("position")
                val start_dateColumIndex : Int = cursor.getColumnIndex("start_date")
                val end_dateColumIndex : Int = cursor.getColumnIndex("end_date")
                val week_dayColumIndex : Int = cursor.getColumnIndex("week_day")
                val repeat_typeColumIndex : Int = cursor.getColumnIndex("repeat_type")
                val repeat_freqColumIndex : Int = cursor.getColumnIndex("repeat_freq")
                val teacher_idColumIndex : Int = cursor.getColumnIndex("teacher_id")

                while(cursor.moveToNext()){
                    val oldId = cursor.getInt(idColumIndex)
                    val oldSubject_id = cursor.getInt(subject_idColumIndex)
                    val oldType = cursor.getString(typeColumIndex)
                    val oldPosition = cursor.getInt(positionColumIndex)
                    val oldStartDate = cursor.getString(start_dateColumIndex)
                    val oldEndDate = cursor.getString(end_dateColumIndex)
                    val oldWeekDay = cursor.getInt(week_dayColumIndex)
                    val oldRepeatType = cursor.getInt(repeat_typeColumIndex)
                    val oldRepeatFreq = cursor.getInt(repeat_freqColumIndex)
                    val oldTeacherId = cursor.getInt(teacher_idColumIndex)

                    val cv = ContentValues()

                    cv.put("id", oldId)
                    cv.put("subject_id", oldSubject_id)
                    cv.put("type", oldType)
                    cv.put("position", oldPosition)
                    cv.put("start_date", oldStartDate)
                    cv.put("end_date", oldEndDate)
                    cv.put("week_day", oldWeekDay)
                    cv.put("repeat_type", oldRepeatType)
                    cv.put("repeat_freq", oldRepeatFreq)
                    cv.put("teacher_id", oldTeacherId)
                    cv.put("aud", "???")
                    db.insert("classes", null, cv)
                }
            }
            finally {
                cursor.close()
                db.execSQL("DROP TABLE classes_old")
            }
        }
    }

    //INSERTS////////////////////////////////////////////////////////////////////////////////////////////
    fun insertClasses(
        subject_id: Int, type: String, position: Int?,
        start_date: Date?, end_date: Date?,
        week_day: Int?, repeat_type: Int?, repeat_freq: Int?,
        teacher_id: Int?, aud: String?
    )
    {
        val record = ContentValues()
        with(record)
        {
            put("subject_id", subject_id)
            put("type", type)
            put("position", position)
            put("start_date", SimpleDateFormat("dd.MM.yyyy").format(start_date))
            put("end_date", SimpleDateFormat("dd.MM.yyyy").format(end_date))
            put("week_day", week_day)
            put("repeat_type", repeat_type)
            put("repeat_freq", repeat_freq)
            put("teacher_id", teacher_id)
            put("aud", aud)
        }

        writableDatabase.insert("classes", null, record)
    }

    fun insertTerm(name: String, start_date: Date, end_date: Date)
    {
        val record = ContentValues()
        with(record)
        {
            put("name", name)
            put("start_date", SimpleDateFormat("dd.MM.yyyy").format(start_date))
            put("end_date",SimpleDateFormat("dd.MM.yyyy").format(end_date) )
        }
        writableDatabase.insert("term", null, record)
    }

    fun insertSubjects(name: String, color: Int?, term_id: Int)
    {
        val record = ContentValues()
        with(record)
        {
            put("name",  name)
            put("color", color)
            put("term_id", term_id)
        }

        writableDatabase.insert("subjects", null, record)
    }

    fun insertSchedule(position: Int, start_time: Date, end_time: Date)
    {
        val record = ContentValues()
        with(record)
        {
            put("position", position)
            put("start_time", SimpleDateFormat("HH:mm").format(start_time))
            put("end_time", SimpleDateFormat("HH:mm").format(end_time))
        }

        writableDatabase.insert("schedule", null, record)
    }

    fun insertTeachers(name: String)
    {
        val record = ContentValues()
        with(record)
        {
            put("name", name)
        }

        writableDatabase.insert("teachers", null, record)
    }

    //GETTERS by ID////////////////////////////////////////////////////////////////////////////////////////////

    fun getClassesByID(id: Int) : MyClass?
    {
       val request = writableDatabase.query(true,
           "classes", null, "id = $id",
           null, null, null, null, null)
        if(request.moveToFirst()) {
            val idColIdx    =   request.getColumnIndex("id")
            val subIdColIdx =   request.getColumnIndex("subject_id")
            val typeColIdx  =   request.getColumnIndex("type")
            val posColIdx   =   request.getColumnIndex("position")
            val sDatColIdx  =   request.getColumnIndex("start_date")
            val eDatColIdx  =   request.getColumnIndex("end_date")
            val wDayColIdx  =   request.getColumnIndex("week_day")
            val rTypeColIdx =   request.getColumnIndex("repeat_type")
            val rFreqColIdx =   request.getColumnIndex("repeat_freq")
            val tIdColIdx   =   request.getColumnIndex("teacher_id")
            val audColIdx = request.getColumnIndex("aud")
            return MyClass(
                request.getInt(idColIdx),
                request.getInt(subIdColIdx),
                request.getInt(posColIdx),
                request.getInt(tIdColIdx),
                request.getString(typeColIdx),
                request.getString(sDatColIdx),
                request.getString(eDatColIdx),
                request.getInt(wDayColIdx),
                request.getInt(rTypeColIdx),
                request.getInt(rFreqColIdx),
                request.getString(audColIdx)
                )
        }
        return null
    }

    fun getTermByID(id: Int) : Term?
    {
        val request = writableDatabase.query(true,
            "term", null, "id = $id",
            null, null, null, null, null)
        if(request.moveToFirst()) {
            val idColIdx    =   request.getColumnIndex("id")
            val nameColIdx  =   request.getColumnIndex("name")
            val sDatColIdx  =   request.getColumnIndex("start_date")
            val eDatColIdx  =   request.getColumnIndex("end_date")

            return Term(
                request.getInt(idColIdx),
                request.getString(nameColIdx),
                request.getString(sDatColIdx),
                request.getString(eDatColIdx)
            )
        }
        return null
    }

    fun getSubjectsById(id: Int): Subject?
    {
        val request = writableDatabase.query(true,
            "subjects", null, "id = $id",
            null, null, null, null, null)
        if(request.moveToFirst()) {
            val idColIdx    =   request.getColumnIndex("id")
            val nameColIdx  =   request.getColumnIndex("name")
            val colorColIdx =   request.getColumnIndex("color")
            val tIdColIdx   =   request.getColumnIndex("term_id")

            return Subject(
                request.getInt(idColIdx),
                request.getString(nameColIdx),
                request.getInt(colorColIdx),
                request.getInt(tIdColIdx)
            )
        }
        return null
    }

    fun getScheduleById(id: Int): Schedule?
    {
        val request = writableDatabase.query(true,
            "schedule", null, "id = $id",
            null, null, null, null, null)
        if(request.moveToFirst()) {
            val idColIdx    =   request.getColumnIndex("id")
            val posColIdx   =   request.getColumnIndex("position")
            val sTimeColIdx =   request.getColumnIndex("start_time")
            val eTimeColIdx =   request.getColumnIndex("end_time")

            return Schedule(
                request.getInt(idColIdx),
                request.getInt(posColIdx),
                request.getString(sTimeColIdx),
                request.getString(eTimeColIdx)
            )
        }
        return null
    }

    fun getTeachersById(id: Int): Teacher?
    {
        val request = writableDatabase.query(true,
            "teachers", null, "id = $id",
            null, null, null, null, null)
        if(request.moveToFirst()) {
            val idColIdx    =   request.getColumnIndex("id")
            val nameColIdx  =   request.getColumnIndex("name")

            return Teacher(
                request.getInt(idColIdx),
                request.getString(nameColIdx)
            )
        }
        return null
    }

    fun getSubTeachByID(id: Int)
    {
    }

    //GETTERS////////////////////////////////////////////////////////////////////////////////////////////

    fun getClasses() : MutableList<MyClass>
    {
        val classesContent = mutableListOf<MyClass>()
        val request = writableDatabase.query("classes", null, null, null, null, null, null)

        if (request.moveToFirst())
        {
            val idColIdx    =   request.getColumnIndex("id")
            val subIdColIdx =   request.getColumnIndex("subject_id")
            val typeColIdx  =   request.getColumnIndex("type")
            val posColIdx   =   request.getColumnIndex("position")
            val sDatColIdx  =   request.getColumnIndex("start_date")
            val eDatColIdx  =   request.getColumnIndex("end_date")
            val wDayColIdx  =   request.getColumnIndex("week_day")
            val rTypeColIdx =   request.getColumnIndex("repeat_type")
            val rFreqColIdx =   request.getColumnIndex("repeat_freq")
            val tIdColIdx   =   request.getColumnIndex("teacher_id")
            val audColIdx = request.getColumnIndex("aud")

            do
            {
                classesContent.add(
                    MyClass(
                    request.getInt(idColIdx),
                    request.getInt(subIdColIdx),
                    request.getInt(posColIdx),
                    request.getInt(tIdColIdx),
                    request.getString(typeColIdx),
                    request.getString(sDatColIdx),
                    request.getString(eDatColIdx),
                    request.getInt(wDayColIdx),
                    request.getInt(rTypeColIdx),
                    request.getInt(rFreqColIdx),
                    request.getString(audColIdx)
                    )
                )
            }
            while (request.moveToNext())
        }
        else
        request.close()
        return classesContent
    }

    fun getTerm(): MutableList<Term>
    {
        val termContent = mutableListOf<Term>()
        val request = writableDatabase.query("term", null, null, null, null, null, null)
        if (request.moveToFirst())
        {
            val idColIdx    =   request.getColumnIndex("id")
            val nameColIdx  =   request.getColumnIndex("name")
            val sDatColIdx  =   request.getColumnIndex("start_date")
            val eDatColIdx  =   request.getColumnIndex("end_date")

            do
            {
                termContent.add(
                    Term(
                        request.getInt(idColIdx),
                        request.getString(nameColIdx),
                        request.getString(sDatColIdx),
                        request.getString(eDatColIdx)
                    )
                )
            }
            while (request.moveToNext())
        }
        else
        request.close()
        return termContent
    }

    fun getSubjects(): MutableList<Subject>
    {
        val subjectsContent = mutableListOf<Subject>()
        val request = writableDatabase.query("subjects", null, null, null, null, null, null)


        if(request.moveToFirst()) {
            val idColIdx    =   request.getColumnIndex("id")
            val nameColIdx  =   request.getColumnIndex("name")
            val colorColIdx =   request.getColumnIndex("color")
            val tIdColIdx   =   request.getColumnIndex("term_id")

                do
                {
                    subjectsContent.add(
                        Subject(
                            request.getInt(idColIdx),
                            request.getString(nameColIdx),
                            request.getInt(colorColIdx),
                            request.getInt(tIdColIdx)
                        )
                    )
                }
                while (request.moveToNext())
        }
        else
        request.close()
        return subjectsContent
    }


    fun getSchedule(): MutableList<Schedule>
    {
        val scheduleContent = mutableListOf<Schedule>()
        val request = writableDatabase.query("schedule", null, null, null, null, null, null)


        if(request.moveToFirst()) {
            val idColIdx    =   request.getColumnIndex("id")
            val posColIdx   =   request.getColumnIndex("position")
            val sTimeColIdx =   request.getColumnIndex("start_time")
            val eTimeColIdx =   request.getColumnIndex("end_time")

            do
            {
                scheduleContent.add(
                    Schedule(
                        request.getInt(idColIdx),
                        request.getInt(posColIdx),
                        request.getString(sTimeColIdx),
                        request.getString(eTimeColIdx)
                    )
                )
            }
            while (request.moveToNext())
        }
        else
        request.close()
        return scheduleContent
    }

    fun getTeachers(): MutableList<Teacher>
    {
        val teachersContent = mutableListOf<Teacher>()
        val request = writableDatabase.query("teachers", null, null, null, null, null, null)


        if(request.moveToFirst()) {
            val idColIdx    =   request.getColumnIndex("id")
            val nameColIdx  =   request.getColumnIndex("name")

            do
            {
                teachersContent.add(
                    Teacher(
                        request.getInt(idColIdx),
                        request.getString(nameColIdx)
                    )
                )
            }
            while (request.moveToNext())
        }
        else
        request.close()
        return teachersContent
    }

    //UPDATE//////////////////////////////////////////////////////////////////////////////////////////////

    fun updateClasses(
        id: Int,
        subject_id: Int, type: String, position: Int?,
        start_date: Date, end_date: Date, week_day: Int,
        repeat_type: Int, repeat_freq: Int,
        teacher_id: Int?, aud: String?
    )
    {
        val record = ContentValues()
        with(record)
        {
            put("subject_id", subject_id)
            put("type", type)
            put("position", position)
            put("start_date", SimpleDateFormat("dd.MM.yyyy").format(start_date))
            put("end_date", SimpleDateFormat("dd.MM.yyyy").format(end_date))
            put("week_day", week_day)
            put("repeat_type", repeat_type)
            put("repeat_freq", repeat_freq)
            put("teacher_id", teacher_id)
            put("aud", aud)
        }

        writableDatabase.update("classes", record, "id = $id", null)
    }

    fun updateTerm(id: Int, name: String?, start_date: Date?, end_date: Date?)
    {
        val record = ContentValues()
        with(record)
        {
            put("name", name)
            put("start_date", SimpleDateFormat("dd.MM.yyyy").format(start_date))
            put("end_date", SimpleDateFormat("dd.MM.yyyy").format(end_date))
        }

        writableDatabase.update("term", record, "id = $id", null)
    }

    fun updateSubjects(id: Int, name: String, color: Int?, term_id: Int)
    {
        val record = ContentValues()
        with(record)
        {
            put("name", name)
            put("color", color)
            put("term_id", term_id)
        }

        writableDatabase.update("subjects", record, "id = $id", null)
    }

    fun updateSchedule(id: Int, position: Int, start_time: Date, end_time: Date)
    {
        val record = ContentValues()
        with(record)
        {
            put("position", position)
            put("start_time", SimpleDateFormat("HH:mm").format(start_time))
            put("end_time", SimpleDateFormat("HH:mm").format(end_time))
        }

        writableDatabase.update("schedule", record, "id = $id", null)
    }

    fun updateTeachers(id: Int, name: String)
    {
        val record = ContentValues()
        with(record)
        {
            put("name", name)
        }

        writableDatabase.update("teachers", record, "id = $id", null)
    }

    //GETTERS by ID////////////////////////////////////////////////////////////////////////////////////////////

    fun deleteClassesByID(id: Int)
    {
        val deleted =  writableDatabase.delete("classes", "id = $id", null)
    }

    fun deleteTermByID(id: Int)
    {
        val deleted =  writableDatabase.delete("term", "id = $id", null)
    }

    fun deleteSubjectsById(id: Int)
    {
        val deleted =  writableDatabase.delete("subjects", "id = $id", null)
    }

    fun deleteScheduleById(id: Int)
    {
        val deleted =  writableDatabase.delete("schedule", "id = $id", null)
    }

    fun deleteTeachersById(id: Int)
    {
        val deleted =  writableDatabase.delete("teachers", "id = $id", null)
    }
}