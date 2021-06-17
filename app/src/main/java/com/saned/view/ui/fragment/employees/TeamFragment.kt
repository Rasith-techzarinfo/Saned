package com.saned.view.ui.fragment.employees

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saned.databinding.FragmentTeamBinding
import com.saned.model.Empdata
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.view.error.SANEDError
import com.saned.view.ui.adapter.myEmployees.MyEmployeesAdapter
import com.saned.view.ui.fragment.adapterfragment.TeamAdapter
import com.saned.view.utils.Utils
import kotlinx.android.synthetic.main.activity_my_employees.*
import kotlinx.coroutines.launch


class TeamFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var binding : FragmentTeamBinding

    var myEmployeesArrayList: ArrayList<Empdata> = ArrayList()
    lateinit var myEmployeesAdapter: TeamAdapter

    //add dummy ui for now
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentTeamBinding.inflate(inflater, container, false)
        init()

        return binding.root
    }





    private fun init() {
        getServicesListFromServer()

        //set dummy values to view
        binding.apply {


        }

    }


    private fun getServicesListFromServer(){

        myEmployeesArrayList.clear()

        Utils.startShimmerRL(shimmerLayout, rootLayout)


            coroutineScope.launch {

                try {


                    val result = apiService.getEmployeeList().await()

                   // Log.e("result", "" + result)

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
                                    "" + item.jbtl




                            )
                            myEmployeesArrayList.add(v1)

                            //  var v2 = HAData1((firstArrayList.size - 1), "" + item.wkid)
                            //   secondArrayList.add(v2)
                        }



//                        totalPages = result.data!!.last_page.toString()
//                        Log.e("result", "" + result.data!!.current_page)

                    }else {

//                        Toast.makeText(this@MyEmployeesActivity, "" + result.message, Toast.LENGTH_SHORT)
//                                .show()
                    }

                    Utils.stopShimmerRL(shimmerLayout, rootLayout)


                    //load static data for now
//                    dynamicWFArrayList.add(HAData("30", "This is so coolllll.", "101"))



                    setupRecyclerView()

                }catch (e: Exception){

//                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
//                    Log.e("error", "" + e.message)
//                    if(e.message == "Connection reset" || e.message == "Failed to connect to /40.123.199.239:3000"){
//
//                    } else
                    if (e is SANEDError) {
                       // Log.e("Err", "" + e.getErrorResponse())
                        if (e.getResponseCode() == 401) {
                          //  Utils.logoutFromApp(applicationContext)
                        } else if (e.getResponseCode() == 500) {
                            //Toast.makeText(applicationContext, "Server error", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        //Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT) .show()
                    }
                }

            }

    }

    private fun setupRecyclerView() {
        if(myEmployeesArrayList.isEmpty()){
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            myEmployeesAdapter =
                    TeamAdapter(myEmployeesArrayList, context = requireContext())
            recyclerView.adapter = myEmployeesAdapter

        } else {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.reverseLayout = true
            recyclerView.layoutManager = linearLayoutManager

            myEmployeesAdapter =
                    TeamAdapter(myEmployeesArrayList, context = requireContext())
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




    companion object {
        fun create(): TeamFragment {
            return TeamFragment()
        }
    }
}