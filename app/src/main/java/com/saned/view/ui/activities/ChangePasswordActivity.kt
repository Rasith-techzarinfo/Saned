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
import android.view.Window
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.saned.R
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.view.error.SANEDError
import com.saned.view.service.ConnectivityReceiver
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.isInternetAvailable
import com.saned.view.utils.Utils.Companion.isValidPassword
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.coroutines.launch

class ChangePasswordActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var networkDialog : Dialog? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        setToolBar()
        init()
    }


    private fun init() {
        //network receiver
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        //click listeners
        submitButton.setOnClickListener {
            currentPasswordLayout.isErrorEnabled = false
            newPasswordLayout.isErrorEnabled = false
            confirmPasswordLayout.isErrorEnabled = false

            if (currentPasswordEditText.text.toString().equals("") || !isValidPassword(currentPasswordEditText.text.toString()) ) {
                currentPasswordLayout.error = "Password must contain 1 Upper case, 1 lower case ,1 special character and 1 number"
                currentPasswordLayout.requestFocus()
                return@setOnClickListener
            }


            if (newPasswordEditText.text.toString().equals("") || !isValidPassword(newPasswordEditText.text.toString())) {
                newPasswordLayout.error = "Password must contain 1 Upper case, 1 lower case ,1 special character and 1 number"
                newPasswordLayout.requestFocus()
                return@setOnClickListener
            }

            if (confirmPasswordEditText.text.toString().equals("") || !isValidPassword(confirmPasswordEditText.text.toString())) {
                confirmPasswordLayout.error = "Password must contain 1 Upper case, 1 lower case ,1 special character and 1 number"
                confirmPasswordLayout.requestFocus()
                return@setOnClickListener
            }

            if (newPasswordEditText.text.toString() != confirmPasswordEditText.text.toString()) {
                confirmPasswordLayout.error = "New password and confirm password mismatch"
                confirmPasswordLayout.requestFocus()
                return@setOnClickListener
            }

            updatePasswordToServer()
        }
    }


    private fun updatePasswordToServer() {


        if (Utils.isInternetAvailable(this@ChangePasswordActivity)) {


            val hashMap: HashMap<String, String> = HashMap()
            hashMap["current_password"] = "" + currentPasswordEditText.text.toString()
            hashMap["password"] = "" + newPasswordEditText.text.toString()
            hashMap["password_confirmation"] = "" + confirmPasswordEditText.text.toString()

            // Custom Progress dialog
            val progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setContentView(R.layout.custom_progress_dialog_layout)
            progressDialog.setCancelable(false)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

//            coroutineScope.launch {
//                try {
//
//                    val result = apiService.changePassword(hashMap).await()
//
//                    Log.e("result", "" + result)
//
//                    if (result.success == "1") {
//
//
//                        Toast.makeText(this@ChangePasswordActivity, "" + result.message, Toast.LENGTH_SHORT).show()
//                        val  intents=   Intent(applicationContext, DashboardActivity::class.java)
//                        intents.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK and  Intent.FLAG_ACTIVITY_NEW_TASK
//                        startActivity(intents)
//                        overridePendingTransition(
//                                R.anim.enter_right_to_left,
//                                R.anim.exit_left_to_right
//                        )
//                    } else {
//
//                        if (result.message != "Kindly check all the fields") {
//                            Toast.makeText(
//                                    this@ChangePasswordActivity,
//                                    "" + result.message,
//                                    Toast.LENGTH_SHORT
//                            )
//                                    .show()
//                        } else {
//
//                            Toast.makeText(
//                                    this@ChangePasswordActivity,
//                                    "" + result.errors!!.password?.get(0),
//                                    Toast.LENGTH_SHORT
//                            )
//                                    .show()
//                        }
//                    }
//
//                    progressDialog.dismiss()
//
//
//                } catch (e: Exception) {
//
//                    progressDialog.dismiss()
//                    Log.e("errorTryCar", "" + e.message)
//
//                    if (e is SANEDError) {
//                        Log.e("Netwo", "" + e.getErrorResponse())
//                        Log.e("Netwo", "" + e.getResponseCode())
//                        if (e.getResponseCode() == 401) {
//                            Utils.logoutFromApp(applicationContext)
//                        } else if (e.getResponseCode() == 500) {
//                            Toast.makeText(
//                                    this@ChangePasswordActivity,
//                                    "Server error",
//                                    Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//                }
//            }


        } else {

            Toast.makeText(this, "No Internet Available", Toast.LENGTH_LONG).show()
//            Snackbar.make(rootLayout, "No Internet Available ", Snackbar.LENGTH_LONG)
//                    .setAction("Retry"){
//                        updatePasswordToServer()
//                    }
//                    .show()
        }

    }



    override fun onNetworkConnectionChanged(isConnected: Boolean) {
      //  showNetworkMessage(isConnected)
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
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
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