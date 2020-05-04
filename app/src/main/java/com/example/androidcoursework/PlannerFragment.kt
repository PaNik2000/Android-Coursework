package com.example.androidcoursework

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

class PlannerFragment : Fragment() {

    lateinit var myAdapter : MyPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_planner, container, false)
        myAdapter = MyPageAdapter(requireActivity().supportFragmentManager)
        (view.findViewById(R.id.viewPager) as ViewPager).adapter = myAdapter
        (view.findViewById(R.id.viewPager) as ViewPager).currentItem = 1
        Log.d("abc", "planner fragment")
        return view
    }

    fun removeFragments() {
        myAdapter.removeFragments()
    }

}