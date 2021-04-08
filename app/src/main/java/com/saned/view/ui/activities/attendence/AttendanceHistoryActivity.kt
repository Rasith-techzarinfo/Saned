package com.saned.view.ui.activities.attendence

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.saned.R
import com.saned.model.AttendenceData
import com.saned.view.ui.adapter.attendence.AttendenceHistoryAdapter
import com.saned.view.utils.Utils
import kotlinx.android.synthetic.main.activity_attendance_history.*
import kotlinx.android.synthetic.main.activity_attendance_history.swipeRefreshLayout
import kotlinx.android.synthetic.main.activity_attendance_history.toolbar

class AttendanceHistoryActivity : AppCompatActivity(), AttendenceHistoryAdapter.ListAdapterListener {

    var attendenceHistoryArrayList: ArrayList<AttendenceData> = ArrayList()
    lateinit var attendenceHistoryAdapter: AttendenceHistoryAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_history)
        setToolBar()
        init()
    }



    private fun init() {
        //listeners
        swipeRefreshLayout.setOnRefreshListener {
            getValues()
            swipeRefreshLayout.isRefreshing = false
        }
        //get data
        getValues()
    }


    private fun getValues() {
        attendenceHistoryArrayList.clear()

        if (Utils.isInternetAvailable(this)) {
            Utils.startShimmerRL(shimmerLayout, rootLayout)

            //add dummy values
            attendenceHistoryArrayList.add(AttendenceData("3", "13 Mar 2021", "12:00",  "16:00",  "4"))
            attendenceHistoryArrayList.add(AttendenceData("2", "12 Mar 2021", "09:30",  "06:30",  "9"))
            attendenceHistoryArrayList.add(AttendenceData("1", "11 Mar 2021", "09:30",  "06:30",  "9"))



            Utils.stopShimmerRL(shimmerLayout, rootLayout)
            setupRecyclerView()


        } else {
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
        }
    }




    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        attendenceHistoryAdapter =  AttendenceHistoryAdapter(attendenceHistoryArrayList, this, this)
        recyclerView.adapter = attendenceHistoryAdapter

    }


    override fun onListItemClicked(dummyData: AttendenceData, position: Int) {

    }



    private fun setToolBar() {
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
    }


    override fun onSupportNavigateUp(): Boolean {

        finishAfterTransition()
        return true
    }

    override fun onBackPressed() {

        finishAfterTransition()
    }
}