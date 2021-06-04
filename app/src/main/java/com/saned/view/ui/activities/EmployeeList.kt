package com.saned.view.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.saned.R
import com.saned.view.ui.interfaces.ResourceStore
import kotlinx.android.synthetic.main.activity_dashboard.*

class EmployeeList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_list)
        setupTabLayout()
        var home=findViewById(R.id.home_menu) as ImageView
        home.setOnClickListener {
            val intent=Intent(applicationContext,MyEmployeesActivity::class.java)
            startActivity(intent)
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
            tab.setIcon(R.drawable.ic_baseline_bar_chart_24)
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