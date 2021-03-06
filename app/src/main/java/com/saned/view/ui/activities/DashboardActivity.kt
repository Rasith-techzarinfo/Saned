package com.saned.view.ui.activities

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
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
import com.saned.view.service.ConnectivityReceiver
import com.saned.view.ui.activities.attendence.AttendancePunchActivity
import com.saned.view.ui.interfaces.ResourceStore
import com.saned.view.utils.Constants.Companion.BASE_URL
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.navigation_layout.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set


class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private var networkDialog : Dialog? = null
    private val permission_storage = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val PERMISSION_REQUEST_CODE = 103
    private val permission_camera = arrayOf(Manifest.permission.CAMERA)
    private val CAMERA_PERMISSION_REQUEST_CODE = 104
    private val permission_audio =
            arrayOf(Manifest.permission.RECORD_AUDIO)
    private val AUDIO_PERMISSION_REQUEST_CODE = 105
    private val permission_location =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private val LOCATION_PERMISSION_REQUEST_CODE = 106

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setupTabLayout()
        setupNavigationDrawer()
        init()
    }

    private fun init() {
        //network receiver
        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        //appbar listeners
        home_layout.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        //notification
        notification_layout.setOnClickListener {
            ///go to local notification page
            openActivity(NotificationActivity::class.java, this@DashboardActivity){}
//            notificationCount.text = "5"
        }

        //navigation items drawer
        closeDrawerIcon.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        my_attendence_menu.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            openActivity(AttendancePunchActivity::class.java, this@DashboardActivity){}
        }
        nav_profile_image.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            openActivity(ProfileActivity::class.java, this@DashboardActivity){}
        }
        profile_edit_icon.setOnClickListener {
                drawer_layout.closeDrawer(GravityCompat.START)
                openActivity(EditProfileActivity::class.java, this@DashboardActivity){}

        }
        profile_nav_mini.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            openActivity(ProfileActivity::class.java, this@DashboardActivity){}
        }
        profile_image.setOnClickListener {
            openActivity(ProfileActivity::class.java, this@DashboardActivity){}
        }
        my_employees_menu.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            openActivity(MyEmployeesActivity::class.java, this@DashboardActivity){}
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
            openActivity(PendingRequestsActivity::class.java, this@DashboardActivity){}
        }
        my_pending_requests_menu.setOnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
            openActivity(PendingRequestsActivity::class.java, this@DashboardActivity){}
        }
        settings_menu.setOnClickListener {
            openActivity(SettingsActivity::class.java, this@DashboardActivity){}
        }
        about_app_menu.setOnClickListener {
            val url="https://dev.osaned.com/why-saned"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
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

        //dashboard
        validatePermission()

        //api calls
        Log.e("bearer", "" + prefHelper.getBearerToken())
        Log.e("fcm", "" + prefHelper.getFCMToken())
//        submitFCMToServer()
//        updateProfileData()
        setupDashboard()
    }

    private fun setupDashboard() {

        //set values
        profile_name.text = prefHelper.getUserName()
//        profile_detail.text = "Last Login: 6 min ago " + Utils.convertDbtoNormalDateTime1(
//            prefHelper.getLastLogin().toString()
//        )  //getUserEmail

        //set images
        Glide.with(this)
                .load("http://40.123.199.239:3000/images/" + prefHelper.getUserProfile())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(profile_image)


        //handle permissions
        Log.e("user", "" + prefHelper.getUserType() + " " + prefHelper.getManagerLevel())
        if(prefHelper.getUserType() == "HR Admin"){
            //manager

        } else if(prefHelper.getUserType() == "Company User") {
            //user
            my_employees_menu.visibility = View.GONE
            services_actions_menu.visibility = View.GONE

        }
    }


    private fun updateProfileData() {
        if (Utils.isInternetAvailable(this)) {

//            Utils.startShimmerRL(shimmerLayout, rootLayout)
//            emptyView.visibility = View.GONE

            coroutineScope.launch {
                try {
                    val result = apiService.getProfileData().await()
                    Log.e("profile", "" + result)

                    if (result.success == "1") {

                        for (item in result.user!!){

                            item.job_title

                            val designation = item.job_title

                            profile_detail.text = designation
                            var emailTextView = drawer_layout.findViewById<TextView>(R.id.nav_userdetail)
                            emailTextView.text = designation
                        }


                        //save to pref

                        //                        prefHelper.setUserName("" + "${result.user!!.t_nama} ") //${result.user!!.last_name}
//                        prefHelper.setUserEmail("" + result.user!!.t_mail)
//                       // prefHelper.setLastLogin("" + result.user!!.previous_login)
//                        prefHelper.setUserType("" + result.user!!.t_role)  //update n check role id freq
//                        //for now, approval matrix
//                        prefHelper.setManagerLevel(if (result.user.t_mail == "rightcursor33@gmail.com") "HR Admin" else if (result.user.t_mail == "immu@gmail.com") "2" else "") //"" not a manager

                        setupNavigationDrawer()
                        setupDashboard()

                        // no profile for now
//                        if (result.user!!.profile_pic != null) {
//
//                            Glide.with(this@ProfileActivity).load(BASE_URL + result.user.profile_pic).placeholder(
//                                    R.drawable.ic_user
//                            ).into(profileImage)
//                        }


                    } else {
                        Toast.makeText(
                            this@DashboardActivity,
                            "" + result.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

//                    Utils.stopShimmerRL(shimmerLayout, rootLayout)

                } catch (e: java.lang.Exception) {
//                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
                    Log.e("error", "" + e.message)
                    if (e is SANEDError) {
                        Log.e("Err", "" + e.getErrorResponse())
                        if (e.getResponseCode() == 401) {
                            Utils.logoutFromApp(applicationContext)
                        } else if (e.getResponseCode() == 500) {
                            Toast.makeText(applicationContext, "Server error", Toast.LENGTH_LONG)
                                    .show()
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
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun submitFCMToServer() {

        if (Utils.isInternetAvailable(this)) {


            val hashMap: HashMap<String, String> = HashMap()

            hashMap["fcm_token"] = "" + prefHelper.getFCMToken()
            hashMap["device_type"] = "1"  //(1-android, 2-IOS)


            coroutineScope.launch {

                try {
                    var result = apiService.updateFcmToken(hashMap).await()

                    Log.e("result", "" + result)

                    if(result.success == "1"){
                        //on success

                    }

                } catch (e: Exception) {
                    Log.e("error", "" + e.message)
                    if (e is SANEDError) {
                        Log.e("Err", "" + e.getErrorResponse())
                        if (e.getResponseCode() == 401) {
                            Utils.logoutFromApp(this@DashboardActivity)
                        } else if (e.getResponseCode() == 500) {
                            Toast.makeText(
                                this@DashboardActivity,
                                "Server error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@DashboardActivity,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        )
                                .show()
                    }
                }
            }

        } else {
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
        }
    }




    private fun setupNavigationDrawer() {

        var profileUrl = BASE_URL + prefHelper.getUserProfile().toString()

        var profileImage = drawer_layout.findViewById(R.id.nav_profile_image) as ImageView
        var nameTextView = drawer_layout.findViewById<TextView>(R.id.nav_username)
        var emailTextView = drawer_layout.findViewById<TextView>(R.id.nav_userdetail)

        nameTextView.text = prefHelper.getUserName().toString()
//        emailTextView.text = "Last Login: " + Utils.convertDbtoNormalDateTime1(
//            prefHelper.getLastLogin().toString()
//        )  //getUserEmail
        //set images


        Glide.with(this)
            .load("http://40.123.199.239:3000/images/" + prefHelper.getUserProfile())
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .into(profileImage)
//        Glide.with(this)
//                .load(profileUrl)
//                .placeholder(R.drawable.ic_user)
//                .error(R.drawable.ic_user)
//                .into(profileImage)

        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }



    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
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
            submitFCMToServer()
            updateProfileData()
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
//        //for now, make result code
//        updateProfileData()
    }

    override fun onDestroy() {
        super.onDestroy()
        ConnectivityReceiver.connectivityReceiverListener = null
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
                finishAffinity()
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
            tab.setIcon(ResourceStore.tabIcons[position])
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


    private fun validatePermission() {

        if(checkStoragePermission()){
            if(checkCameraPermission()){
                if (checkAudioPermission()) {
                    if (checkLocationPermission()) {
                        //okay

                    } else {
                        if (prefHelper.getLocationPermission() == "0") {
                            requestLocationPermission()
                        } else {
                            showLocationPermissionDialog()
                        }
                    }

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

//        checkStorageManagerPermission()
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
                builder.setPositiveButton(
                    "Click here",
                    DialogInterface.OnClickListener { dialog, which ->
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
            this,
            permission_storage,
            PERMISSION_REQUEST_CODE
        )
    }


    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
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


        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    validatePermission()
                } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(
                        permissions[0]
                    )
                ) {

                    Log.e("Never", "Go to Settings and Grant the permission to use this feature.")

                    prefHelper.setLocationPermission("1")

                    // User selected the Never Ask Again Option
                } else {

                    prefHelper.setLocationPermission("1")
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
            this,
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


    private fun checkLocationPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return result == PackageManager.PERMISSION_GRANTED
    }


    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            permission_location,
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }


    fun showLocationPermissionDialog() {

        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("We need to access your Location to use this Application. Kindly allow permission now")
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
    private class ViewPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(
        fm,
        behavior
    ) {
        private val fragments: MutableList<Fragment> = ArrayList()
        private val fragmentTitle: MutableList<String> = ArrayList()
        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            fragmentTitle.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitle[position]
        }
    }
}



