package com.saned.view.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.saned.R
import com.saned.model.EmployeeData
import com.saned.view.ui.adapter.myEmployees.MyEmployeesAdapter
import com.saned.view.utils.Utils
import kotlinx.android.synthetic.main.activity_my_employees.*
import kotlinx.android.synthetic.main.activity_my_employees.toolbar

class MyEmployeesActivity : AppCompatActivity(), MyEmployeesAdapter.ListAdapterListener {


    var myEmployeesArrayList: ArrayList<EmployeeData> = ArrayList()
    lateinit var myEmployeesAdapter: MyEmployeesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_employees)
        setToolBar()
        init()
    }




    private fun init() {

        swipeRefreshLayout.setOnRefreshListener {
            getValues()
            swipeRefreshLayout.isRefreshing = false
        }
        //get data
        getValues()

    }



    private fun getValues() {
//        myEmployeesArrayList.clear()

        if (Utils.isInternetAvailable(this)) {
            Utils.startShimmerRL(shimmerLayout, rootLayout)

            //add dummy values
            myEmployeesArrayList.add(EmployeeData("3", "Ishaque", "Project Manager",  "ishaque@gmail.com",  "9876543210", "https://content.fortune.com/wp-content/uploads/2018/10/gettyimages-524263730-e1540503436210.jpg"))
            myEmployeesArrayList.add(EmployeeData("2", "Test", "User",  "test@gmail.com",  "9876543210", "https://content.fortune.com/wp-content/uploads/2018/10/gettyimages-524263730-e1540503436210.jpg"))
            myEmployeesArrayList.add(EmployeeData("1", "Robert Downey JR", "Admin",  "robert@gmail.com",  "9876543210", "https://content.fortune.com/wp-content/uploads/2018/10/gettyimages-524263730-e1540503436210.jpg"))



            Utils.stopShimmerRL(shimmerLayout, rootLayout)
            setupRecyclerView()


        } else {
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
        }
    }




    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        myEmployeesAdapter =
            MyEmployeesAdapter(myEmployeesArrayList, this, this@MyEmployeesActivity)
        recyclerView.adapter = myEmployeesAdapter

    }


    override fun onListItemClicked(dummyData: EmployeeData, position: Int) {

    }




    private fun setToolBar() {
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
    }


    override fun onSupportNavigateUp(): Boolean {

        finishAfterTransition()
        return true
    }

    override fun onBackPressed() {

        finishAfterTransition()
    }
}