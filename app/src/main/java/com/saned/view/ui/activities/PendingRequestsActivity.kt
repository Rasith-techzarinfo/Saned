package com.saned.view.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saned.R
import com.saned.model.Data
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.view.error.SANEDError
import com.saned.view.ui.adapter.myEmployees.MyEmployeesAdapter
import com.saned.view.ui.adapter.pendingRequests.PendingRequestsAdapter
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivityWithResult
import kotlinx.android.synthetic.main.activity_my_employees.*
import kotlinx.android.synthetic.main.activity_pending_requests.*
import kotlinx.android.synthetic.main.activity_pending_requests.recyclerView
import kotlinx.android.synthetic.main.activity_pending_requests.rootLayout
import kotlinx.android.synthetic.main.activity_pending_requests.shimmerLayout
import kotlinx.android.synthetic.main.activity_pending_requests.toolbar
import kotlinx.coroutines.launch

class PendingRequestsActivity : AppCompatActivity() {


    var myPendingsArrayList: ArrayList<Data> = ArrayList()
    lateinit var myPendingAdapter: PendingRequestsAdapter

    var currentPage: Int = 1

    var wkid: String = ""
    var form_name: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_requests)
        //getValues()
        setToolBar()
        init()

    }

    private fun init() {

//        swipeRefreshLayoutforpending.setOnRefreshListener {
//            //getValues()
//            getPendingRequestFromServer()
//            swipeRefreshLayoutforpending.isRefreshing = false
//        }
        //get data
      //  getValues()
        getPendingRequestFromServer()

        add_WF_fab.setOnClickListener {
            openActivityWithResult(ServicesActionsActivity::class.java, this, 101){
                //putString("formID", "" + formID)
                //putString("formName", "" + formName)
            }
        }
    }

    private fun getPendingRequestFromServer() {
        myPendingsArrayList.clear()
        currentPage = 1

        if (Utils.isInternetAvailable(this)) {

            Utils.startShimmerRL(shimmerLayout, rootLayout)

            coroutineScope.launch {

                try{

                    val result = apiService.getPendingHistory().await()

                    Log.e("result", "" + result)

                    if(result.success == "1"){

//                        var firstArrayList: ArrayList<HousingWFData> = ArrayList()
//                        var secondArrayList: ArrayList<HAData1> = ArrayList()

                        for (item in result.data!!) {

                            val v1 = Data(
                                "" + item.wkid,
                                "" + item.form_name,
                                "" + item.pending_since,
                                "" + item.pending_with,
                                "" + item.added_by,
                                "" + item.added_at,
                                "" + item.profile,
                                    "" + item.job_title,
                                "" + item.last_action_date,
                                "" + item.status,
                                "" + item.reason

                            )
                            myPendingsArrayList.add(v1)

//                            var v2 = HAData1((firstArrayList.size - 1), "" + item.wkid)
//                            secondArrayList.add(v2)
                        }

//                        val hashMap: HashMap<String, MutableList<Int>> = HashMap()


//                        for (i in 0 until secondArrayList.size) {
//                            if (hashMap[secondArrayList[i].wkid] != null) {
//                                var indexList = hashMap[secondArrayList.get(i).wkid]
//                                indexList!!.add(i)
//                                hashMap[secondArrayList.get(i).wkid] = indexList!!
//                            } else {
//                                var indexList: MutableList<Int> = ArrayList()
//                                indexList.add(i)
//                                hashMap[secondArrayList.get(i).wkid] = indexList
//                            }
//                        }
//                        Log.e("HashMap", "" + hashMap.toString())

//                        for (item in hashMap){
//
//                            var indexList: MutableList<Int> = ArrayList()
//                            indexList = item.value
//                            Log.e("HashMap Item", "" + item.key + " " +  indexList)
//
//                            var month = ""
//                            var reason = ""
//                            var userID = ""
//                            var document = ""
//                            var wkid = ""
//                            for (item in indexList){
//                                var t1 = firstArrayList[item]
////                                if(t1.sern == "1" ){ //month no
////                                  month = t1.data
////                                }
////                                if(t1.sern == "2"){ //reason
////                                    reason = t1.data
////                                }
////                                if(t1.sern == "3"){ //user id
////                                    userID = t1.data
////                                }
////                                if(t1.sern == "4"){ //document
////                                    document = t1.data
////                                }
//                                if(t1.labl.equals("Month No",true)){ //month no
//                                    month = t1.data
//                                }
//                                if(t1.labl.equals("Reason",true)){ //reason
//                                    reason = t1.data
//                                }
//                                if(t1.labl.equals("User Id",true)){ //user id
//                                    userID = t1.data
//                                }
//                                if(t1.labl.equals("Document",true)){ //document
//                                    document = t1.data
//                                }
//                                wkid = t1.wkid
//
//                            }
//                            val v2 = HAData(
//                                "" + month,
//                                "" + reason,
//                                "" + userID,
//                                "" + document,
//                                "" + wkid
//                            )
//                            myPendingsArrayList.add(v2)
//                            Log.e("WF", "" + v2)
//                        }



//                        totalPages = result.data!!.last_page.toString()
//                        Log.e("result", "" + result.data!!.current_page)

                    }else {

                        Toast.makeText(this@PendingRequestsActivity, "" + result.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    Utils.stopShimmerRL(shimmerLayout, rootLayout)


                    setupRecyclerView()


                }catch (e: Exception){

                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
                    Log.e("error", "" + e.message)
//                    if(e.message == "Connection reset" || e.message == "Failed to connect to /40.123.199.239:3000"){
//
//                    } else
                    if (e is SANEDError) {
                        Log.e("Err", "" + e.getErrorResponse())
                        if (e.getResponseCode() == 401) {
                            Utils.logoutFromApp(applicationContext)
                        } else if (e.getResponseCode() == 500) {
                            Toast.makeText(applicationContext, "Server error", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

            }
        }

    }


//    private fun getValues() {
//        myPendingsArrayList.clear()
//        currentPage = 1
//
//        if (Utils.isInternetAvailable(this)) {
//
//            Utils.startShimmerRL(shimmerLayout, rootLayout)
//
//            //add dummy values
////            myPendingsArrayList.add(HAData("3", "vacation", "123",  "ishaque@gmail.com",  "987654"))
////            myPendingsArrayList.add(HAData("6", "housing alter", "124",  "ishaqu@gmail.com",  "9876543"))
////            myPendingsArrayList.add(HAData("12", "leave", "125",  "ishaq@gmail.com",  "98765432"))
////            myPendingsArrayList.add(HAData("3", "coffee", "126",  "isha@gmail.com",  "987654321"))
//
//
//            Utils.stopShimmerRL(shimmerLayout, rootLayout)
//            setupRecyclerView()
//
//
//        } else {
//            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
//        }
//    }

    fun onListItemClicked(dummyData:Data, position: Int) {

        openActivityWithResult(PendingDetailActivity::class.java, this, 101){
//            putString("t_mail", "" + dummyData.t_mail)
//            putString("t_nama", "" + dummyData.t_nama)
             putString("wkid", "" + dummyData.wkid)
             putString("form_name", "" + dummyData.form_name)
        }


    }

    private fun setupRecyclerView() {
        if(myPendingsArrayList.isEmpty()) {
            recyclerView.layoutManager = LinearLayoutManager(applicationContext)
            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )
            myPendingAdapter =
                PendingRequestsAdapter(myPendingsArrayList, this, this@PendingRequestsActivity)
            recyclerView.adapter = myPendingAdapter
        }else {
            val linearLayoutManager = LinearLayoutManager(this)
            linearLayoutManager.reverseLayout = true
            recyclerView.layoutManager = linearLayoutManager

            myPendingAdapter =
                PendingRequestsAdapter(myPendingsArrayList, this, this@PendingRequestsActivity)
            recyclerView.adapter = myPendingAdapter

            //pagination code
            var loading = true
            var pastVisiblesItems: Int
            var visibleItemCount: Int
            var totalItemCount: Int
            //first visible item value 0 - top of recyelrview (it called everytime once loaded)

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    //scroll control
//                    if (dy > 0) {
//                        Log.e("tap", "Last Item !")
//                        //   composeFabText!!.visibility = View.GONE
//                        Utils.hideKeyBoard(rootLayout,this@ChatSearchUserActivity)
//                    } else if(dy == dx) {
//                        Log.e("tap", "Equal !")
//                        Utils.hideKeyBoard(rootLayout,this@ChatSearchUserActivity)
//                    } else {
//                        Log.e("tap", "else !")
//                        Utils.hideKeyBoard(rootLayout,this@ChatSearchUserActivity)
//                    }


                    //load more
                    var firstVisiblesItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    var lastvisisbleitem = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    if(lastvisisbleitem == myPendingsArrayList.size - 1){
//                        Log.e("tap1", "TOP! ${chatSearchList.size - 1}")
                    }
                }

            })



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