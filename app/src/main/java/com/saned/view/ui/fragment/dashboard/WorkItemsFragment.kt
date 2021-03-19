package com.saned.view.ui.fragment.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.saned.R

class WorkItemsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_work_items, container, false)
        ButterKnife.bind(this, view)

        return view
    }



    companion object {
        fun create(): WorkItemsFragment {
            return WorkItemsFragment()
        }
    }



}