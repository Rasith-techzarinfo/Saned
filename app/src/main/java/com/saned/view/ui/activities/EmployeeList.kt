package com.saned.view.ui.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.saned.R
import com.saned.view.service.ConnectivityReceiver
import com.saned.view.ui.interfaces.ResourceStore
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_dashboard.home_menu
import kotlinx.android.synthetic.main.activity_dashboard.tablayout
import kotlinx.android.synthetic.main.activity_dashboard.viewPager
import kotlinx.android.synthetic.main.activity_employee_list.*
import kotlinx.android.synthetic.main.activity_view_dynamic_w_f.*

class EmployeeList : AppCompatActivity() {


    var t_mail: String = ""
    var t_nama: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_list)
        setupTabLayout()
        init()

    }

    private fun init() {


      //  registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        //get intent data
        t_mail = "" + intent.getStringExtra("t_mail")
        t_nama = "" + intent.getStringExtra("t_nama")
        Log.e("itt", "" + t_nama)

        employeelist_mail.text = t_mail
        employeelist_name.text = t_nama


                //  toolbarTitle.text = formName + " Detail"

        home_menu.setOnClickListener {
            val intent=Intent(applicationContext,MyEmployeesActivity::class.java)
            startActivity(intent)
            finishAfterTransition()
        }
    }


    private fun setupTabLayout() {

        viewPager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return ResourceStore.fragments[position]
            }

            override fun getItemCount(): Int {
                return ResourceStore.tabList2.size
            }
        }

        TabLayoutMediator(tablayout, viewPager) { tab, position ->
            tab.text = ResourceStore.tabList2[position]
            tab.setIcon(ResourceStore.tabIcons2[position])
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

}