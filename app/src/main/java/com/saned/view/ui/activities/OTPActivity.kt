package com.saned.view.ui.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.saned.R
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivity
import kotlinx.android.synthetic.main.activity_o_t_p.*
import kotlinx.coroutines.launch

class OTPActivity : AppCompatActivity() {

    var emailString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_o_t_p)
        init()
    }

    private fun init() {

        //get intent
        emailString = "" + intent.getStringExtra("email")
        //listeners
        submitButton.setOnClickListener {
            OTPLayout.isErrorEnabled = false
            if (otpEditText.text.toString() == "") {
                OTPLayout.error = "Enter the OTP"
                return@setOnClickListener
            }

            submitOtp()
        }
    }




    private fun submitOtp() {
        Utils.hideKeyBoard(submitButton, this)

        if (Utils.isInternetAvailable(this)) {


            // Custom Progress dialog
            val progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setContentView(R.layout.custom_progress_dialog_layout)
            progressDialog.setCancelable(false)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()


            val hashMap: HashMap<String, String> = HashMap()

            hashMap["token"] = "" + otpEditText.text.toString()
            hashMap["email"] = "" + emailString

            //for now
            otpSuccess()

//            coroutineScope.launch {
//                try {
//
//                    val result = apiService.checkOTP(hashMap).await()
//
//                    Log.e("result", "" + result)
//
//                    if (result.success == "1") {
//
//                        prefHelper.setBearerToken("" + result.token)
//
//                        otpSuccess()
//                    } else {
//
//                        if (result.message=="Password reset token expired. Please try again"){
//                            Toast.makeText(this@OTPActivity, "" + result.message, Toast.LENGTH_LONG).show()
//                        }else{
//                            Toast.makeText(this@OTPActivity, "Incorrect OTP", Toast.LENGTH_SHORT).show()
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
//                    Toast.makeText(this@OTPActivity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
//
//                }
//            }

        } else {
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
        }

    }

    private fun otpSuccess(){
        openActivity(UpdatePasswordActivity::class.java, this){}
    }

    override fun onSupportNavigateUp(): Boolean {

        finishAfterTransition()
        return true
    }

    override fun onBackPressed() {

        finishAfterTransition()
    }
}