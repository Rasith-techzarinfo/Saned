package com.saned.view.ui.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat.finishAfterTransition
import com.bumptech.glide.Glide
import com.saned.R
import com.saned.sanedApplication
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.error.SANEDError
import com.saned.view.ui.activities.dynamicWF.HistoryDynamicWFActivity
import com.saned.view.utils.Constants.Companion.BASE_URL
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setToolBar()
        init()
    }







    private fun init() {

        //edit profile
        profile_edit_icon.setOnClickListener {
            openActivity(EditProfileActivity::class.java, this){}
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

                    if (result.success == "1") {


                        userName.text = "" + "${result.user!!.first_name} ${result.user!!.last_name}"
                        userEmail.text = "" + "${result.user!!.email}"
                        userPhone.text = "" + "${result.user!!.phone}"
                        profileName.text = "" + "${result.user!!.first_name} ${result.user!!.last_name}"
                        profileDetails.text =  "last login: " + Utils.convertDbtoNormalDateTime("" + result.user!!.previous_login)
//                        profileDetails.text = "" + "${result.user!!.email}"

                        //save to pref
                        prefHelper.setUserName("" + "${result.user!!.first_name} ${result.user!!.last_name}")
                        prefHelper.setUserEmail("" + result.user!!.email)
                        prefHelper.setLastLogin("" + result.user!!.previous_login)

                        //listeners
                        userEmail.setOnClickListener {
                            userEmail.isSelected = true
                        }
                        userName.setOnClickListener {
                            userName.isSelected = true
                        }
                        userPhone.setOnClickListener {
                            userPhone.isSelected = true
                        }


                        // no profile for now
//                        if (result.user!!.profile_pic != null) {
//
//                            Glide.with(this@ProfileActivity).load(BASE_URL + result.user.profile_pic).placeholder(
//                                    R.drawable.ic_user
//                            ).into(profileImage)
//                        }


                    } else {
                        Toast.makeText(this@ProfileActivity, "" + result.message, Toast.LENGTH_SHORT).show()
                    }

                    Utils.stopShimmerRL(shimmerLayout, rootLayout)

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
            Toast.makeText(this@ProfileActivity, "No Internet Available", Toast.LENGTH_SHORT).show()
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