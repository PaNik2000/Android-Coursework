package com.example.androidcoursework

import java.util.Comparator

val idsOfColors = arrayOf(-769226, -1499549, -6543440, -10011977,
    -12627531, -14575885, -16537100, -16728876,
    -16738680, -11751600, -7617718, -3285959,
    -5317, -16121, -26624, -43230)

val colorResources = arrayOf(R.drawable.red, R.drawable.pink, R.drawable.lilac, R.drawable.violet,
    R.drawable.blue, R.drawable.light_blue, R.drawable.super_light_blue, R.drawable.sky_blue,
    R.drawable.tirquoise, R.drawable.green, R.drawable.light_green, R.drawable.salad,
    R.drawable.yellow, R.drawable.light_orange, R.drawable.orange, R.drawable.dark_orange)

class TimeClassComparator() : Comparator<MyClass> {
    override fun compare(o1: MyClass?, o2: MyClass?): Int {
        if (o1!!.scheduleID < o2!!.scheduleID) return -1
        if (o1.scheduleID > o2.scheduleID) return 1
        return 0
    }

}