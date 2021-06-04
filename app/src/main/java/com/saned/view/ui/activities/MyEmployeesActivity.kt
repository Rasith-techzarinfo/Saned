package com.saned.view.ui.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saned.R
import com.saned.model.EmployeeData
import com.saned.view.ui.adapter.myEmployees.MyEmployeesAdapter
import com.saned.view.utils.Utils
import kotlinx.android.synthetic.main.activity_my_employees.*
import kotlinx.android.synthetic.main.activity_my_employees.toolbar
import kotlinx.android.synthetic.main.employee_list_item.*
import kotlinx.android.synthetic.main.services_actions_menu_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class MyEmployeesActivity : AppCompatActivity(), MyEmployeesAdapter.ListAdapterListener {


    var myEmployeesArrayList: ArrayList<EmployeeData> = ArrayList()
    lateinit var myEmployeesAdapter: MyEmployeesAdapter
    lateinit var searchView: SearchView
    var keyword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_employees)
        setToolBar()
        init()
        setUpListener()
    }
    private fun setUpListener(){
    searchView=findViewById(R.id.employee_search)
        //search listener
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                //not empty, keyword min length 3
                if (query.toString() != "" && query.toString().isNotEmpty() && query.toString().length > 2) {
                    keyword = query.toString()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.toString() != "" && newText.toString().isNotEmpty() && newText.toString().length > 2) {
//                    keyword = newText.toString()
////                    getChatUserKeyListFromServer()
                }
                if(newText.toString() == ""){
                    keyword = newText.toString()
                }
                return false
            }
        })



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
        myEmployeesArrayList.clear()

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
        if(myEmployeesArrayList.isEmpty()){
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
            myEmployeesAdapter =
                MyEmployeesAdapter(myEmployeesArrayList, this, this@MyEmployeesActivity)
            recyclerView.adapter = myEmployeesAdapter

        } else {
            val linearLayoutManager = LinearLayoutManager(this)
            linearLayoutManager.reverseLayout = true
            recyclerView.layoutManager = linearLayoutManager

            myEmployeesAdapter =
                MyEmployeesAdapter(myEmployeesArrayList, this, this@MyEmployeesActivity)
            recyclerView.adapter = myEmployeesAdapter

            //pagination code
            var loading = true
            var pastVisiblesItems: Int
            var visibleItemCount: Int
            var totalItemCount: Int
            //first visible item value 0 - top of recyelrview (it called everytime once loaded)

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    //scroll control
//                    if (dy > 0) {
//                        Log.e("tap", "Last Item !")
//                        //   composeFabText!!.visibility = View.GONE
//                        Utils.hideKeyBoard(rootLayout,this@ChatSearchUserActivity)
//                    } else if(dy == dx) {
//                        Log.e("tap", "Equal !")
//                        Utils.hideKeyBoard(rootLayout,this@ChatSearchUserActivity)
//                    } else {
//                        Log.e("tap", "else !")
//                        Utils.hideKeyBoard(rootLayout,this@ChatSearchUserActivity)
//                    }


                    //load more
                    var firstVisiblesItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    var lastvisisbleitem = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    if(lastvisisbleitem == myEmployeesArrayList.size - 1){
//                        Log.e("tap1", "TOP! ${chatSearchList.size - 1}")
                    }
                }

            })



        }



    }


    override fun onListItemClicked(dummyData: EmployeeData, position: Int) {
        val intent=Intent(applicationContext,EmployeeList::class.java)
        startActivity(intent)
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

        val intent=Intent(applicationContext, DashboardActivity::class.java)
        startActivity(intent)
    }
}
