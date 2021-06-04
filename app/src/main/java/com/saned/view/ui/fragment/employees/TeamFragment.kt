package com.saned.view.ui.fragment.employees

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.saned.databinding.FragmentTeamBinding


class TeamFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var binding : FragmentTeamBinding

    //add dummy ui for now
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentTeamBinding.inflate(inflater, container, false)
        init()

        return binding.root
    }





    private fun init() {

        //set dummy values to view
        binding.apply {


        }

    }




    companion object {
        fun create(): TeamFragment {
            return TeamFragment()
        }
    }
}