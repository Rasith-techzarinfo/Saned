package com.saned.view.ui.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.RadioButton
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
import com.saned.view.utils.Utils
import kotlinx.android.synthetic.main.activity_pending_detail.*
import kotlinx.android.synthetic.main.activity_view_dynamic_w_f.*
import kotlinx.android.synthetic.main.pending_detail_items.view.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.textColor
import java.lang.Exception

class PendingDetailActivity : AppCompatActivity() {

    var myPendingsDetailArrayList: ArrayList<PendingDetail> = ArrayList()
    lateinit var myPendingDetailAdapter: PendingDetailAdapter

    var wkid: String = ""
    var form_name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_detail)

        setToolBar()
        init()
    }

    private fun init() {

        wkid = "" + intent.getStringExtra("wkid")
        form_name = "" + intent.getStringExtra("form_name")

        Log.e("arjun", "this is id" +wkid)

//        swipeRefreshLayout.setOnRefreshListener {
//            //getValues()
//            getDataFromServer()
//            swipeRefreshLayout.isRefreshing = false
//        }
        //get data
        //  getValues()

        submitButton1.setOnClickListener {
            //validate
            val selectedId = radioGroup1.checkedRadioButtonId
            if (selectedId != -1) {
                val radioButtonValue = findViewById<RadioButton>(selectedId)
                Log.e("RadioValue", "" + radioButtonValue.text)

                var radVal = ""
                if (radioButtonValue.text == "Approve") {
                    radVal = "1"
                } else {
                    radVal = "2"
                }

                //data
                sendDataToServer(radVal)

            } else {
                Toast.makeText(applicationContext, "Select the Approve status", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

        }

        getDataFromServer()
    }

    private fun sendDataToServer(status: String) {

        Utils.hideKeyBoard(submitButton1, this)

        if (Utils.isInternetAvailable(this)) {


            // Custom Progress dialog
            val progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setContentView(R.layout.custom_progress_dialog_layout)
            progressDialog.setCancelable(false)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()



            val hashMap: java.util.HashMap<String, String> = java.util.HashMap()

            hashMap["form"] = "" + form_name
            hashMap["reason"] = "" + ""
            hashMap["status"] = "" + status
            hashMap["wkid"] = "" + wkid

            coroutineScope.launch {
                try {

//                    if(sanedApplication.prefHelper.getUserType() != "3") {
//                        val result = if (sanedApplication.prefHelper.getManagerLevel() == "1") apiService.verifyHAStatus1(hashMap).await() else apiService.verifyHAStatus2(hashMap).await()

                        val result = apiService.requestUpdate(hashMap).await()

                        Log.e("result", "" + result)

                        if (result.success == "1") {

                            //on success
                           // res = "true"
                            onBackPressed()
                            Toast.makeText(applicationContext, "Status Updated", Toast.LENGTH_SHORT).show()


                        } else {

                            Toast.makeText(applicationContext, "" + result.message, Toast.LENGTH_SHORT).show()
                        }
               //     }
                    progressDialog.dismiss()


                } catch (e: Exception) {

                    progressDialog.dismiss()
                    Log.e("error", "" + e.message)
                    if (e is SANEDError) {
                        Log.e("Err", "" + e.getErrorResponse())
                        if (e.getResponseCode() == 401) {
                            Utils.logoutFromApp(applicationContext)
                        } else if (e.getResponseCode() == 500) {
                            Toast.makeText(applicationContext, "Server error", Toast.LENGTH_LONG).show()
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
//            Toast.makeText(applicationContext, "No Internet Available", Toast.LENGTH_SHORT).show()
            Utils.checkNetworkDialog(this, this) { getDataFromServer() }
        }
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

//                        approvalStatusLayout1.visibility = View.VISIBLE
//                        verifyLayout1.visibility = View.VISIBLE



                        if(result.status == "Pending"){
                            statusTextView2.text = result.status
                            statusTextView2.textColor = Color.parseColor("#ffad46")

                        }else if (result.status == "Rejected"){
                            statusTextView2.text = result.status
                            statusTextView2.textColor = Color.parseColor("#ff0000")
                        }else{
                            statusTextView2.text = result.status
                            statusTextView2.textColor = Color.parseColor("#00aa00")
                        }


//                        labelVal15.setText("" + result.data!!.f_name)
//                        labelVal16.setText("" + result.data!!.emp_code)
//                        labelVal3.setText("" + result.data!!.join)
//                        labelVal4.setText("" + result.data!!.basic)
//                        labelVal5.setText("" + result.data!!.cnttyp)
//                        labelVal6.setText("" + result.data!!.bank)
//                        labelVal7.setText("" + result.data!!.a_name)
//                        labelVal8.setText("" + result.data!!.pspt)
//                        labelVal9.setText("" + result.data!!.email)
//                        labelVal10.setText("" + result.data!!.phon)
//                        labelVal11.setText("" + result.data!!.mart)
//                        labelVal12.setText("" + result.data!!.city)
//                        labelVal13.setText("" + result.data!!.gend)
//                        labelVal14.setText("" + result.data!!.relg)

//                        var firstArrayList: ArrayList<HousingWFData> = ArrayList()
//                        var secondArrayList: ArrayList<HAData1> = ArrayList()

                        for (item in result.data!!) {

                            val v1 = PendingDetail(

                                "" + item.id,
                                "" + item.wkid,
                                "" + item.sern,
                                "" + item.labl,
                                "" + item.data,
                                "" + item.form_name,
                                "" + item.email,
                                "" + item.added_by,
                                "" + item.added_at,
                                "" + item.t_idno,
                                "" + item.emp_name,
                                "" + item.t_emno,
                                "" + item.emp_code

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
                            )
                            myPendingsDetailArrayList.add(v1)
//
////                            var v2 = HAData1((firstArrayList.size - 1), "" + item.wkid)
////                            secondArrayList.add(v2)
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

                    Utils.stopShimmerRL(shimmerLayoutpending, rootLayoutpending)
                    verifyLayout1.visibility = if(sanedApplication.prefHelper.getUserType() == "HR Admin") View.VISIBLE else View.GONE
                    approvalStatusLayout1.visibility = View.VISIBLE
                    setupRecyclerView()

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





    private fun setupRecyclerView() {
        recyclerViewpending.layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewpending.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        myPendingDetailAdapter =
                PendingDetailAdapter(myPendingsDetailArrayList, this, this@PendingDetailActivity)
        recyclerViewpending.adapter = myPendingDetailAdapter
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