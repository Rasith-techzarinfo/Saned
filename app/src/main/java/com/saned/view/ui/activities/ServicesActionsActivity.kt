package com.saned.view.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.facebook.shimmer.ShimmerFrameLayout
import com.nchores.user.model.ServicesMenu
import com.saned.R
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.view.error.SANEDError
import com.saned.view.ui.activities.dynamicWF.HistoryDynamicWFActivity
import com.saned.view.ui.adapter.servicesActions.ServiceActionsAdapter
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.isInternetAvailable
import com.saned.view.utils.Utils.Companion.openActivity
import com.saned.view.utils.Utils.Companion.startShimmerRL
import com.saned.view.utils.Utils.Companion.stopShimmerRL
import kotlinx.coroutines.launch
import java.lang.Exception

class ServicesActionsActivity : AppCompatActivity(), ServiceActionsAdapter.ListAdapterListener {



    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.toolbar_title)
    lateinit var toolbarTitle: TextView

    @BindView(R.id.recycler_view)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.success_swipe_refresh)
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @BindView(R.id.shimmer_layout)
    lateinit var shimmerLayout: ShimmerFrameLayout

    @BindView(R.id.root_layout)
    lateinit var rootLayout: RelativeLayout

    @BindView(R.id.nested_scroll_view)
    lateinit var nestedScrollView: NestedScrollView

    @BindView(R.id.empty_nodata_view)
    lateinit var emptyView: LinearLayout

    lateinit var serviceActionsAdapter : ServiceActionsAdapter

    var servicesArrayList: ArrayList<ServicesMenu> = ArrayList()

    var currentPage: Int = 1
    var totalPages: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services_actions)
        ButterKnife.bind(this)
        setToolBar()
        init()
    }



    //clears and adds first 10 results
    private fun getServicesListFromServer(){
        servicesArrayList.clear()
        currentPage = 1

        if (isInternetAvailable(this)) {

            //shimmer
            startShimmerRL(shimmerLayout, rootLayout)

            coroutineScope.launch {

                try {

//                    val result = netwoApplication.apiService.getTicketFAQList(currentPage).await()
//                    Log.e("result", "" + result)
//
//                    if(result.success == "1"){
//
//                        for(item in result.data!!.data!!){
////                            Log.e("current page info",""+item.question)
//
//                            val c1 = TicketCategory(
//                                "" + item.category!!.id,
//                                "" + item.category!!.name
//                            )
//
//                            val v1 = ServicesMenu(
//                                ""+ item.id,
//                                c1,
//                                ""+item.question,
//                                ""+item.answer,
//                                ""+item.user_id,
//                                ""+item.active,
//                                ""+item.created_at,
//                                ""+item.updated_at
//                            )
//                            servicesArrayList.add(v1)
//                        }
//                        totalPages = result.data!!.last_page.toString()
//                        Log.e("result", "" + result.data!!.current_page)
//
//                    }else {
//
//                        Toast.makeText(this@ServicesActionsActivity, "" + result.message, Toast.LENGTH_SHORT)
//                            .show()
//                    }
//


                     //load static data for now
                     servicesArrayList.add(ServicesMenu("Housing Advance", "101"))
                     servicesArrayList.add(ServicesMenu("Leave Request", "101"))
        
        
        

                    stopShimmerRL(shimmerLayout, rootLayout)


                    setupRecyclerView()

                }catch(e: Exception){

                    stopShimmerRL(shimmerLayout, rootLayout)
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
//                            val v1 = ServicesMenu(
//                                ""+ item.id,
//                                c1,
//                                ""+item.question,
//                                ""+item.answer,
//                                ""+item.user_id,
//                                ""+item.active,
//                                ""+item.created_at,
//                                ""+item.updated_at
//                            )
//                            servicesArrayList.add(v1)
//                        }
//                        totalPages = result.data.last_page.toString()
//                        Log.e("result", "" + result.data.current_page)
//
//                    }else {
//
//                        Toast.makeText(this@ServicesActionsActivity, "" + result.message, Toast.LENGTH_SHORT)
//                            .show()
//                    }
//
//                    progressDialog.dismiss()
//                    //notify changes to adapter
//                    serviceActionsAdapter.notifyDataSetChanged()
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
        if (servicesArrayList.isEmpty()) {
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
            serviceActionsAdapter = ServiceActionsAdapter(servicesArrayList, this, this@ServicesActionsActivity)
            recyclerView.adapter = serviceActionsAdapter
            if (recyclerView.itemDecorationCount == 0) {
                recyclerView.addItemDecoration(
                    DividerItemDecoration(
                        this,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        }
    }



    override fun onListItemClicked(dummyData: ServicesMenu, position: Int) {

        //send form data to new activity
        openActivity(HistoryDynamicWFActivity::class.java, this@ServicesActionsActivity){
            putString("formID", "" + dummyData.id)
            putString("formName", "" + dummyData.title)
        }
    }









    private fun init() {
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