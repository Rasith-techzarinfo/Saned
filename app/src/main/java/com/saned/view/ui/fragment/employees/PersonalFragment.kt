package com.saned.view.ui.fragment.employees

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.saned.R
import com.saned.databinding.FragmentPersonalBinding
import com.saned.view.ui.fragment.dashboard.MyDashboardFragment


class PersonalFragment : Fragment() {
    lateinit var relativeLayout: RelativeLayout
    lateinit var linearLayout: LinearLayout
    lateinit var rl2: RelativeLayout
    lateinit var ll2: LinearLayout
    lateinit var rl3: RelativeLayout
    lateinit var ll3: LinearLayout
    lateinit var rl4: RelativeLayout
    lateinit var ll4: LinearLayout
    lateinit var rl5: RelativeLayout
    lateinit var ll5: LinearLayout
    lateinit var img1: ImageView
    lateinit var img2: ImageView
    lateinit var img3: ImageView
    lateinit var img4: ImageView
    lateinit var img5: ImageView
    lateinit var binding: FragmentPersonalBinding

    //add dummy ui for now
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPersonalBinding.inflate(inflater, container, false)
        init()
        linearLayout=binding.root.findViewById(R.id.ll123)
        relativeLayout=binding.root.findViewById(R.id.rl23)
        rl2=binding.root.findViewById(R.id.rl234)
        ll2=binding.root.findViewById(R.id.ll234)
        rl3=binding.root.findViewById(R.id.rl345)
        ll3=binding.root.findViewById(R.id.ll3456)
        rl4=binding.root.findViewById(R.id.rl456)
        ll4=binding.root.findViewById(R.id.ll456)
        rl5=binding.root.findViewById(R.id.rl567)
        ll5=binding.root.findViewById(R.id.ll5678)
        img1=binding.root.findViewById(R.id.img1)
        img2=binding.root.findViewById(R.id.img2)
        img3=binding.root.findViewById(R.id.img3)
        img4=binding.root.findViewById(R.id.img4)
        img5=binding.root.findViewById(R.id.img5)
        relativeLayout.setOnClickListener {
            if (linearLayout.visibility == View.GONE) {
                img1.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
                linearLayout.visibility = View.VISIBLE
            } else {
                img1.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                linearLayout.visibility = View.GONE
            }

        }
        rl2.setOnClickListener {
            if (ll2.visibility == View.GONE) {
                img2.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
                ll2.visibility = View.VISIBLE
            } else {
                img2.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                ll2.visibility = View.GONE
            }

        }
        rl3.setOnClickListener {
            if (ll3.visibility == View.GONE) {
                img3.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
                ll3.visibility = View.VISIBLE
            } else {
                img3.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                ll3.visibility = View.GONE
            }

        }
        rl4.setOnClickListener {
            if (ll4.visibility == View.GONE) {
                img4.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
                ll4.visibility = View.VISIBLE
            } else {
                img4.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                ll4.visibility = View.GONE
            }

        }
        rl5.setOnClickListener {
            if (ll5.visibility == View.GONE) {
                img5.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
                ll5.visibility = View.VISIBLE
            } else {
                img5.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                ll5.visibility = View.GONE
            }

        }

        return binding.root
    }


    private fun init() {

        //set dummy values to view
        binding.apply {
//            accuralsPercentProgress.progress = 80
            ObjectAnimator.ofInt(accuralsPercentProgress, "progress", 3).start()
            accuralPercentTextView.text = "3"

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
        fun create(): PersonalFragment {
            return PersonalFragment()
        }
    }
}