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
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.saned.R
import com.saned.view.ui.interfaces.ResourceStore
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivity
import org.jetbrains.anko.backgroundColor

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.toolbar_title)
    lateinit var toolbarTitle: TextView

    @BindView(R.id.drawer_layout)
    lateinit var drawerLayout: DrawerLayout

    @BindView(R.id.nav_view)
    lateinit var navigationView: NavigationView

    @BindView(R.id.nav_drawer)
    lateinit var navDrawerLayout: RelativeLayout

    @BindView(R.id.tablayout)
    lateinit var tabLayout: TabLayout

    @BindView(R.id.viewPager)
    lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        ButterKnife.bind(this)

        setToolBar()
        setupNavigationDrawer()
        setupTabLayout()
        init()
    }


    private fun init() {


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

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = ResourceStore.tabList[position]
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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


    //appbar listeners
    @OnClick(R.id.home_layout)
    fun homeMenuCLicked() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    @OnClick(R.id.closeDrawerIcon)
    fun closeDrawerMenuCLicked() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    @OnClick(R.id.search_layout)
    fun searchMenuClicked() {
//        val intent = Intent(this, ChatSearchUserActivity::class.java)
//        startActivity(intent)
//        overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
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

    //navigation items drawer
    @OnClick(R.id.nav_profile_image)
    fun myProfileHeader() {
        drawerLayout.closeDrawer(GravityCompat.START)
        openActivity(ProfileActivity::class.java, this@DashboardActivity){
//            putString("string.key", "string.value")
//            putInt("string.key", 43)
        }
    }

    @OnClick(R.id.profile_nav_mini)
    fun myProfileMenu() {
        drawerLayout.closeDrawer(GravityCompat.START)
        openActivity(ProfileActivity::class.java, this@DashboardActivity){}
    }

    @OnClick(R.id.my_employees_menu)
    fun myEmployees() {
        drawerLayout.closeDrawer(GravityCompat.START)
//        val intent = Intent(applicationContext, ProfileActivity::class.java)
//        startActivity(intent)
//        overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
    }

    @OnClick(R.id.services_actions_menu)
    fun servicesActions() {
        drawerLayout.closeDrawer(GravityCompat.START)
        openActivity(ServicesActionsActivity::class.java, this@DashboardActivity){}
    }

    @OnClick(R.id.notifications_menu)
    fun myNotifications() {
        drawerLayout.closeDrawer(GravityCompat.START)
//        val intent = Intent(applicationContext, ProfileActivity::class.java)
//        startActivity(intent)
//        overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
    }

    @OnClick(R.id.my_pending_requests_menu)
    fun myPendingRequests() {
        drawerLayout.closeDrawer(GravityCompat.START)
//        val intent = Intent(applicationContext, ProfileActivity::class.java)
//        startActivity(intent)
//        overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
    }

    @OnClick(R.id.settings_menu)
    fun mySettings() {
        drawerLayout.closeDrawer(GravityCompat.START)
//        val intent = Intent(applicationContext, ProfileActivity::class.java)
//        startActivity(intent)
//        overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
    }

    @OnClick(R.id.about_app_menu)
    fun aboutApp() {
        drawerLayout.closeDrawer(GravityCompat.START)
//        val intent = Intent(applicationContext, ProfileActivity::class.java)
//        startActivity(intent)
//        overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right)
    }

    @OnClick(R.id.logout_menu)
    fun logoutMenu() {
        drawerLayout.closeDrawer(GravityCompat.START)
        val mDialog = Dialog(this)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        mDialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
        )
        mDialog.setContentView(R.layout.logout_dialog)
        mDialog.setCancelable(false)
        val exitApp = mDialog.findViewById(R.id.okay_text) as TextView
        val cancelExitApp = mDialog.findViewById(R.id.cancel_text) as TextView
        val relaiveLayout = mDialog.findViewById<RelativeLayout>(R.id.relative1)
        relaiveLayout.backgroundColor = resources.getColor(R.color.colorPrimary)

        exitApp.setOnClickListener {

            Utils.logoutFromApp(this)
            mDialog.dismiss()

        }

        cancelExitApp.setOnClickListener {
            mDialog.dismiss()
        }
        mDialog.show()
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

        finish()
        overridePendingTransition(R.anim.back_left_to_right, R.anim.back_right_to_left)
        return true
    }

    override fun onBackPressed() {

        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
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
            val relaiveLayout = mDialog.findViewById<RelativeLayout>(R.id.relative1)

            exitApp.setOnClickListener {
                finishAffinity()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
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