package com.saned.view.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.saned.R
import com.saned.model.NotifyData
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.ui.activities.dynamicWF.ViewDynamicWFActivity
import com.saned.view.ui.adapter.notification.NotificationAdapter
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity(), NotificationAdapter.ListAdapterListener {


    var notificationArrayList: ArrayList<NotifyData> = ArrayList()
    lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
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
        notificationArrayList.clear()

        if (Utils.isInternetAvailable(this)) {
            Utils.startShimmerRL(shimmerLayout, rootLayout)

            //add dummy values
            notificationArrayList.add(NotifyData("3", "0", "Leave Request", "Your request has been accepted by Andy Rubin", "Just now", "1", "12", "Ishaque", "https://content.fortune.com/wp-content/uploads/2018/10/gettyimages-524263730-e1540503436210.jpg"))
            notificationArrayList.add(NotifyData("2", "1", "Leave Request", "Your request has been rejected by Andy Rubin", "1 min ago", "1", "11", "Ishaque", "https://content.fortune.com/wp-content/uploads/2018/10/gettyimages-524263730-e1540503436210.jpg"))
            notificationArrayList.add(NotifyData("1", "0", "Housing Advance", "Your request has been accepted by Ishaque", "2 min ago", "1", "10", "Ishaque", "https://content.fortune.com/wp-content/uploads/2018/10/gettyimages-524263730-e1540503436210.jpg"))



            Utils.stopShimmerRL(shimmerLayout, rootLayout)
            setupRecyclerView()


        } else {
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
        }
    }




    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        notificationAdapter =
            NotificationAdapter(notificationArrayList, this, this@NotificationActivity)
        recyclerView.adapter = notificationAdapter

    }


    override fun onListItemClicked(dummyData: NotifyData, position: Int) {
        Log.e("NotifyID", "" + dummyData.id)

//        if (dummyData.readStatus == "0") {
//            changeNotificationStatus(dummyData, position)
//        }

        if(prefHelper.getUserType() == "1" && prefHelper.getUserType() == "2") {
            if (dummyData.type == "1") {

                //static formid, name for now
                openActivity(ViewDynamicWFActivity::class.java, this){
                    putString("formID", "" + "101")
                    putString("formName", "" + "Housing Advance")
                    putString("wkid", "" + dummyData.wkid)
                }
            }
        }


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