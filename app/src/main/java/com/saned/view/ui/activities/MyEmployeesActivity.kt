package com.saned.view.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saned.R
import com.saned.model.*
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.view.error.SANEDError
import com.saned.view.ui.adapter.myEmployees.MyEmployeesAdapter
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivityWithResult
import kotlinx.android.synthetic.main.activity_history_dynamic_w_f.*
import kotlinx.android.synthetic.main.activity_my_employees.*
import kotlinx.android.synthetic.main.activity_my_employees.recyclerView
import kotlinx.android.synthetic.main.activity_my_employees.rootLayout
import kotlinx.android.synthetic.main.activity_my_employees.shimmerLayout
import kotlinx.android.synthetic.main.activity_my_employees.swipeRefreshLayout
import kotlinx.android.synthetic.main.activity_my_employees.toolbar
import kotlinx.android.synthetic.main.activity_services_actions.*
import kotlinx.android.synthetic.main.employee_list_item.*
import kotlinx.android.synthetic.main.services_actions_menu_item.view.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MyEmployeesActivity : AppCompatActivity(), MyEmployeesAdapter.ListAdapterListener {


    var myEmployeesArrayList: ArrayList<Empdata> = ArrayList()
    lateinit var myEmployeesAdapter: MyEmployeesAdapter
    lateinit var searchView: SearchView
    var keyword: String = ""
    var currentPage: Int = 1

    var totalPages: String = ""


    var t_mail: String = ""
    var t_nama: String = ""

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
           // getValues()
            getServicesListFromServer()
            swipeRefreshLayout.isRefreshing = false
        }
        //get data
        //getValues()
        getServicesListFromServer()

    }



//    private fun getValues() {
//        myEmployeesArrayList.clear()
//
//        if (Utils.isInternetAvailable(this)) {
//            Utils.startShimmerRL(shimmerLayout, rootLayout)
//
//            //add dummy values
//            myEmployeesArrayList.add(EmployeeData("3", "Ishaque", "Project Manager",  "ishaque@gmail.com",  "9876543210", "https://content.fortune.com/wp-content/uploads/2018/10/gettyimages-524263730-e1540503436210.jpg"))
//            myEmployeesArrayList.add(EmployeeData("2", "Test", "User",  "test@gmail.com",  "9876543210", "https://content.fortune.com/wp-content/uploads/2018/10/gettyimages-524263730-e1540503436210.jpg"))
//            myEmployeesArrayList.add(EmployeeData("1", "Robert Downey JR", "Admin",  "robert@gmail.com",  "9876543210", "https://content.fortune.com/wp-content/uploads/2018/10/gettyimages-524263730-e1540503436210.jpg"))
//
//
//
//            Utils.stopShimmerRL(shimmerLayout, rootLayout)
//            setupRecyclerView()
//
//
//        } else {
//            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
//        }
//    }



    private fun getServicesListFromServer(){
        myEmployeesArrayList.clear()
        currentPage = 1

        if (Utils.isInternetAvailable(this)) {

            //shimmer
            Utils.startShimmerRL(shimmerLayout, rootLayout)

            coroutineScope.launch {

                try {


                    val result = apiService.getEmployeeList().await()

                    Log.e("result", "" + result)

                    if(result.success == "1"){

                       // var myEmployeesArrayList: ArrayList<Empdata> = ArrayList()
                      //  var secondArrayList: ArrayList<HAData1> = ArrayList()

                        for (item in result.data!!) {

                            val v1 = Empdata(
                                "" + item.t_idno,
                                "" + item.uuid,
                                "" + item.t_mail,
                                "" + item.t_nama,
                                "" + item.t_pass,
                                "" + item.t_lnme,
                                "" + item.t_comp,
                                "" + item.t_role,
                                "" + item.t_page,
                                "" + item.t_emno,
                                "" + item.is_active,
                                "" + item.created_by,
                                "" + item.updated_by,
                                "" + item.profile_pic,
                                "" + item.created_at,
                                "" + item.updated_at,
                                "" + item.deleted_at,
                                "" + item.fcm_token,
                                "" + item.remember_token,
                                "" + item.remember_token_at,
                                "" + item.emp_id,
                                "" + item.f_name,
                                "" + item.a_name,
                                "" + item.jbtl,
                                "" + item.email,
                                "" + item.job_title




                            )
                            myEmployeesArrayList.add(v1)

                          //  var v2 = HAData1((firstArrayList.size - 1), "" + item.wkid)
                         //   secondArrayList.add(v2)
                        }



//                        totalPages = result.data!!.last_page.toString()
//                        Log.e("result", "" + result.data!!.current_page)

                    }else {

                        Toast.makeText(this@MyEmployeesActivity, "" + result.message, Toast.LENGTH_SHORT)
                            .show()
                    }



                    //load static data for now
//                    dynamicWFArrayList.add(HAData("30", "This is so coolllll.", "101"))




                    Utils.stopShimmerRL(shimmerLayout, rootLayout)


                    setupRecyclerView()

                }catch (e: Exception){

                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
                    Log.e("error", "" + e.message)
//                    if(e.message == "Connection reset" || e.message == "Failed to connect to /40.123.199.239:3000"){
//
//                    } else
                    if (e is SANEDError) {
                        Log.e("Err", "" + e.getErrorResponse())
                        if (e.getResponseCode() == 401) {
                            Utils.logoutFromApp(applicationContext)
                        } else if (e.getResponseCode() == 500) {
                            Toast.makeText(applicationContext, "Server error", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        } else {
            // Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
            Utils.checkNetworkDialog(this, this) { getServicesListFromServer() }
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


    override fun onListItemClicked(dummyData: Empdata, position: Int) {

        openActivityWithResult(EmployeeList::class.java, this, 101){
            putString("t_mail", "" + dummyData.email)
            putString("t_nama", "" + dummyData.f_name)
           // putString("wkid", "" + dummyData.id)
        }
//        val intent=Intent(applicationContext,EmployeeList::class.java)
//        startActivity(intent)
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


}
