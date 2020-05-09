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

        // TERM////////////////////////////////////////////////////////////////////////////////////////////
        db.execSQL(("create table term ("
                + "id integer primary key auto_increment,"
                + "name text,"
                + "start_date date,"
                + "end_date date"
                + ");"))
        Log.d(LOG_TAG, "Term created")

        //SCHEDULE////////////////////////////////////////////////////////////////////////////////////////////
        db.execSQL(("create table schedule ("
                + "id integer primary key auto_increment,"
                + "position integer not null,"
                + "start_time time not null,"
                + "end_time time not null"
                + ");"))
        Log.d(LOG_TAG, "Schedule created")

        //TEACHERS////////////////////////////////////////////////////////////////////////////////////////////
        db.execSQL(("create table teachers ("
                + "id integer primary key auto_increment,"
                + "name text not null"
                + ");"))
        Log.d(LOG_TAG, "Teachers created")

        //SUBJECTS////////////////////////////////////////////////////////////////////////////////////////////
        db.execSQL(("create table subjects ("
                + "id integer primary key auto_increment,"
                + "name text,"
                + "color integer,"
                + "term_id int,"

                + "foreign key(term_id) references term(id) on delete cascade"

                + ");"))
        Log.d(LOG_TAG, "Subjects created")

        //SUB_TEACHERS////////////////////////////////////////////////////////////////////////////////////////////
        db.execSQL(("create table sub_teachers("
                + "id integer primary key auto_increment,"
                + "subject_id integer,"
                + "teacher_id integer,"

                + "foreign key(subject_id) references subjects(id) on delete cascade,"
                + "foreign key(teacher_id) references teachers(id) on delete cascade"

                + ");"))
        Log.d(LOG_TAG, "Sub_teachers created")

        //CLASSES////////////////////////////////////////////////////////////////////////////////////////////
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

    //INSERTS////////////////////////////////////////////////////////////////////////////////////////////
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

    fun insertSchedule(position: Int, start_time: Time, end_time: Time)
    {
        Log.d(LOG_TAG, "--- insertSchedule ---")
        val record = ContentValues()
        with(record)
        {
            put("position", position)
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
            return MyClass(
                request.getInt(idColIdx),
                request.getInt(subIdColIdx),
                request.getInt(posColIdx),
                request.getInt(tIdColIdx),
                request.getString(typeColIdx),
                request.getString(sDatColIdx),
                request.getString(eDatColIdx),
                request.getInt(wDayColIdx),
                request.getString(rTypeColIdx),
                request.getString(rFreqColIdx)
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
                    request.getString(rTypeColIdx),
                    request.getString(rFreqColIdx)
                    )
                )
            }
            while (request.moveToNext())
        }
        else
            Log.d(LOG_TAG, "0 rows")
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
            Log.d(LOG_TAG, "0 rows")
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
            Log.d(LOG_TAG, "0 rows")
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
            Log.d(LOG_TAG, "0 rows")
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
            Log.d(LOG_TAG, "0 rows")
        request.close()
        return teachersContent
    }

    fun getSubTeach()
    {
    }

    //UPDATE//////////////////////////////////////////////////////////////////////////////////////////////

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
    }

    //GETTERS by ID////////////////////////////////////////////////////////////////////////////////////////////

    fun deleteClassesByID(id: Int)
    {
        val deleted =  writableDatabase.delete("classes", "id = $id", null)
        Log.d(LOG_TAG, "rows deleted from classes by deleteClassesByID(id: Int): $deleted")
    }

    fun deleteTermByID(id: Int)
    {
        val deleted =  writableDatabase.delete("term", "id = $id", null)
        Log.d(LOG_TAG, "rows deleted from term by deleteTermByID(id: Int): $deleted")
    }

    fun deleteSubjectsById(id: Int)
    {
        val deleted =  writableDatabase.delete("subjects", "id = $id", null)
        Log.d(LOG_TAG, "rows deleted from term by deleteSubjectsById(id: Int): $deleted")
    }

    fun deleteScheduleById(id: Int)
    {
        val deleted =  writableDatabase.delete("schedule", "id = $id", null)
        Log.d(LOG_TAG, "rows deleted from term by deleteScheduleById(id: Int): $deleted")
    }

    fun deleteTeachersById(id: Int)
    {
        val deleted =  writableDatabase.delete("teachers", "id = $id", null)
        Log.d(LOG_TAG, "rows deleted from term by deleteTeachersById(id: Int): $deleted")
    }

    fun deleteSubTeachByID(id: Int)
    {
    }

    //DELETE////////////////////////////////////////////////////////////////////////////////////////////
    fun deleteClasses()
    {
        Log.d(LOG_TAG, "--- deleteClasses ---")

        val deleted =  writableDatabase.delete("classes", null, null)
        Log.d(LOG_TAG, "rows deleted from term by deleteClasses(): $deleted")
    }

    fun deleteTerm()
    {
        Log.d(LOG_TAG, "--- deleteTerm ---")

        val deleted =  writableDatabase.delete("term", null, null)
        Log.d(LOG_TAG, "rows deleted from term by deleteTerm(): $deleted")
    }

    fun deleteSubjects()
    {
        Log.d(LOG_TAG, "--- deleteSubjects ---")

        val deleted =  writableDatabase.delete("subjects", null, null)
        Log.d(LOG_TAG, "rows deleted from term by deleteSubjects(): $deleted")
    }

    fun deleteSchedule()
    {
        Log.d(LOG_TAG, "--- deleteSchedule ---")

        val deleted =  writableDatabase.delete("schedule", null, null)
        Log.d(LOG_TAG, "rows deleted from term by deleteSchedule(): $deleted")
    }

    fun deleteTeachers()
    {
        Log.d(LOG_TAG, "--- deleteTeachers ---")

        val deleted =  writableDatabase.delete("teachers", null, null)
        Log.d(LOG_TAG, "rows deleted from term by deleteTeachers(): $deleted")
    }

    fun deleteSubTeach()
    {
    }


}