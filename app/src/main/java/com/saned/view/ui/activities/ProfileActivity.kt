package com.saned.view.ui.activities

import android.annotation.SuppressLint
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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.saned.R
import com.saned.sanedApplication
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.error.SANEDError
import com.saned.view.service.ConnectivityReceiver
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivityWithResult
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.lang.Exception

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

            coroutineScope.launch {
                try {
                    val result = apiService.getProfileData().await()
                    Log.e("result", "" + result)
                    if (result.success == "1") {


                        // userPhone.text = "" + "${result.user!!.phone}" //${result.user!!.last_name}
                        // profileDetails.text =  "Last Login: " + Utils.convertDbtoNormalDateTime1("" + result.user!!.previous_login)
//                        profileDetails.text = "" + "${result.user!!.email}"


                        firstNameEditText2.text=result.data!!.fnme
                        lastNameEditText2.text=result.data!!.lnme
                        middleNameEditText2.text=result.data!!.mnme
                        arabicNameEditText2.text=result.data!!.a_name
                        dobEditText2.text=result.data!!.dob
                        nationalityEditText2.text=result.data!!.ccty
                        genderEditText2.text=result.data!!.gend
                        emailEditText2.text=result.data!!.email
                        phoneEditText2.text=result.data!!.phon
                        religionEditText2.text=result.data!!.relg
                        emergencyEditText2.text=result.data!!.emrcnt
                        empcodeEditText2.text=result.data!!.emp_code
                        fullNameEditText2.text=result.data!!.f_name
                        dojEditText2.text=result.data!!.join
                        jobTitleEditText2.text=result.data!!.jbtl
                        departmentEditText2.text=result.data!!.dept
                        basicEditText2.text=result.data!!.basic
                        housingEditText2.text=result.data!!.hous
                        managerEditText2.text=result.data!!.mngr
                        ibanEditText2.text=result.data!!.iban
                        idEditText2.text=result.data!!.idno
                        lastdateEditText2.text=result.data!!.ldate
                        vacationEditText2.text=result.data!!.days
                        gosiEditText2.text=result.data!!.gosi
                        cashEditText2.text=result.data!!.cash
                        gradeEditText2.text=result.data!!.grade
                        professionEditText2.text=result.data!!.prof
                        overEditText2.text=result.data!!.ovrt
                        idexpiryEditText2.text=result.data!!.idex
                        passEditText2.text=result.data!!.pspt
                        passexpiryEditText2.text=result.data!!.psptex
                        contractEditText2.text=result.data!!.cnttyp
                        gosinumEditText2.text=result.data!!.gosino
                        contractexpEditText2.text=result.data!!.cntrex
                        subDeptEditText2.text=result.data!!.subdep
                        projectEditText2.text=result.data!!.proj
                        idnumEditText2.text=result.data!!.id


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