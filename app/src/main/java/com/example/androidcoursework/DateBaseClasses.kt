package com.example.androidcoursework

import java.util.*

class Term(val ID: Int?, val name : String, val startDate : String, val endDate : String)

class Subject(val ID: Int?, val name : String, val color: Int, val termID: Int)

class Schedule(val ID: Int?, val position: Int, val startTime: String, val endTime: String)

class Teacher(val ID: Int?, val name: String)

class MyClass(val ID: Int?, val subjectID: Int, val scheduleID: Int, val teacherID: Int, val type: String, val startDate : String, val endDate : String, val weekDay: Int, val repeatType: Int, val repeatFreq: Int)

enum class RepeatTypes (val TYPE: Int){
    DAY(0),
    WEEK(1)
}