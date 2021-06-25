package com.saned.view.ui.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.saned.R
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.utils.Utils.Companion.openActivity
import kotlinx.android.synthetic.main.activity_attendance_punch.*


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        fullScreenNoActionBar()

        Handler().postDelayed(Runnable {

            if (prefHelper.getIsLogin() == "1") {
                val builder = AlertDialog.Builder(this)
                //set title for alert dialog
                builder.setTitle("Login Via")
                //set message for alert dialog
                builder.setIcon(R.drawable.saned_logo)

                //performing positive action
                builder.setPositiveButton("Biometric"){dialogInterface, which ->
                   Biometric()
                }
                //performing negative action
                builder.setNegativeButton("Skip"){dialogInterface, which ->
                    openActivity(DashboardActivity::class.java, this){}
                    Toast.makeText(
                            this@SplashScreenActivity,
                            "Login Succesfully",
                            Toast.LENGTH_LONG
                    ).show()
                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()

            } else {

//                if(prefHelper.getIsFirstTime() == "1"){

                openActivity(LoginActivity::class.java, this) {}

//                } else {
//
//                    intent = Intent(applicationContext, OnBoardingActivity::class.java)
//                    startActivity(intent)
//                    overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
//                }
            }

        }, 2000)

    }

// take status bar app fullscreen
    private fun fullScreenNoActionBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    }
    private fun Biometric(){
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(
                this,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {

                    override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                            val intent = Intent(applicationContext, DashboardActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(
                                    this@SplashScreenActivity,
                                    "Login Succesfully",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else {


                        }
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        val intent = Intent(applicationContext, DashboardActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(
                                this@SplashScreenActivity,
                                "Login Succesfully",
                                Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()

                        Toast.makeText(
                                this@SplashScreenActivity,
                                "Can't authorized",
                                Toast.LENGTH_SHORT
                        ).show()

                    }
                })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using fingerprint or face")
                .setNegativeButtonText("Cancel")
                .build()
        biometricPrompt.authenticate(promptInfo)

    }
}
