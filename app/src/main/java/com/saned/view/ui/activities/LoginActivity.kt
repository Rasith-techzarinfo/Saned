package com.saned.view.ui.activities

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.saned.R
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.error.SANEDError
import com.saned.view.utils.Constants
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch
import java.util.*


class LoginActivity : AppCompatActivity() {


    private val permission_storage = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val PERMISSION_REQUEST_CODE = 103
    private val permission_camera = arrayOf(Manifest.permission.CAMERA)
    private val CAMERA_PERMISSION_REQUEST_CODE = 104
    private val permission_audio =
            arrayOf(Manifest.permission.RECORD_AUDIO)
    private val AUDIO_PERMISSION_REQUEST_CODE = 105


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }





    private fun performLoginService() {

        Utils.hideKeyBoard(login_button, this@LoginActivity)

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
            hashMap["password"] = "" + password_edit_text.text.toString()
            hashMap["device_type"] = "" + Constants.DEVICE_TYPE



            coroutineScope.launch {
                try {

                    val result = apiService.performLogin(hashMap).await()
//
//                    Log.e("result", "" + result)
//
//                    if (result.success == "1") {
//
//
//                        prefHelper.setBearerToken("" + result.token)
//                        prefHelper.setUserId("" + result.user!!.id)
//                        prefHelper.setUserPassword("" + password_edit_text.text.toString())
//                        prefHelper.setUserType("" + result.user!!.role_id)      //role_id 1 - manager, 2- user
//                        prefHelper.setUserName("" + "${result.user!!.first_name} ${result.user!!.last_name}")
//                        prefHelper.setUserEmail("" + email_edit_text.text.toString())
////                        prefHelper.setUserProfile("" + result.data?.get(0)!!.profile_pic)
//                        prefHelper.setIsLogin("1")
//
//                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
//                        loginSuccess()
//                    } else {
//
//                        Toast.makeText(this@LoginActivity, "" + result.message, Toast.LENGTH_SHORT)
//                                .show()
//                    }

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
//            Snackbar.make(nested_scroll_view, "No Internet Available", Snackbar.LENGTH_LONG).show()
            Toast.makeText(applicationContext, "No Internet Available", Toast.LENGTH_SHORT).show()
        }

    }

//    @OnClick(R.id.forgot_password)
//    fun forgetPassword(){
//        intent = Intent(applicationContext, ForgetPasswordActivity::class.java)
//        startActivity(intent)
//        overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
//    }


    private  fun loginSuccess(){
        openActivity(DashboardActivity::class.java, this){}
    }








    private fun init() {
        //listeners
        login_button.setOnClickListener {

            user_layout.isErrorEnabled = false
            password_layout.isErrorEnabled = false

            //upiTextLayout.editText!!.text!!
            if (email_edit_text.text.toString() == "" || !Utils.isValidEmail(email_edit_text.text.toString())) {
                user_layout.error = "Enter Valid Email"
                user_layout.requestFocus()
                return@setOnClickListener
            }

            if(password_edit_text.text.toString() == "" || password_edit_text.text.toString().length < 8) {
                password_layout.error = "Password must be minimum 8 characters"
                password_layout.requestFocus()
                return@setOnClickListener
            }

//        if(!Utils.isValidPassword(passwordEditText.text.toString())){
//            passwordLayout.error = "Password must contain 1 Upper case, 1 lower case ,1 special character and 1 number"
//
//            return
//        }

            validatePermission()
        }




    }



    override fun onBackPressed() {

        val mDialog = Dialog(this)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        mDialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
        )
        mDialog.setContentView(R.layout.exit_layout)
        mDialog.setCancelable(false)
        val exitApp = mDialog.findViewById(R.id.okay_button) as MaterialButton
        val cancelExitApp = mDialog.findViewById(R.id.cancel_button) as MaterialButton
        val relaiveLayout = mDialog.findViewById<RelativeLayout>(R.id.relative1)

        exitApp.setOnClickListener {
            finishAfterTransition()
            mDialog.dismiss()
        }

        cancelExitApp.setOnClickListener {
            mDialog.dismiss()
        }
        mDialog.show()

//        MaterialAlertDialogBuilder(this@LoginActivity)
//                .setMessage("Are you sure want to Exit?")
//                .setPositiveButton("OK") { dialogInterface, _ ->
//                    finishAffinity()
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//                }
//                .setNegativeButton("CANCEL") { dialogInterface, _ ->
//                 }
//                .show()
//                .setCanceledOnTouchOutside(false)

    }


    private fun validatePermission() {

        if(checkStoragePermission()){
            if(checkCameraPermission()){
                if (checkAudioPermission()) {
                    performLoginService()

                } else {
                    if (prefHelper.getAudioPermission() == "0") {
                        requestAudioPermission()
                    } else {
                        showAudioPermissionDialog()
                    }
                }


            }else{

                if(prefHelper.getCameraPermission() == "0"){
                    requestCameraPermission()
                }
                else{
                    showCameraPermissionDialog()
                }

            }

        } else {

            if (prefHelper.getStoragePermission() == "0") {
                requestStoragePermission()
            } else {
                showStoragePermissionDialog()
            }

        }

        checkStorageManagerPermission()
    }

    //permission functions
    private fun checkStoragePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun checkStorageManagerPermission() {
        //request AllFileAccess above & on R
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            } else {
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                builder.setIcon(R.drawable.saned_logo).setTitle("Alert")
                builder.setMessage("We need to access your storage to use this Application. Kindly allow permission now")
                builder.setPositiveButton("Click here", DialogInterface.OnClickListener { dialog, which ->
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivity(intent)
                    dialog.dismiss()
                })
                //builder.show();
                val dialog = builder.create()
                dialog.show() //Only after .show() was called
                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(resources.getColor(R.color.colorPrimary))
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
                dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(resources.getColor(R.color.colorPrimary))
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        return result == PackageManager.PERMISSION_GRANTED
    }


    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
                this@LoginActivity,
                permission_storage,
                PERMISSION_REQUEST_CODE
        )
    }


    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
                this@LoginActivity,
                permission_camera,
                CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    validatePermission()
                } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(
                                permissions[0]
                        )) {

                    Log.e("Never", "Go to Settings and Grant the permission to use this feature.")

                    prefHelper.setStoragePermission("1")

                    // User selected the Never Ask Again Option
                } else {

                    prefHelper.setStoragePermission("1")
                    Log.e("Denied", "Permission Denied")
                }
            }
        }

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    validatePermission()
                } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(
                                permissions[0]
                        ))
                {

                    Log.e("Never", "Go to Settings and Grant the permission to use this feature.")

                    prefHelper.setCameraPermission("1")

                    // User selected the Never Ask Again Option
                } else {

                    prefHelper.setCameraPermission("1")
                    Log.e("Denied", "Permission Denied")
                }
            }
        }

        if (requestCode == AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    validatePermission()
                } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(
                                permissions[0]
                        )
                ) {

                    Log.e("Never", "Go to Settings and Grant the permission to use this feature.")

                    prefHelper.setAudioPermission("1")

                    // User selected the Never Ask Again Option
                } else {

                    prefHelper.setAudioPermission("1")
                    Log.e("Denied", "Permission Denied")
                }
            }
        }

    }



    private fun checkAudioPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        return result == PackageManager.PERMISSION_GRANTED
    }


    private fun requestAudioPermission() {
        ActivityCompat.requestPermissions(
                this@LoginActivity,
                permission_audio,
                AUDIO_PERMISSION_REQUEST_CODE
        )
    }


    fun showAudioPermissionDialog() {

        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("We need to access your Microphone to use this Application. Kindly allow permission now")
        builder.setPositiveButton("Click here", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
            dialog.dismiss()
        })
        //builder.show();
        val dialog = builder.create()
        dialog.show() //Only after .show() was called
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.colorPrimary))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.colorPrimary))

    }

    fun showStoragePermissionDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        // builder.setIcon(R.drawable.ic_baseline_warning_24).setTitle("Alert")
        builder.setTitle("Alert")
        builder.setMessage("We need to access your storage to use this Application. Kindly allow permission now")
        builder.setPositiveButton("Click here", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
            dialog.dismiss()
        })
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.colorPrimary))
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.colorPrimary))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)


    }

    fun showCameraPermissionDialog() {

        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("We need to access your Camera to use this Application.Kindly allow permission now")
        builder.setPositiveButton("Click here", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
            dialog.dismiss()
        })
        //builder.show();
        val dialog = builder.create()
        dialog.show() //Only after .show() was called
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.colorPrimary))
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.colorPrimary))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

    }

}



//var progressDialog : Dialog
//var customProgressView = LayoutInflater.from(this)
//    .inflate(R.layout.custom_progress_dialog_layout, null, false)
//progressDialog = MaterialAlertDialogBuilder(this).setView(customProgressView).create()
//progressDialog.setCancelable(false)
//progressDialog.setCanceledOnTouchOutside(false)
//progressDialog.show()