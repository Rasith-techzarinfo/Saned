package com.saned.view.ui.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.saned.R
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivityWithFlag
import kotlinx.android.synthetic.main.activity_update_password.*

class UpdatePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)
        init()
    }


    private fun init() {

        //listeners
        submitButton.setOnClickListener {
            newPasswordLayout.isErrorEnabled = false
            confirmPasswordLayout.isErrorEnabled = false

            if (newPasswordEditText.text.toString().equals("") || !Utils.isValidPassword( newPasswordEditText.text.toString())){
                newPasswordLayout.error = "Password must contain 1 Upper case, 1 lower case ,1 special character and 1 number"
                newPasswordLayout.requestFocus()
                return@setOnClickListener
            }

            if (confirmPasswordEditText.text.toString().equals("") || !Utils.isValidPassword(confirmPasswordEditText.text.toString())) {
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


        Utils.hideKeyBoard(submitButton, this@UpdatePasswordActivity)


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

            hashMap["password"] = "" + newPasswordEditText.text.toString()
            hashMap["password_confirmation"] = "" + confirmPasswordEditText.text.toString()

            //for now
            updateSuccess()

//            coroutineScope.launch {
//                try {
//
//                    val result = apiService.updatePassword(hashMap).await()
//
//                    Log.e("result", "" + result)
//
//                    if (result.success == "1") {
//                        Toast.makeText(this@UpdatePasswordActivity, "" + result.message, Toast.LENGTH_LONG).show()
//
//                        updateSuccess()
//
//                    } else {
//
//                        Toast.makeText(this@UpdatePasswordActivity, "" + result.message, Toast.LENGTH_SHORT).show()
//                    }
//
//                    progressDialog.dismiss()
//
//
//                } catch (e: Exception) {
//
//                    progressDialog.dismiss()
//                    Log.e("error", "" + e.message)
//                    if (e is NETWOError) {
//                        Log.e("Err", "" + e.getErrorResponse())
//                        if (e.getResponseCode() == 401) {
//                            Utils.logoutFromApp(applicationContext)
//                        } else if (e.getResponseCode() == 500) {
//                            Toast.makeText(applicationContext, "Server error", Toast.LENGTH_LONG).show()
//                        }
//                    } else {
//                        Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//            }


        } else {
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
        }


    }





    private fun updateSuccess(){
        openActivityWithFlag(LoginActivity::class.java, this,Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK){}
    }

    override fun onSupportNavigateUp(): Boolean {

        finishAfterTransition()
        return true
    }

    override fun onBackPressed() {

        finishAfterTransition()
    }
}