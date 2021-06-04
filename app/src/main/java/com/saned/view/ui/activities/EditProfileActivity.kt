package com.saned.view.ui.activities

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.saned.R
import com.saned.sanedApplication
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.error.SANEDError
import com.saned.view.service.ConnectivityReceiver
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivity
import kotlinx.android.synthetic.main.activity_create_dynamic_w_f.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.submitButton
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.toolbar
import kotlinx.android.synthetic.main.activity_spinner_list.view.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS


class EditProfileActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {


    private var networkDialog : Dialog? = null

     var res: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        spinner()
        setToolBar()
        init()
        val dob=findViewById(R.id.dobEditText) as TextInputEditText
        val doj=findViewById(R.id.dojEditText) as TextInputEditText
        val ld=findViewById(R.id.lastdateEditText) as TextInputEditText
        dob.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                dobEditText.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)

            }, year, month, day)

            dpd.show()

        }
        doj.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                dojEditText.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)

            }, year, month, day)

            dpd.show()

        }
        ld.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                lastdateEditText.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)

            }, year, month, day)

            dpd.show()

        }

    }






    private fun init() {
        //network receiver
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        //get profile data
        getMyProfileData()
        passEditText.setText("" + prefHelper.getUserPassword())

        //btn listeners
        passLayout.setOnClickListener {
            openActivity(ChangePasswordActivity::class.java, this){}
        }
        passLayout.setEndIconOnClickListener {
            openActivity(ChangePasswordActivity::class.java, this){}
        }
        submitButton.setOnClickListener {
            // validate all fields
            if (firstNameEditText.text.toString() == "") {

                Toast.makeText(this, "Enter the Firstname", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            if (lastNameEditText.text.toString() == "") {
//
//                Snackbar.make(rootLayout, "Enter the Lastname", Snackbar.LENGTH_LONG).show()
//                return@setOnClickListener
//            }
            
//            if (phoneEditText.text.toString() == "") {
//
//                Snackbar.make(rootLayout, "Enter the Phone", Snackbar.LENGTH_LONG).show()
//                return@setOnClickListener
//            }
            
            sendDataToServer()
        }
    }

    private fun sendDataToServer() {

        if (Utils.isInternetAvailable(this)) {


            // Custom Progress dialog
            val progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setContentView(R.layout.custom_progress_dialog_layout)
            progressDialog.setCancelable(false)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()


            val hashMap: HashMap<String, String> = HashMap()

            hashMap["fnme"] = "" + firstNameEditText.text.toString()
            hashMap["lnme"] = "" + lastNameEditText.text.toString()
            hashMap["mnme"] = "" + middleNameEditText.text.toString()
            hashMap["email"] = "" + lastNameEditText.text.toString()
            hashMap["dob"] = "" + dobEditText.text.toString()
            hashMap["a_name"] = "" + arabicNameEditText.text.toString()
            hashMap["ccty"] = "" + nationalityEditText.text.toString()
            hashMap["phon"] = "" + phoneEditText.text.toString()
            //hashMap["last_name"] = "" + lastNameEditText.text.toString()
           // hashMap["phone"] = "" + phoneEditText.text.toString()


            coroutineScope.launch {

                try {

                    var result = sanedApplication.apiService.editProfile(hashMap).await()

                    Log.e("result", "" + result)


                    if (result.success == "1") {

                        //navigateback
                        res = "true"
                        onBackPressed()
                        Toast.makeText(
                                this@EditProfileActivity,
                                "Profile Updated",
                                Toast.LENGTH_SHORT
                        ).show()


                    } else {

                        Toast.makeText(
                                this@EditProfileActivity,
                                "" + result.message,
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                    progressDialog.dismiss()

                } catch (e: Exception) {
                    progressDialog.dismiss()
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

    private fun getMyProfileData() {
        if (Utils.isInternetAvailable(this)) {

            //custom progress dialog
            val progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setContentView(R.layout.custom_progress_dialog_layout)
            progressDialog.setCancelable(false)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            coroutineScope.launch {
                try {
                    val result = sanedApplication.apiService.getProfileData().await()
                    Log.e("result", "" + result)

                    if (result.success == "1") {


                        firstNameEditText.setText("" + result.user!!.t_nama)
                        lastNameEditText.setText("" + result.user!!.t_mail)
                       // lastNameEditText.setText("" + result.user!!.last_name)
                       // phoneEditText.setText("" + result.user!!.phone)

                        // no profile for now
//                        if (result.user!!.profile_pic != null) {
//
//                            Glide.with(this@ProfileActivity).load(BASE_URL + result.user.profile_pic).placeholder(
//                                    R.drawable.ic_user
//                            ).into(profileImage)
//                        }


                    } else {
                        Toast.makeText(this@EditProfileActivity, "" + result.message, Toast.LENGTH_SHORT).show()
                    }

                    progressDialog.dismiss()

                } catch (e: Exception) {
                    progressDialog.dismiss()
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
//            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
            Utils.checkNetworkDialog(this, this) { getMyProfileData() }
        }
    }




    override fun onNetworkConnectionChanged(isConnected: Boolean) {
//        showNetworkMessage(isConnected)
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
            getMyProfileData()
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

    private fun setToolBar() {
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
    }


    override fun onSupportNavigateUp(): Boolean {

        var intent = Intent()
        intent.putExtra("isUpdated", "" + res)
        setResult(RESULT_OK, intent)
        finishAfterTransition()
        return true
    }

    override fun onBackPressed() {

        var intent = Intent()
        intent.putExtra("isUpdated", "" + res)
        setResult(RESULT_OK, intent)
        finishAfterTransition()
    }
    fun spinner() {
        // access the items of the list
        val gender = resources.getStringArray(R.array.gender)
        val religion = resources.getStringArray(R.array.religion)
        val job = resources.getStringArray(R.array.job)
        val department = resources.getStringArray(R.array.department)
        val gosi = resources.getStringArray(R.array.gosi)
        val grade = resources.getStringArray(R.array.grade)

        // access the spinner
        val spinner = findViewById<Spinner>(R.id.genderEditText)
        val spinner2 = findViewById<Spinner>(R.id.religionEditText)
        val spinner3 = findViewById<Spinner>(R.id.jobTitleEditText)
        val spinner4 = findViewById<Spinner>(R.id.departmentEditText)
        val spinner5 = findViewById<Spinner>(R.id.gosiEditText)
        val spinner6 = findViewById<Spinner>(R.id.gradeEditText)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, gender)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        if (spinner2 != null) {
            val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, religion)
            spinner2.adapter = adapter

            spinner2.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        if (spinner3 != null) {
            val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, job)
            spinner3.adapter = adapter

            spinner3.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        if (spinner4 != null) {
            val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, department)
            spinner4.adapter = adapter

            spinner4.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        if (spinner5 != null) {
            val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, gosi)
            spinner5.adapter = adapter

            spinner5.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        if (spinner6 != null) {
            val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, grade)
            spinner6.adapter = adapter

            spinner6.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }
        }
