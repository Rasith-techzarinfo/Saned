package com.saned.view.ui.fragment.dashboard

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.saned.R
import com.saned.databinding.FragmentMyDashboardBinding
import kotlinx.android.synthetic.main.fragment_my_dashboard.*
import java.util.ArrayList

class MyDashboardFragment : Fragment() {

  lateinit var binding : FragmentMyDashboardBinding

    //add dummy ui for now
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding  = FragmentMyDashboardBinding.inflate(inflater, container, false)
        init()

        return binding.root
    }



    private fun init() {

        //set dummy values to view
        binding.apply{
//            accuralsPercentProgress.progress = 80
            ObjectAnimator.ofInt(accuralsPercentProgress, "progress", 80).start()
            accuralPercentTextView.text = "80%"

            ObjectAnimator.ofInt(earningPaySlipProgress, "progress", 100).start()
            earningPaySlipTextView.text = "100%"

            ObjectAnimator.ofInt(deductionPaySlipProgress, "progress", 20).start()
            deductionPaySlipTextView.text = "20%"

            ObjectAnimator.ofInt(salaryPaySlipProgress, "progress", 80).start()
            salaryPaySlipTextView.text = "80%"

//            absentRangeSlider.isEnabled = false
            ObjectAnimator.ofInt(absentPercentProgress, "progress", 50).start()
            absentPercentTextView.text = "50%"

        }

    }





















    companion object {
        fun create(): MyDashboardFragment {
            return MyDashboardFragment()
        }
    }
}