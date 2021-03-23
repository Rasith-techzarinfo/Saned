package com.saned.view.ui.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.saned.R
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.error.SANEDError
import com.saned.view.ui.interfaces.ResourceStore
import com.saned.view.utils.Constants.Companion.BASE_URL
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_dashboard.home_layout
import kotlinx.android.synthetic.main.navigation_layout.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.backgroundColor

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setToolBar()
        setupNavigationDrawer()
        setupTabLayout()
        init()
    }













    private fun init() {

        //appbar listeners
        home_layout.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        //search
        search_layout.setOnClickListener {

        }

        //navigation items drawer
        closeDrawerIcon.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        nav_profile_image.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            openActivity(ProfileActivity::class.java, this@DashboardActivity){}
        }
        profile_nav_mini.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            openActivity(ProfileActivity::class.java, this@DashboardActivity){}
        }
        my_employees_menu.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        services_actions_menu.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            openActivity(ServicesActionsActivity::class.java, this@DashboardActivity){}
        }
        notifications_menu.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        my_pending_requests_menu.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        my_pending_requests_menu.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        settings_menu.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        about_app_menu.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        logout_menu.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            val mDialog = Dialog(this)
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            mDialog.window!!.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT)
            )
            mDialog.setContentView(R.layout.logout_dialog)
            mDialog.setCancelable(false)
            val exitApp = mDialog.findViewById(R.id.okay_text) as TextView
            val cancelExitApp = mDialog.findViewById(R.id.cancel_text) as TextView

            exitApp.setOnClickListener {

                Utils.logoutFromApp(this)
                mDialog.dismiss()

            }

            cancelExitApp.setOnClickListener {
                mDialog.dismiss()
            }
            mDialog.show()
        }

        //api calls
        Log.e("bearer", "" + prefHelper.getBearerToken())
        Log.e("fcm", "" + prefHelper.getFCMToken())
        submitFCMToServer()

        //dashboard
        setupDashboard()
    }

    private fun setupDashboard() {
        //set values
        profile_name.text = prefHelper.getUserName()
        profile_detail.text = prefHelper.getUserEmail()

        //set images
        Glide.with(this)
                .load(BASE_URL + prefHelper.getUserProfile())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(profile_image)

        //handle permissions
        if(prefHelper.getUserType() == "1"){
            //manager

        } else if(prefHelper.getUserType() == "2") {
            //user

        }
    }

    private fun submitFCMToServer() {

        if (Utils.isInternetAvailable(this)) {


            val hashMap: HashMap<String, String> = HashMap()

            hashMap["fcm_code"] = "" + prefHelper.getFCMToken()
            hashMap["device_type"] = "1"  //(1-android, 2-IOS)


            coroutineScope.launch {

                try {
//                    var result = apiService.updateFcmToken(hashMap).await()
//
//                    Log.e("result", "" + result)
//
//                    if(result.success == "1"){
//                        //on success
//
//                    }

                } catch (e: Exception) {
                    Log.e("error", "" + e.message)
                    if (e is SANEDError) {
                        Log.e("Err", "" + e.getErrorResponse())
                        if (e.getResponseCode() == 401) {
                            Utils.logoutFromApp(this@DashboardActivity)
                        } else if (e.getResponseCode() == 500) {
                            Toast.makeText(this@DashboardActivity, "Server error", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@DashboardActivity, "Something went wrong", Toast.LENGTH_SHORT)
                                .show()
                    }
                }
            }

        } else {
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupTabLayout() {

        viewPager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return ResourceStore.pagerFragments[position]
            }

            override fun getItemCount(): Int {
                return ResourceStore.tabList.size
            }
        }

        TabLayoutMediator(tablayout, viewPager) { tab, position ->
            tab.text = ResourceStore.tabList[position]
        }.attach()

        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
               // Toast.makeText(this@DashboardActivity, "Tab ${tab?.text} selected", Toast.LENGTH_SHORT).show()
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
//                Toast.makeText(this@MainActivity, "Tab ${tab?.text} unselected", Toast.LENGTH_SHORT).show()
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
//                Toast.makeText(this@MainActivity, "Tab ${tab?.text} reselected", Toast.LENGTH_SHORT).show()
            }
        })

    }


    private fun setupNavigationDrawer() {

        var profileUrl = BASE_URL + prefHelper.getUserProfile().toString()

        var profileImage = drawer_layout.findViewById(R.id.nav_profile_image) as ImageView
        var nameTextView = drawer_layout.findViewById<TextView>(R.id.nav_username)
        var emailTextView = drawer_layout.findViewById<TextView>(R.id.nav_userdetail)

        nameTextView.text = prefHelper.getUserName().toString()
        emailTextView.text = prefHelper.getUserEmail().toString()
        //set images
        Glide.with(this)
                .load(profileUrl)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(profileImage)

        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun setToolBar() {
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        finishAfterTransition()
        return true
    }

    override fun onBackPressed() {

        if (drawer_layout!!.isDrawerOpen(GravityCompat.START)) {
            drawer_layout!!.closeDrawer(GravityCompat.START)
            return
        } else {

            val mDialog = Dialog(this)
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            mDialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            mDialog.setContentView(R.layout.exit_layout)
            mDialog.setCancelable(false)
            val exitApp = mDialog.findViewById(R.id.okay_button) as MaterialButton
            val cancelExitApp = mDialog.findViewById(R.id.cancel_button) as MaterialButton

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


    }
}