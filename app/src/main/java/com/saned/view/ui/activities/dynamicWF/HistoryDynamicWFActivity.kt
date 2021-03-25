package com.saned.view.ui.activities.dynamicWF

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.saned.R
import com.saned.model.HAData
import com.saned.model.HAData1
import com.saned.model.HousingWFData
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.error.SANEDError
import com.saned.view.ui.adapter.dynamicWF.DynamicWFHistoryAdapter
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivity
import kotlinx.android.synthetic.main.activity_history_dynamic_w_f.*
import kotlinx.coroutines.launch

class HistoryDynamicWFActivity : AppCompatActivity(), DynamicWFHistoryAdapter.ListAdapterListener {


    lateinit var dynamicWFHistoryAdapter : DynamicWFHistoryAdapter

    var dynamicWFArrayList: ArrayList<HAData> = ArrayList()

    var currentPage: Int = 1
    var totalPages: String = ""
    var formID: String = ""
    var formName: String = ""


    //dynamic form using fields
    //static for now
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_dynamic_w_f)
        setToolBar()
        init()
    }



    //clears and adds first 10 results
    private fun getServicesListFromServer(){
        dynamicWFArrayList.clear()
        currentPage = 1

        if (Utils.isInternetAvailable(this)) {

            //shimmer
            Utils.startShimmerRL(shimmerLayout, rootLayout)
            emptyView.visibility = View.GONE

            coroutineScope.launch {

                try {

                    val result = apiService.getHousingWFList().await()
                    Log.e("result", "" + result)

                    if(result.success == "1"){

                        var firstArrayList: ArrayList<HousingWFData> = ArrayList()
                        var secondArrayList: ArrayList<HAData1> = ArrayList()

                        for (item in result.data!!) {

                            val v1 = HousingWFData(
                                    "" + item.id,
                                    "" + item.wkid,
                                    "" + item.sern,
                                    "" + item.labl,
                                    "" + item.data,
                                    "" + item.form_name,
                                    "" + item.email
                            )
                            firstArrayList.add(v1)

                            var v2 = HAData1((firstArrayList.size - 1), "" + item.wkid)
                            secondArrayList.add(v2)
                        }

                        val hashMap: HashMap<String, MutableList<Int>> = HashMap()


                        for (i in 0 until secondArrayList.size) {
                            if (hashMap[secondArrayList[i].wkid] != null) {
                                var indexList = hashMap[secondArrayList.get(i).wkid]
                                indexList!!.add(i)
                                hashMap[secondArrayList.get(i).wkid] = indexList!!
                            } else {
                                var indexList: MutableList<Int> = ArrayList()
                                indexList.add(i)
                                hashMap[secondArrayList.get(i).wkid] = indexList
                            }
                        }
                        Log.e("HashMap", "" + hashMap.toString())

                        for (item in hashMap){

                            var indexList: MutableList<Int> = ArrayList()
                            indexList = item.value
                            Log.e("HashMap Item", "" + item.key + " " +  indexList)

                            var month = ""
                            var reason = ""
                            var wkid = ""
                            for (item in indexList){
                                var t1 = firstArrayList[item]
                                if(t1.labl == "Month No" ){
                                  month = t1.data
                                }
                                if(t1.labl == "Reason"){
                                    reason = t1.data
                                }
                                wkid = t1.wkid

                            }
                            val v2 = HAData(
                                            "" + month,
                                            "" + reason,
                                            "" + wkid
                                    )
                                    dynamicWFArrayList.add(v2)
                                    Log.e("WF", "" + v2)
                        }



//                        totalPages = result.data!!.last_page.toString()
//                        Log.e("result", "" + result.data!!.current_page)

                    }else {

                        Toast.makeText(this@HistoryDynamicWFActivity, "" + result.message, Toast.LENGTH_SHORT)
                            .show()
                    }



                    //load static data for now
//                    dynamicWFArrayList.add(HAData("30", "This is so coolllll.", "101"))




                    Utils.stopShimmerRL(shimmerLayout, rootLayout)


                    setupRecyclerView()

                }catch (e: Exception){

                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
                    Log.e("error", "" + e.message)
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
        } else {
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
        }

    }


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




    override fun onListItemClicked(dummyData: HAData, position: Int) {
        //send form data to new activity
        openActivity(ViewDynamicWFActivity::class.java, this){
            putString("formID", "" + formID)
            putString("formName", "" + formName)
        }
    }


    private fun init() {
        //get intent data
        formID = "" + intent.getStringExtra("formID")
        formName = "" + intent.getStringExtra("formName")
        Log.e("itt", "" + formID)
        toolbarTitle.text = formName + " History"

        //user permission, hide for manager
        add_WF_fab.visibility = if(prefHelper.getUserType() == "1") View.GONE else View.VISIBLE

        //fab
        add_WF_fab.setOnClickListener {
            //send form data to new activity
            openActivity(CreateDynamicWFActivity::class.java, this){
                putString("formID", "" + formID)
                putString("formName", "" + formName)
            }
        }
        //swipe
        swipeRefreshLayout.setOnRefreshListener {
            getServicesListFromServer()
            swipeRefreshLayout.isRefreshing = false
        }
        getServicesListFromServer()

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