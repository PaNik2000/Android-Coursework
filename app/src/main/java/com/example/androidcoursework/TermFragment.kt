package com.example.androidcoursework

import android.content.Context
import android.content.Intent
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


class TermFragment : Fragment() {

    lateinit var termList: MutableList<Term>
    lateinit var listView: ListView
//    val db = DBHelper(activity as Context)

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
        listView = view.findViewById(R.id.termList) as ListView

        //Получаем все term'ы
        termList = DBHelper(activity as Context).getTerm()

        //Это потом не понадобится /////////////////
//        val start = Calendar.getInstance()
//        val end = Calendar.getInstance()
//        val start2 = Calendar.getInstance()
//        val end2 = Calendar.getInstance()
//        start.set(2019, Calendar.FEBRUARY, 1)
//        end.set(2019, Calendar.MAY, 31)
//        termList.add(TermInfo("1 term", start, end))
//        start2.set(2019, Calendar.SEPTEMBER, 1)
//        end2.set(2019, Calendar.DECEMBER, 31)
//        termList.add(TermInfo("2 term", start2, end2))
        ////////////////////////////////////////////////////

        Log.d("abc", termList.size.toString())

        listView.adapter = TermListAdapter(activity as Context, R.layout.term_list_element, termList)
        listView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, itemClicked: View, position: Int, id: Long) {
                val intent = Intent(activity, SubjectsInTermActivity::class.java)
                //Как-то получаем айдишник текущего term'a, а также его имя и передаем вместе с интентом
                val termID = termList[position].ID
                val termName = termList[position].name
                //Запрос жопы...
                intent.putExtra("termID", termID)
                intent.putExtra("termName", termName)
                startActivity(intent)
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        termList = DBHelper(activity as Context).getTerm()
        listView.adapter = TermListAdapter(activity as Context, R.layout.term_list_element, termList)
        Log.d("term", "onResume")
    }

}

class TermListAdapter(context : Context, val resource: Int, objects: MutableList<Term>)
    : ArrayAdapter<Term>(context, resource, objects){
    val db = DBHelper(context)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val term = getItem(position)
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null)
        }

        val str = "${term?.startDate} - ${term?.endDate}"

        (view?.findViewById(R.id.termName) as TextView).text = term?.name
        (view.findViewById(R.id.termDates) as TextView).text = str

        Log.d("abc", count.toString())

        return view
    }

}