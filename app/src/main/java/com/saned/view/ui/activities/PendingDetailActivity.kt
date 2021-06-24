package com.saned.view.ui.activities

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
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.view.error.SANEDError
import com.saned.view.ui.adapter.pendingDetail.PendingDetailAdapter
import com.saned.view.utils.Utils
import kotlinx.android.synthetic.main.activity_pending_detail.*
import kotlinx.android.synthetic.main.pending_detail_items.view.*
import kotlinx.coroutines.launch
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

        Log.e("arjun", "this is id" +wkid)

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

            Utils.startShimmerRL(shimmerLayoutpending, rootLayoutpending)
            emptyViewpending.visibility = View.GONE

            coroutineScope.launch {
                try {
                    val result = apiService.getPendingHistoryDetail(wkid).await()
                    Log.e("result", "" + result)

                    if (result.success == "1") {


                        labelVal15.setText("" + result.data!!.f_name)
                        labelVal16.setText("" + result.data!!.emp_code)
                        labelVal3.setText("" + result.data!!.join)
                        labelVal4.setText("" + result.data!!.basic)
                        labelVal5.setText("" + result.data!!.cnttyp)
                        labelVal6.setText("" + result.data!!.bank)
                        labelVal7.setText("" + result.data!!.a_name)
                        labelVal8.setText("" + result.data!!.pspt)
                        labelVal9.setText("" + result.data!!.email)
                        labelVal10.setText("" + result.data!!.phon)
                        labelVal11.setText("" + result.data!!.mart)
                        labelVal12.setText("" + result.data!!.city)
                        labelVal13.setText("" + result.data!!.gend)
                        labelVal14.setText("" + result.data!!.relg)

//                        var firstArrayList: ArrayList<HousingWFData> = ArrayList()
//                        var secondArrayList: ArrayList<HAData1> = ArrayList()

//                        for (item in result.data!!) {
//
//                            val v1 = PendingDetail(
//
//                                "" + item.id,
//                                "" + item.wkid,
//                                "" + item.type,
//                                "" + item.emno,
//                                "" + item.date,
//                                "" + item.time,
//                                "" + item.step,
//                                "" + item.stnm,
//                                "" + item.nemn,
//                                "" + item.ndat,
//                                "" + item.ntim,
//                                "" + item.emp_code,
//                                "" + item.f_name,
//                                "" + item.a_name,
//                                "" + item.dept,
//                                "" + item.jbtl,
//                                "" + item.stat,
//                                "" + item.join,
//                                "" + item.dob,
//                                "" + item.ccty,
//                                "" + item.email,
//                                "" + item.password,
//                                "" + item.phon,
//                                "" + item.mart,
//                                "" + item.bank,
//                                "" + item.city,
//                                "" + item.loca,
//                                "" + item.iban,
//                                "" + item.mngr,
//                                "" + item.basic,
//                                "" + item.hous,
//                                "" + item.tran,
//                                "" + item.bnka,
//                                "" + item.cont,
//                                "" + item.medc,
//                                "" + item.ldate,
//                                "" + item.gend,
//                                "" + item.grade,
//                                "" + item.idno,
//                                "" + item.relg,
//                                "" + item.vacb,
//                                "" + item.days,
//                                "" + item.gosi,
//                                "" + item.cash,
//                                "" + item.refcntd,
//                                "" + item.refcntu,
//                                "" + item.fnme,
//                                "" + item.lnme,
//                                "" + item.mnme,
//                                "" + item.prof,
//                                "" + item.ovrt,
//                                "" + item.idex,
//                                "" + item.pspt,
//                                "" + item.psptex,
//                                "" + item.cnttyp,
//                                "" + item.emrcnt,
//                                "" + item.gosino,
//                                "" + item.cntrex,
//                                "" + item.subdep,
//                                "" + item.proj,
//                                "" + item.created_at,
//                                "" + item.updated_at,
//                                "" + item.deleted_at
//
//
//
//                            )
//                            myPendingsDetailArrayList.add(v1)
//
////                            var v2 = HAData1((firstArrayList.size - 1), "" + item.wkid)
////                            secondArrayList.add(v2)
//                        }

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

                    Utils.stopShimmerRL(shimmerLayoutpending, rootLayoutpending)

                   // setupRecyclerView()

                } catch (e: Exception) {
                    Utils.stopShimmerRL(shimmerLayoutpending, rootLayoutpending)
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


//    private fun setupRecyclerView() {
//        recyclerViewpending.layoutManager = LinearLayoutManager(applicationContext)
//        recyclerViewpending.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//        myPendingDetailAdapter =
//                PendingDetailAdapter(myPendingsDetailArrayList, this, this@PendingDetailActivity)
//        recyclerViewpending.adapter = myPendingDetailAdapter
//    }

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