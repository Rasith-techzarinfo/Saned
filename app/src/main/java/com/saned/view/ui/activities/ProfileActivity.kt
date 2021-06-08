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
import com.google.android.material.button.MaterialButton
import com.saned.R
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.error.SANEDError
import com.saned.view.service.ConnectivityReceiver
import com.saned.view.utils.Constants
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivityWithResult
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_spinner_list.view.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.HashMap

class ProfileActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {


    private var networkDialog : Dialog? = null


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
        profile_edit_icon.setOnClickListener {
            openActivityWithResult(EditProfileActivity::class.java, this, 101){}
        }
        //get values
        getMyProfileData()

    }

    private fun getMyProfileData() {
        if (Utils.isInternetAvailable(this)) {

            Utils.startShimmerRL(shimmerLayout, rootLayout)
            emptyView.visibility = View.GONE

            coroutineScope.launch {
                try {
                    val result = apiService.getProfileData().await()
                    Log.e("result", "" + result)

                    Utils.stopShimmerRL(shimmerLayout, rootLayout)

                } catch (e: Exception) {
                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
                    Log.e("error", "" + e.message)
                    if (e is SANEDError) {
                        Log.e("Err", "" + e.getErrorResponse())
                        if (e.getResponseCode() == 401) {
                            Utils.logoutFromApp(applicationContext)
                        } else if (e.getResponseCode() == 500) {
                            Toast.makeText(applicationContext, "Server", Toast.LENGTH_LONG)
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
//            Toast.makeText(this@ProfileActivity, "No Internet Available", Toast.LENGTH_SHORT).show()
            Utils.checkNetworkDialog(this, this) { getMyProfileData() }
        }
    }

    //getting value from onbackpressed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {  //editprofile
            if (resultCode == RESULT_OK) {
                var temp1 = data!!.getStringExtra("isUpdated")
                if(temp1 == "true") {
                    getMyProfileData()
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
            getMyProfileData()
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