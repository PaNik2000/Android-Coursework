package com.example.androidcoursework

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.collections.ArrayList
import java.util.*


class TermFragment : Fragment() {

    private val termList = ArrayList<TermInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("abc", "term fragment")

        val view = inflater.inflate(R.layout.fragment_term, container, false)

        val listView = view.findViewById(R.id.termList) as ListView

        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        val start2 = Calendar.getInstance()
        val end2 = Calendar.getInstance()
        start.set(2019, Calendar.FEBRUARY, 1)
        end.set(2019, Calendar.MAY, 31)
        termList.add(TermInfo("1 term", start, end))
        start2.set(2019, Calendar.SEPTEMBER, 1)
        end2.set(2019, Calendar.DECEMBER, 31)
        termList.add(TermInfo("2 term", start2, end2))

        Log.d("abc", termList.size.toString())

        listView.adapter = TermListAdapter(activity as Context, R.layout.term_list_element, termList)
        listView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, itemClicked: View, position: Int, id: Long) {
                //TODO intent
            }
        })

        return view
    }
}

class TermListAdapter(context : Context, val resource: Int, objects: MutableList<TermInfo>)
    : ArrayAdapter<TermInfo>(context, resource, objects){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val term = getItem(position)
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null)
        }

        val str = "${term?.startDate?.get(Calendar.DAY_OF_MONTH)}.${term?.startDate?.get(Calendar.MONTH)} - " +
                "${term?.endDate?.get(Calendar.DAY_OF_MONTH)}.${term?.endDate?.get(Calendar.MONTH)}"

        (view?.findViewById(R.id.termName) as TextView).text = term?.name
        (view.findViewById(R.id.termDates) as TextView).text = str

        Log.d("abc", count.toString())

        return view
    }

}


class TermInfo(val name : String, val startDate : Calendar, val endDate : Calendar)