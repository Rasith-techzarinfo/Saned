package com.saned.view.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.saned.R
import com.saned.sanedApplication
import com.saned.view.error.SANEDError
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivity
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.toolbar
import kotlinx.coroutines.launch
import java.lang.Exception

class EditProfileActivity : AppCompatActivity() {






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        setToolBar()
        init()
    }






    private fun init() {

        //get profile data
        getMyProfileData()

        //btn listeners
        submitButton.setOnClickListener {




        }
    }

    private fun getMyProfileData() {
        if (Utils.isInternetAvailable(this)) {

//            Utils.startShimmerRL(shimmerLayout, rootLayout)
//            emptyView.visibility = View.GONE

            sanedApplication.coroutineScope.launch {
                try {
                    val result = sanedApplication.apiService.getProfileData().await()
                    Log.e("result", "" + result)

                    if (result.success == "1") {


                        userName.text = "" + "${result.user!!.first_name} ${result.user!!.last_name}"
                        userEmail.text = "" + "${result.user!!.email}"
                        userPhone.text = "" + "${result.user!!.phone}"

                        // no profile for now
//                        if (result.user!!.profile_pic != null) {
//
//                            Glide.with(this@ProfileActivity).load(BASE_URL + result.user.profile_pic).placeholder(
//                                    R.drawable.ic_user
//                            ).into(profileImage)
//                        }


                    } else {
                        Toast.makeText(this@EditProfileActivity, "" + result.message, Toast.LENGTH_SHORT).show()
                    }

//                    Utils.stopShimmerRL(shimmerLayout, rootLayout)

                } catch (e: Exception) {
//                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
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
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
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