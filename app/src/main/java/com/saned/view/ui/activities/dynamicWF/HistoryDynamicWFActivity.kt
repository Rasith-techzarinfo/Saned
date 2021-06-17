package com.saned.view.ui.activities.dynamicWF

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.saned.R
import com.saned.model.*
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.view.error.SANEDError
import com.saned.view.ui.adapter.dynamicWF.DynamicWFHistoryAdapter
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivityWithResult
import kotlinx.android.synthetic.main.activity_history_dynamic_w_f.*
import kotlinx.android.synthetic.main.activity_history_dynamic_w_f.emptyView
import kotlinx.android.synthetic.main.activity_history_dynamic_w_f.recyclerView
import kotlinx.android.synthetic.main.activity_history_dynamic_w_f.rootLayout
import kotlinx.android.synthetic.main.activity_history_dynamic_w_f.shimmerLayout
import kotlinx.android.synthetic.main.activity_history_dynamic_w_f.swipeRefreshLayout
import kotlinx.android.synthetic.main.activity_history_dynamic_w_f.toolbar
import kotlinx.android.synthetic.main.activity_my_employees.*
import kotlinx.coroutines.launch

class HistoryDynamicWFActivity : AppCompatActivity(), DynamicWFHistoryAdapter.ListAdapterListener {


    lateinit var dynamicWFHistoryAdapter : DynamicWFHistoryAdapter

    var dynamicWFArrayList: ArrayList<ServDetail> = ArrayList()

    var currentPage: Int = 1
    var totalPages: String = ""
    var formID: String = ""
    var formName: String = ""

    var module_name: String = ""


