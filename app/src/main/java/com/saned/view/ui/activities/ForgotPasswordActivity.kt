package com.saned.view.ui.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.saned.R
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.user_layout
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        init()
    }

    private fun init() {

        //listeners
        backToLogin.setOnClickListener {
            onBackPressed()
        }
        submitButton.setOnClickListener {
            user_layout.isErrorEnabled = false

            if (email_edit_text.text.toString() == "" || !Utils.isValidEmail(email_edit_text.text.toString())) {
                user_layout.error = "Enter Valid Email"
                user_layout.requestFocus()
                return@setOnClickListener
            }

            performResetPassword()
        }
    }

    private fun performResetPassword() {

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

            hashMap["email"] = "" + email_edit_text.text.toString()

            //for now
           // forgotSuccess()
//
            coroutineScope.launch {
                try {

                    val result = apiService.resetPassword(hashMap).await()

                    Log.e("resultonforgot", "" + result)

                    if (result.success == "1") {

                        Toast.makeText(this@ForgotPasswordActivity, "otp send successfully" + result.message, Toast.LENGTH_SHORT).show()
                        forgotSuccess()
                    } else {

                        if (result.message=="We can't find a user with that e-mail address."){
                            Toast.makeText(this@ForgotPasswordActivity, "Email address not found", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@ForgotPasswordActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }

                    progressDialog.dismiss()


                } catch (e: Exception) {

                    progressDialog.dismiss()
                    Log.e("errorTryCar", "" + e.message)

                }
            }

        } else {
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
        }

    }



    private  fun forgotSuccess(){
        openActivity(OTPActivity::class.java, this){
            putString("email", "" + email_edit_text.text.toString())
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