package com.saned.view.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.saned.R
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.utils.Utils.Companion.openActivity


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        fullScreenNoActionBar()

        Handler().postDelayed(Runnable {

            if (prefHelper.getIsLogin() == "1") {

                openActivity(DashboardActivity::class.java, this){}
            } else {

//                if(prefHelper.getIsFirstTime() == "1"){

                    openActivity(LoginActivity::class.java, this){}

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
}
