package com.saned.view.ui.activities

import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.saned.R
import com.saned.model.Empdata
import com.saned.model.Profile
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.error.SANEDError
import com.saned.view.service.ConnectivityReceiver
import com.saned.view.ui.adapter.myEmployees.MyEmployeesAdapter
import com.saned.view.ui.adapter.pendingRequests.PendingRequestsAdapter
import com.saned.view.ui.adapter.profileView.ProfileviewAdapter
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivityWithResult
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_my_employees.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.recyclerView
import kotlinx.android.synthetic.main.activity_profile.rootLayout
import kotlinx.android.synthetic.main.activity_profile.shimmerLayout
import kotlinx.android.synthetic.main.activity_profile.toolbar
import kotlinx.android.synthetic.main.profile_view_layout.*
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {


    private var networkDialog : Dialog? = null

    var myProfileArrayList: ArrayList<Profile> = ArrayList()
    lateinit var myProfileAdapter: ProfileviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setToolBar()
        init()
    }







    private fun init() {
        //network receiver
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        //edit profile
//        profile_edit_icon.setOnClickListener {
//            openActivityWithResult(EditProfileActivity::class.java, this, 101){}
//        }
        //get values
       // getMyProfileData()


        //get data
        //  getValues()
        getServicesListFromServer()

    }

//    private fun getMyProfileData() {
//        if (Utils.isInternetAvailable(this)) {
//
//            Utils.startShimmerRL(shimmerLayout, rootLayout)
//            emptyView.visibility = View.GONE
//
//            coroutineScope.launch {
//                try {
//                    val result = apiService.getProfileData().await()
//                    Log.e("result", "" + result)
//
//                    if (result.success == "1") {
//
//
//                        firstNameEditText2.text = "" + "${result.user!!.fnme} "  //${result.user!!.last_name}
//                        userEmail.text = "" + "${result.user!!.t_mail}"
//                       // userPhone.text = "" + "${result.user!!.phone}"
//                        profileName.text = "" + "${result.user!!.t_nama} "  //${result.user!!.last_name}
//                       // profileDetails.text =  "Last Login: " + Utils.convertDbtoNormalDateTime1("" + result.user!!.previous_login)
////                        profileDetails.text = "" + "${result.user!!.email}"
//
//                        //save to pref
//                        prefHelper.setUserName("" + "${result.user!!.} ") //${result.user!!.last_name}
//                        prefHelper.setUserEmail("" + result.user!!.t_mail)
//                       // prefHelper.setLastLogin("" + result.user!!.previous_login)
//
//                        //listeners
//                        userEmail.setOnClickListener {
//                            userEmail.isSelected = true
//                        }
//                        userName.setOnClickListener {
//                            userName.isSelected = true
//                        }
////                        userPhone.setOnClickListener {
////                            userPhone.isSelected = true
////                        }
//
//
//                        // no profile for now
////                        if (result.user!!.profile_pic != null) {
////
////                            Glide.with(this@ProfileActivity).load(BASE_URL + result.user.profile_pic).placeholder(
////                                    R.drawable.ic_user
////                            ).into(profileImage)
////                        }
//
//
//                    } else {
//                        Toast.makeText(this@ProfileActivity, "" + result.message, Toast.LENGTH_SHORT).show()
//                    }
//
//                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
//
//                } catch (e: Exception) {
//                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
//                    Log.e("error", "" + e.message)
//                    if (e is SANEDError) {
//                        Log.e("Err", "" + e.getErrorResponse())
//                        if (e.getResponseCode() == 401) {
//                            Utils.logoutFromApp(applicationContext)
//                        } else if (e.getResponseCode() == 500) {
//                            Toast.makeText(applicationContext, "Server", Toast.LENGTH_LONG)
//                                    .show()
//                        }
//                    } else {
//                        Toast.makeText(
//                                applicationContext,
//                                "Something went wrong",
//                                Toast.LENGTH_SHORT
//                        )
//                                .show()
//                    }
//
//                }
//            }
//        } else {
////            Toast.makeText(this@ProfileActivity, "No Internet Available", Toast.LENGTH_SHORT).show()
//            Utils.checkNetworkDialog(this, this) { getMyProfileData() }
//        }
//    }


    private fun getServicesListFromServer(){

        myProfileArrayList.clear()
                //currentPage = 1

        if (Utils.isInternetAvailable(this)) {

            //shimmer
            Utils.startShimmerRL(shimmerLayout, rootLayout)

            coroutineScope.launch {

                try {


                    val result = apiService.getProfileData().await()

                    Log.e("result", "" + result)

                    if(result.success == "1"){

                        // var myEmployeesArrayList: ArrayList<Empdata> = ArrayList()
                        //  var secondArrayList: ArrayList<HAData1> = ArrayList()

                        for (item in result.user!!) {

                            val v1 = Profile(
                                    "" + item.sno,
                                    "" + item.id,
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
                                    "" + item.deleted_at,
                                    "" + item.medical_class,
                                    "" + item.department_name,
                                    "" + item.sub_name,
                                    "" + item.job_title,
                                    "" + item.grade_name,
                                    "" + item.location_name




                            )
                            myProfileArrayList.add(v1)

                            //firstNameEditText2.text = "" + "${result.user!!.fnme}
                            //  var v2 = HAData1((firstArrayList.size - 1), "" + item.wkid)
                            //   secondArrayList.add(v2)
                        }



//                        totalPages = result.data!!.last_page.toString()
//                        Log.e("result", "" + result.data!!.current_page)

                    }else {

                        Toast.makeText(this@ProfileActivity, "" + result.message, Toast.LENGTH_SHORT)
                                .show()
                    }



                    //load static data for now
//                    dynamicWFArrayList.add(HAData("30", "This is so coolllll.", "101"))




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
        } else {
            // Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
            Utils.checkNetworkDialog(this, this) { getServicesListFromServer() }
        }

    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        myProfileAdapter =
                ProfileviewAdapter(myProfileArrayList, this, this@ProfileActivity)
        recyclerView.adapter = myProfileAdapter
    }




    //getting value from onbackpressed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {  //editprofile
            if (resultCode == RESULT_OK) {
                var temp1 = data!!.getStringExtra("isUpdated")
                if(temp1 == "true") {
                   // getMyProfileData()
                    getServicesListFromServer()
                }
            }
        }
    }




    override fun onNetworkConnectionChanged(isConnected: Boolean) {
//        showNetworkMessage(isConnected)
    }

    private fun showNetworkMessage(isConnected: Boolean) {
//        Log.e("connectionChange", "" + isConnected)

        if(!isConnected) {
            networkDialog = Dialog(this)
            networkDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            networkDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            networkDialog?.setContentView(R.layout.no_internet_layout)
            networkDialog?.setCancelable(false)
            val okayButton = networkDialog!!.findViewById(R.id.okayButton) as MaterialButton
            okayButton.setOnClickListener {
                if (isConnected) {
                    networkDialog?.dismiss()
                }
            }
            if(!isFinishing) {
                networkDialog?.show()
            }
        } else {
            networkDialog?.dismiss()
            //get data here
           // getMyProfileData()
            getServicesListFromServer()
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
//        //for now, make result code
//        updateProfileData()
    }

    override fun onDestroy() {
        super.onDestroy()
        ConnectivityReceiver.connectivityReceiverListener = null
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