    //dynamic form using fields
    //static for now
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_dynamic_w_f)
        setToolBar()
        init()
    }


    private fun init() {
        //get intent data
//        formID = "" + intent.getStringExtra("formID")
//        formName = "" + intent.getStringExtra("formName")
//        Log.e("itt", "" + formID)

        module_name = "" + intent.getStringExtra("module_name")

        toolbarTitle.text = module_name + " History"

        //user permission, hide for manager
       // add_WF_fab.visibility = if(prefHelper.getUserType() == "HR Admin") View.GONE else View.VISIBLE

        //fab
//        add_WF_fab.setOnClickListener {
//            //send form data to new activity
//            openActivityWithResult(CreateDynamicWFActivity::class.java, this, 101){
//                putString("formID", "" + formID)
//                putString("formName", "" + formName)
//            }
//        }
        //swipe
        swipeRefreshLayout.setOnRefreshListener {
            getPendingDetailFromServer()
            swipeRefreshLayout.isRefreshing = false
        }
        getPendingDetailFromServer()

    }

    private fun getPendingDetailFromServer() {
        dynamicWFArrayList.clear()
        currentPage = 1

        if (Utils.isInternetAvailable(this)) {

            Utils.startShimmerRL(shimmerLayout, rootLayout)

            coroutineScope.launch {

                try{

                    val result = apiService.getPendingServicesDetail(module_name).await()

                    Log.e("result", "" + result)

                    if(result.success == "1"){

//                        var firstArrayList: ArrayList<HousingWFData> = ArrayList()
//                        var secondArrayList: ArrayList<HAData1> = ArrayList()

                        for (item in result.data!!) {

                            val v1 = ServDetail(
                                    "" + item.id,
                                    "" + item.wkid,
                                    "" + item.sern,
                                    "" + item.labl,
                                    "" + item.data,
                                    "" + item.form_name,
                                    "" + item.email,
                                    "" + item.added_by,
                                    "" + item.added_at
                            )
                            dynamicWFArrayList.add(v1)

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

                        Toast.makeText(this@HistoryDynamicWFActivity, "" + result.message, Toast.LENGTH_SHORT)
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





























    //clears and adds first 10 results
//    private fun getServicesListFromServer(){
//        dynamicWFArrayList.clear()
//        currentPage = 1
//
//        if (Utils.isInternetAvailable(this)) {
//
//            //shimmer
//            Utils.startShimmerRL(shimmerLayout, rootLayout)
//            emptyView.visibility = View.GONE
//
//            coroutineScope.launch {
//
//                try {
//
//                    val result = if(prefHelper.getUserType() == "3") apiService.getHousingWFListUser().await() else if (prefHelper.getManagerLevel() == "1") apiService.getHousingWFListManager1().await() else apiService.getHousingWFListManager2().await()
//
//                    Log.e("result", "" + result)
//
//                    if(result.success == "1"){
//
//                        var firstArrayList: ArrayList<HousingWFData> = ArrayList()
//                        var secondArrayList: ArrayList<HAData1> = ArrayList()
//
//                        for (item in result.data!!) {
//
//                            val v1 = HousingWFData(
//                                    "" + item.id,
//                                    "" + item.wkid,
//                                    "" + item.sern,
//                                    "" + item.labl,
//                                    "" + item.data,
//                                    "" + item.form_name,
//                                    "" + item.email
//                            )
//                            firstArrayList.add(v1)
//
//                            var v2 = HAData1((firstArrayList.size - 1), "" + item.wkid)
//                            secondArrayList.add(v2)
//                        }
//
//                        val hashMap: HashMap<String, MutableList<Int>> = HashMap()
//
//
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
//
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
//                                    "" + month,
//                                    "" + reason,
//                                    "" + userID,
//                                    "" + document,
//                                    "" + wkid
//                                    )
//                                    dynamicWFArrayList.add(v2)
//                                    Log.e("WF", "" + v2)
//                        }
//
//
//
////                        totalPages = result.data!!.last_page.toString()
////                        Log.e("result", "" + result.data!!.current_page)
//
//                    }else {
//
//                        Toast.makeText(this@HistoryDynamicWFActivity, "" + result.message, Toast.LENGTH_SHORT)
//                            .show()
//                    }
//
//
//
//                    //load static data for now
////                    dynamicWFArrayList.add(HAData("30", "This is so coolllll.", "101"))
//
//
//
//
//                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
//
//
//                    setupRecyclerView()
//
//                }catch (e: Exception){
//
//                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
//                    Log.e("error", "" + e.message)
////                    if(e.message == "Connection reset" || e.message == "Failed to connect to /40.123.199.239:3000"){
////
////                    } else
//                        if (e is SANEDError) {
//                        Log.e("Err", "" + e.getErrorResponse())
//                        if (e.getResponseCode() == 401) {
//                            Utils.logoutFromApp(applicationContext)
//                        } else if (e.getResponseCode() == 500) {
//                            Toast.makeText(applicationContext, "Server error", Toast.LENGTH_LONG).show()
//                        }
//                    } else {
//                        Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//
//            }
//        } else {
//           // Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
//            Utils.checkNetworkDialog(this, this) { getServicesListFromServer() }
//        }
//
//    }


//    private fun loadMoreData(){
//        if (Utils.isInternetAvailable(this)) {
//
//            // Custom Progress dialog
//            val progressDialog = Dialog(this)
//            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            val customLayout = layoutInflater.inflate(R.layout.custom_progress_dialog_layout, null)
//            progressDialog.setContentView(customLayout)
//            val circularProgress = customLayout.findViewById<ProgressIndicator>(R.id.progressCircleIndeterminate)
//            //progress color
//            try {
//                circularProgress.indicatorColors =  intArrayOf( Color.parseColor("${netwoApplication.prefHelper.getCurrentPrimaryDarkColor()}") )
//            }catch (e: java.lang.Exception) {
//                Log.e("catch", "" + e.message)
//            }
//            progressDialog.setCancelable(false)
//            progressDialog.setCanceledOnTouchOutside(false)
//            progressDialog.show()
//
//            netwoApplication.coroutineScope.launch {
//
//                try {
//
//                    val result = netwoApplication.apiService.getTicketFAQList(currentPage).await()
//                    Log.e("result", "" + result)
//
//                    if(result.success == "1"){
//
//                        for(item in result.data!!.data!!){
//
//                            val c1 = TicketCategory(
//                                "" + item.category!!.id,
//                                "" + item.category!!.name
//                            )
//
//                            val v1 = HAData(
//                                ""+ item.id,
//                                c1,
//                                ""+item.question,
//                                ""+item.answer,
//                                ""+item.user_id,
//                                ""+item.active,
//                                ""+item.created_at,
//                                ""+item.updated_at
//                            )
//                            dynamicWFArrayList.add(v1)
//                        }
//                        totalPages = result.data.last_page.toString()
//                        Log.e("result", "" + result.data.current_page)
//
//                    }else {
//
//                        Toast.makeText(this@HistoryDynamicWFActivity, "" + result.message, Toast.LENGTH_SHORT)
//                            .show()
//                    }
//
//                    progressDialog.dismiss()
//                    //notify changes to adapter
//                    dynamicWFHistoryAdapter.notifyDataSetChanged()
//
//
//                }catch(e: Exception){
//
//                    progressDialog.dismiss()
//                    Log.e("error", "" + e.message)
//                    if (e is NETWOError) {
//                        Log.e("Err", "" + e.getErrorResponse())
//                        if (e.getResponseCode() == 401) {
//                            Utils.logoutFromApp(applicationContext)
//                        } else if (e.getResponseCode() == 500) {
//                            Toast.makeText(applicationContext, "Server error", Toast.LENGTH_LONG).show()
//                        }
//                    } else {
//                        Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//
//                progressDialog.dismiss()
//            }
//        } else {
//            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun setupRecyclerView() {
        if (dynamicWFArrayList.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE

        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE

            recyclerView.layoutManager = LinearLayoutManager(this)
            //disable nsv
            recyclerView.isNestedScrollingEnabled = false;
            //pagination code
            nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
                val view =
                    nestedScrollView.getChildAt(nestedScrollView.childCount - 1) as View
                val diff =
                    view.bottom - (nestedScrollView.height + nestedScrollView
                        .scrollY)
                if (diff == 0) {
//                        Log.e("result", "$currentPage $totalPages")
//                        if (currentPage < totalPages.toInt()) {
//                            currentPage += 1
//                            loadMoreSuccessManualData()
//                        }
                }
            }
            dynamicWFHistoryAdapter = DynamicWFHistoryAdapter(dynamicWFArrayList, this, this@HistoryDynamicWFActivity)
            recyclerView.adapter = dynamicWFHistoryAdapter
//            if (recyclerView.itemDecorationCount == 0) {
//                recyclerView.addItemDecoration(
//                    DividerItemDecoration(
//                        this,
//                        DividerItemDecoration.VERTICAL
//                    )
//                )
//            }
        }
    }




    override fun onListItemClicked(dummyData: ServDetail, position: Int) {
        //send form data to new activity
        openActivityWithResult(ViewDynamicWFActivity::class.java, this, 101){
            putString("formID", "" + formID)
            putString("formName", "" + formName)
            putString("wkid", "" + dummyData.id)
        }
    }

    //getting value from onbackpressed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {  //update
            if (resultCode == RESULT_OK) {
                var temp1 = data!!.getStringExtra("isUpdated")
                var temp2 = data!!.getStringExtra("isAdded")
                if(temp1 == "true" || temp2 == "true") {
                    getPendingDetailFromServer()
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