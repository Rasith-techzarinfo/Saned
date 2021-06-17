package com.saned.view.ui.fragment.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.saned.R
import com.saned.model.Data
import com.saned.sanedApplication
import com.saned.view.error.SANEDError
import com.saned.view.utils.Utils
import kotlinx.android.synthetic.main.activity_my_employees.*
import kotlinx.coroutines.launch

class WorkItemsFragment : Fragment() {


    //add dummy ui for now
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_work_items, container, false)
        init()
        return view
    }





    private fun init() {

        //set dummy values to view

    }














    companion object {
        fun create(): WorkItemsFragment {
            return WorkItemsFragment()
        }
    }
}