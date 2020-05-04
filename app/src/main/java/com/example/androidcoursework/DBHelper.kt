package com.example.androidcoursework

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


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

        //SCHEDULE
        db.execSQL(("create table schedule ("
                + "id integer primary key auto_increment,"
                + "start_time time,"
                + "end_time time"
                + ");"))

        //TEACHERS
        db.execSQL(("create table teachers ("
                + "id integer primary key auto_increment,"
                + "name text"
                + ");"))

        //SUBJECTS
        db.execSQL(("create table subjects ("
                + "id integer primary key auto_increment,"
                + "name text,"
                + "color integer,"
                + "term_id int,"

                + "foreign key(term_id) references term(id) on delete cascade"

                + ");"))

        //SUB_TEACHERS
        db.execSQL(("create table sub_teachers("
                + "id integer primary key auto_increment,"
                + "subject_id integer,"
                + "teacher_id integer,"

                + "foreign key(subject_id) references subjects(id) on delete cascade,"
                + "foreign key(teacher_id) references teachers(id) on delete cascade"

                + ");"))

        //CLASSES
        db.execSQL(("create table classes ("
                + "id integer primary key auto_increment,"
                + "subject_id int,"
                + "type text,"
                + "position int,"
                + "star_date date,"
                + "end_date date,"
                + "week_day int,"
                + "repeat_type int,"
                + "repeat_freq int,"
                + "teacher_id int,"

                + "foreign key(subject_id) references subjects(id) on delete cascade,"
                + "foreign key(position) references schedule(id) on delete set null"
                + "foreign key(teacher_id) references teachers(id) on delete set null"

                + ");"))

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion:Int, newVersion:Int) {

    }
}