package com.saned.view.ui.fragment.dashboard

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saned.R
import com.saned.databinding.FragmentMyDashboardBinding
import com.saned.databinding.FragmentWorkItemsBinding

class WorkItemsFragment : Fragment() {

    lateinit var binding : FragmentWorkItemsBinding

    //add dummy ui for now
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding  = FragmentWorkItemsBinding.inflate(inflater, container, false)
        init()

        return binding.root
    }





    private fun init() {

        //set dummy values to view
        binding.apply{


        }

    }















    companion object {
        fun create(): WorkItemsFragment {
            return WorkItemsFragment()
        }
    }
}