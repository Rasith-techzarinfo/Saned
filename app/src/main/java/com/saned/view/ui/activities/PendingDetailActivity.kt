package com.saned.view.ui.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.saned.R
import com.saned.model.PendingDetail
import com.saned.sanedApplication
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.view.error.SANEDError
import com.saned.view.ui.adapter.pendingDetail.PendingDetailAdapter
import com.saned.view.ui.adapter.pendingRequests.PendingRequestsAdapter
import com.saned.view.utils.Utils
import kotlinx.android.synthetic.main.activity_my_employees.*
import kotlinx.android.synthetic.main.activity_my_employees.emptyView
import kotlinx.android.synthetic.main.activity_my_employees.recyclerView
import kotlinx.android.synthetic.main.activity_my_employees.rootLayout
import kotlinx.android.synthetic.main.activity_my_employees.shimmerLayout
import kotlinx.android.synthetic.main.activity_my_employees.swipeRefreshLayout
import kotlinx.android.synthetic.main.activity_pending_detail.*
import kotlinx.android.synthetic.main.activity_view_dynamic_w_f.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.textColor
import java.lang.Exception

class PendingDetailActivity : AppCompatActivity() {

    var myPendingsDetailArrayList: ArrayList<PendingDetail> = ArrayList()
    lateinit var myPendingDetailAdapter: PendingDetailAdapter

    var wkid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_detail)

        setToolBar()
        init()
    }

    private fun init() {

        wkid = "" + intent.getStringExtra("wkid")

//        swipeRefreshLayout.setOnRefreshListener {
//            //getValues()
//            getDataFromServer()
//            swipeRefreshLayout.isRefreshing = false
//        }
        //get data
        //  getValues()
        getDataFromServer()
    }

    private fun getDataFromServer() {

        myPendingsDetailArrayList.clear()

        if (Utils.isInternetAvailable(this)) {

            Utils.startShimmerRL(shimmerLayout, rootLayout)
            emptyView.visibility = View.GONE

            coroutineScope.launch {
                try {
                    val result = apiService.getPendingHistoryDetail(wkid).await()
                    Log.e("result", "" + result)

                    if (result.success == "1") {


//                        var firstArrayList: ArrayList<HousingWFData> = ArrayList()
//                        var secondArrayList: ArrayList<HAData1> = ArrayList()

                        for (item in result.data!!) {

                            val v1 = PendingDetail(

                                "" + item.id,
                                "" + item.wkid,
                                "" + item.type,
                                "" + item.emno,
                                "" + item.date,
                                "" + item.time,
                                "" + item.step,
                                "" + item.stnm,
                                "" + item.nemn,
                                "" + item.ndat,
                                "" + item.ntim,
                                "" + item.emp_code,
                                "" + item.f_name,
                                "" + item.a_name,
                                "" + item.dept,
                                "" + item.jbtl,
                                "" + item.stat,
                                "" + item.join,
                                "" + item.dob,
                                "" + item.ccty,
                                "" + item.email,
                                "" + item.password,
                                "" + item.phon,
                                "" + item.mart,
                                "" + item.bank,
                                "" + item.city,
                                "" + item.loca,
                                "" + item.iban,
                                "" + item.mngr,
                                "" + item.basic,
                                "" + item.hous,
                                "" + item.tran,
                                "" + item.bnka,
                                "" + item.cont,
                                "" + item.medc,
                                "" + item.ldate,
                                "" + item.gend,
                                "" + item.grade,
                                "" + item.idno,
                                "" + item.relg,
                                "" + item.vacb,
                                "" + item.days,
                                "" + item.gosi,
                                "" + item.cash,
                                "" + item.refcntd,
                                "" + item.refcntu,
                                "" + item.fnme,
                                "" + item.lnme,
                                "" + item.mnme,
                                "" + item.prof,
                                "" + item.ovrt,
                                "" + item.idex,
                                "" + item.pspt,
                                "" + item.psptex,
                                "" + item.cnttyp,
                                "" + item.emrcnt,
                                "" + item.gosino,
                                "" + item.cntrex,
                                "" + item.subdep,
                                "" + item.proj,
                                "" + item.created_at,
                                "" + item.updated_at,
                                "" + item.deleted_at



                            )
                            myPendingsDetailArrayList.add(v1)

//                            var v2 = HAData1((firstArrayList.size - 1), "" + item.wkid)
//                            secondArrayList.add(v2)
                        }

//                        val hashMap: HashMap<String, MutableList<Int>> = HashMap()

                        // no profile for now
//                        if (result.user!!.profile_pic != null) {
//
//                            Glide.with(this@ProfileActivity).load(BASE_URL + result.user.profile_pic).placeholder(
//                                    R.drawable.ic_user
//                            ).into(profileImage)
//                        }


                    } else {
                        Toast.makeText(applicationContext, "" + result.message, Toast.LENGTH_SHORT).show()
                    }

                    Utils.stopShimmerRL(shimmerLayout, rootLayout)

                    setupRecyclerView()

                } catch (e: Exception) {
                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
                    Log.e("error", "" + e.message)
                    if (e is SANEDError) {
                        Log.e("Err", "" + e.getErrorResponse())
                        if (e.getResponseCode() == 401) {
                            Utils.logoutFromApp(applicationContext)
                        } else if (e.getResponseCode() == 500) {
                            Toast.makeText(applicationContext, "Server error", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }
            }
        } else {
//            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
            Utils.checkNetworkDialog(this, this) { getDataFromServer() }
        }
    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        myPendingDetailAdapter =
                PendingDetailAdapter(myPendingsDetailArrayList, this, this@PendingDetailActivity)
        recyclerView.adapter = myPendingDetailAdapter
    }

    private fun setToolBar() {
        setSupportActionBar(toolbarpending)
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