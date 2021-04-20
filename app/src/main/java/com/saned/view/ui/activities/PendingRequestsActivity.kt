package com.saned.view.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.saned.R
import com.saned.model.HAData
import com.saned.view.ui.adapter.pendingRequests.PendingRequestsAdapter
import com.saned.view.utils.Utils
import kotlinx.android.synthetic.main.activity_my_employees.*

class PendingRequestsActivity : AppCompatActivity() {


    var myPendingsArrayList: ArrayList<HAData> = ArrayList()
    lateinit var myPendingAdapter: PendingRequestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_requests)
        setToolBar()
        init()

    }

    private fun init() {

        swipeRefreshLayout.setOnRefreshListener {
            getValues()
            swipeRefreshLayout.isRefreshing = false
        }
        //get data
        getValues()

    }


    private fun getValues() {
        myPendingsArrayList.clear()

        if (Utils.isInternetAvailable(this)) {
            Utils.startShimmerRL(shimmerLayout, rootLayout)

            //add dummy values
            myPendingsArrayList.add(HAData("3", "vacation", "123",  "ishaque@gmail.com",  "987654"))
            myPendingsArrayList.add(HAData("6", "housing alter", "124",  "ishaqu@gmail.com",  "9876543"))
            myPendingsArrayList.add(HAData("12", "leave", "125",  "ishaq@gmail.com",  "98765432"))
            myPendingsArrayList.add(HAData("3", "coffee", "126",  "isha@gmail.com",  "987654321"))


            Utils.stopShimmerRL(shimmerLayout, rootLayout)
            setupRecyclerView()


        } else {
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
        }
    }

    fun onListItemClicked(dummyData:HAData, position: Int) {

    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        myPendingAdapter =
            PendingRequestsAdapter(myPendingsArrayList, this, this@PendingRequestsActivity)
        recyclerView.adapter = myPendingAdapter
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