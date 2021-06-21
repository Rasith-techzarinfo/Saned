package com.saned.view.ui.fragment.dashboard

import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.saned.R
import com.saned.databinding.FragmentTeamBinding
import com.saned.databinding.FragmentWorkItemsBinding
import com.saned.model.Data
import com.saned.sanedApplication
import com.saned.view.error.SANEDError
import com.saned.view.ui.adapter.pendingRequests.PendingRequestsAdapter
import com.saned.view.ui.fragment.adapterfragment.WorkItemsAdapter
import com.saned.view.utils.Utils
import kotlinx.android.synthetic.main.activity_my_employees.*
import kotlinx.android.synthetic.main.activity_my_employees.recyclerView
import kotlinx.android.synthetic.main.activity_my_employees.shimmerLayout
import kotlinx.android.synthetic.main.fragment_work_items.*
import kotlinx.coroutines.launch

class WorkItemsFragment : Fragment() {
    var myPendingsArrayList: ArrayList<Data> = ArrayList()
    lateinit var myPendingAdapter: WorkItemsAdapter

    var currentPage: Int = 1
    var wkid: String = ""
    lateinit var progressDialog: Dialog
    lateinit var binding : FragmentWorkItemsBinding

    //add dummy ui for now
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        progressDialog= Dialog(requireContext())
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setContentView(com.saned.R.layout.custom_progress_dialog_layout)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        Handler().postDelayed({
            progressDialog.dismiss()
        },25000)
        binding  = FragmentWorkItemsBinding.inflate(inflater, container, false)
        init()

        return binding.root
    }





    private fun init() {

        //set dummy values to view
        binding.apply {
            getPendingRequestFromServer()

        }

    }

    private fun getPendingRequestFromServer() {
        myPendingsArrayList.clear()
        currentPage = 1

            sanedApplication.coroutineScope.launch {

                try{

                    val result = sanedApplication.apiService.getPendingHistory().await()

                    Log.e("result", "" + result)

                    if(result.success == "1"){

//                        var firstArrayList: ArrayList<HousingWFData> = ArrayList()
//                        var secondArrayList: ArrayList<HAData1> = ArrayList()

                        for (item in result.data!!) {

                            val v1 = Data(
                                    "" + item.wkid,
                                    "" + item.form_name,
                                    "" + item.pending_since,
                                    "" + item.pending_with,
                                    "" + item.added_by,
                                    "" + item.added_at,
                                    "" + item.profile,
                                    "" + item.job_title,
                                    "" + item.last_action_date,
                                    "" + item.status,
                                    "" + item.reason

                            )
                            myPendingsArrayList.add(v1)

//                            var v2 = HAData1((firstArrayList.size - 1), "" + item.wkid)
//                            secondArrayList.add(v2)
                        }

//                        val hashMap: HashMap<String, MutableList<Int>> = HashMap()


//                        for (i in 0 until secondArrayList.size) {
//                            if (hashMap[secondArrayList[i].wkid] != null) {
//                                var indexList = hashMap[secondArrayList.get(i).wkid]
//                                indexList!!.add(i)
//                                hashMap[secondArrayList.get(i).wkid] = indexList!!
//                            } else {
//                                var indexList: MutableList<Int> = ArrayList()
//                                indexList.add(i)
//                                hashMap[secondArrayList.get(i).wkid] = indexList
//                            }
//                        }
//                        Log.e("HashMap", "" + hashMap.toString())

//                        for (item in hashMap){
//
//                            var indexList: MutableList<Int> = ArrayList()
//                            indexList = item.value
//                            Log.e("HashMap Item", "" + item.key + " " +  indexList)
//
//                            var month = ""
//                            var reason = ""
//                            var userID = ""
//                            var document = ""
//                            var wkid = ""
//                            for (item in indexList){
//                                var t1 = firstArrayList[item]
////                                if(t1.sern == "1" ){ //month no
////                                  month = t1.data
////                                }
////                                if(t1.sern == "2"){ //reason
////                                    reason = t1.data
////                                }
////                                if(t1.sern == "3"){ //user id
////                                    userID = t1.data
////                                }
////                                if(t1.sern == "4"){ //document
////                                    document = t1.data
////                                }
//                                if(t1.labl.equals("Month No",true)){ //month no
//                                    month = t1.data
//                                }
//                                if(t1.labl.equals("Reason",true)){ //reason
//                                    reason = t1.data
//                                }
//                                if(t1.labl.equals("User Id",true)){ //user id
//                                    userID = t1.data
//                                }
//                                if(t1.labl.equals("Document",true)){ //document
//                                    document = t1.data
//                                }
//                                wkid = t1.wkid
//
//                            }
//                            val v2 = HAData(
//                                "" + month,
//                                "" + reason,
//                                "" + userID,
//                                "" + document,
//                                "" + wkid
//                            )
//                            myPendingsArrayList.add(v2)
//                            Log.e("WF", "" + v2)
//                        }



//                        totalPages = result.data!!.last_page.toString()
//                        Log.e("result", "" + result.data!!.current_page)

                    }else {

                       // Toast.makeText(this@PendingRequestsActivity, "" + result.message, Toast.LENGTH_SHORT)
                         //       .show()
                    }


                    setupRecyclerView()


                }catch (e: Exception){

 //                   Utils.stopShimmerRL(shimmerLayout, rootLayout)
                   // Log.e("error", "" + e.message)
//                    if(e.message == "Connection reset" || e.message == "Failed to connect to /40.123.199.239:3000"){
//
//                    } else
                    if (e is SANEDError) {
                       // Log.e("Err", "" + e.getErrorResponse())
                        if (e.getResponseCode() == 401) {
                           // Utils.logoutFromApp(applicationContext)
                        } else if (e.getResponseCode() == 500) {
                            //Toast.makeText(applicationContext, "Server error", Toast.LENGTH_LONG).show()
                        }
                    } else {
                       // Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                        //        .show()
                    }

                }

            }
        }





    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        myPendingAdapter =
                WorkItemsAdapter(myPendingsArrayList, context=requireContext())
        recyclerView.adapter = myPendingAdapter
    }










    companion object {
        fun create(): WorkItemsFragment {
            return WorkItemsFragment()
        }
    }
}