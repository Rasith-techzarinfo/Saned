package com.saned.view.ui.activities.attendence

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.saned.R
import com.saned.model.AttenHistoryDetail
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.view.error.SANEDError
import com.saned.view.ui.adapter.attendence.AttendenceHistoryAdapter
import com.saned.view.utils.Utils
import kotlinx.android.synthetic.main.activity_attendance_history.*
import kotlinx.android.synthetic.main.activity_attendance_history.recyclerView
import kotlinx.android.synthetic.main.activity_attendance_history.rootLayout
import kotlinx.android.synthetic.main.activity_attendance_history.shimmerLayout
import kotlinx.android.synthetic.main.activity_attendance_history.swipeRefreshLayout
import kotlinx.android.synthetic.main.activity_attendance_history.toolbar
import kotlinx.android.synthetic.main.activity_attendance_punch.*
import kotlinx.android.synthetic.main.activity_history_dynamic_w_f.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class AttendanceHistoryActivity : AppCompatActivity(), AttendenceHistoryAdapter.ListAdapterListener {

    var attendenceHistoryArrayList: ArrayList<AttenHistoryDetail> = ArrayList()
    lateinit var attendenceHistoryAdapter: AttendenceHistoryAdapter

    var day: Int = 0

    var from_date: String = ""
    var to_date: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_history)
        showReportDateDialog()
        setToolBar()
        init()
    }



    private fun init() {
        //listeners
        swipeRefreshLayout.setOnRefreshListener {
           // getValues()
           // getvaluefromserver(from_date,to_date)
            swipeRefreshLayout.isRefreshing = false
        }
        //get data
        //getValues()
      //  getvaluefromserver(from_date,to_date)

        add_date_fab.setOnClickListener {
            //send form data to new activity
            showReportDateDialog()
            }


    }

    private fun showReportDateDialog() {

        val alert = AlertDialog.Builder(this@AttendanceHistoryActivity)
        val inflater: LayoutInflater = this@AttendanceHistoryActivity.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.date_filter_dialog, null)
        alert.setView(dialogView)
        alert.setTitle("Select Date")
        val alertDialog = alert.create()
        val img_remove = dialogView.findViewById<View>(R.id.img_close) as ImageView
        val txt_from_date = dialogView.findViewById<View>(R.id.txt_from_date) as TextView
        val txt_to_date = dialogView.findViewById<View>(R.id.txt_to_date) as TextView
        val btn_submit = dialogView.findViewById<View>(R.id.btn_submit) as Button
        img_remove.setOnClickListener { alertDialog.dismiss() }
        txt_from_date.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                txt_from_date.setText("" + dayOfMonth + "-" + monthOfYear + "-" + year)

            }, year, month, day)

            dpd.show()
        }
        txt_to_date.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                txt_to_date.setText("" + dayOfMonth + "-" + monthOfYear + "-" + year)

            }, year, month, day)

            dpd.show()
        }
        btn_submit.setOnClickListener {
            from_date = txt_from_date.text.toString()
            to_date = txt_to_date.text.toString()
            if (from_date != "Select From Date" && from_date.length != 0) {
                if (to_date != "Select To Date" && to_date.length != 0) {
                    if (Utils.isInternetAvailable(this@AttendanceHistoryActivity) === true) {
                        alertDialog.dismiss()

                        Log.e("arjun","a" +from_date + to_date)
                       // from_date = Utils.changeDateFormat(from_date)
                       // to_date = Utils.changeDateFormat(to_date)
//                        com.zconstruction.reportnew.LaborFragment.reportSelectedDate = ""
//                        laborList.clear()
                        getvaluefromserver(from_date, to_date)

                        // new LaborForm.syncLaborReport().execute(from_date, to_date);
                    } else {
                        //Utils.showMessageDialog(getActivity(), "Internet unavailable")
                    }
                } else {
                    //Utils.showMessageDialog(getActivity(), "Select To Date")
                }
            } else {
              //  Utils.showMessageDialog(getActivity(), "Select From Date")
            }
        }
        if (!(this@AttendanceHistoryActivity as Activity).isFinishing) {
            alertDialog.show()
        }
    }

    private fun getvaluefromserver(fromDate: String, toDate: String) {

        if (Utils.isInternetAvailable(this)) {

            //custom progress dialog
            Utils.startShimmerRL(shimmerLayout, rootLayout)



            val hashMap: HashMap<String, String> = HashMap()

            hashMap["from"] = "" + fromDate
            hashMap["to"] = "" + toDate




            coroutineScope.launch {

                try {

                    var result = apiService.attendanceSearch(hashMap).await()


                    Log.e("result", "" + result)


                    if (result.success == "1") {


                        //on success
                        //  res = "true"
                        // onBackPressed()
                        for (item in result.data!!) {

                            val v1 = AttenHistoryDetail(
                                    "" + item.id,
                                    "" + item.location,
                                    "" + item.in_time,
                                    "" + item.out_time,
                                    "" + item.emp_id,
                                    "" + item.date,
                                    "" + item.working_hours


                            )
                            attendenceHistoryArrayList.add(v1)

                        }



                        } else {

                        Toast.makeText(
                                this@AttendanceHistoryActivity,
                                "" + result.message,
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
                    setupRecyclerView()

                } catch (e: Exception) {
                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
                    Log.e("error", "" + e.message)
                    if (e is SANEDError) {
                        Log.e("Err", "" + e.getErrorResponse())
                        if (e.getResponseCode() == 401) {
                            Utils.logoutFromApp(applicationContext)
                        } else if (e.getResponseCode() == 500) {
                            Toast.makeText(applicationContext, "Server error", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(
                                applicationContext,
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }



        } else {
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
        }
    }



    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        attendenceHistoryAdapter =  AttendenceHistoryAdapter(attendenceHistoryArrayList, this, this)
        recyclerView.adapter = attendenceHistoryAdapter

    }


    override fun onListItemClicked(dummyData: AttenHistoryDetail, position: Int) {

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