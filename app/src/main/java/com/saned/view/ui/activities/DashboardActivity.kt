package com.saned.view.ui.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.saned.R
import com.saned.view.ui.interfaces.ResourceStore
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_dashboard.home_layout
import kotlinx.android.synthetic.main.navigation_layout.*
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

//        profileUrl = BASE_URL + prefHelper.getUserProfile().toString()
//
//        var profileImage = navDrawerLayout.findViewById(R.id.profile_image) as ImageView
//        var nameTextView = navDrawerLayout.findViewById<TextView>(R.id.profile_name)
//
//        nameTextView.text = prefHelper.getUserName().toString()
//        //set images
//        Glide.with(this)
//                .load(profileUrl)
//                .placeholder(R.drawable.ic_user)
//                .error(R.drawable.ic_user)
//                .into(profileImage)
//
//        navigationView.setNavigationItemSelectedListener(this)

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