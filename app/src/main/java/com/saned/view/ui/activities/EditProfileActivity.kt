package com.saned.view.ui.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.saned.R
import com.saned.sanedApplication
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.view.error.SANEDError
import com.saned.view.service.ConnectivityReceiver
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.submitButton
import kotlinx.android.synthetic.main.activity_profile.toolbar
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*


class EditProfileActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {


    private var networkDialog : Dialog? = null

     var res: String = ""
    var spinnerSelectedInt1: Int = 0
    var spinnerSelectedInt2: Int = 0
    var spinnerSelectedInt3: Int = 0
    var spinnerSelectedInt4: Int = 0
    var spinnerSelectedInt5: Int = 0
    var spinnerSelectedInt6: Int = 0
    private var disposable: Disposable? = null
    var spinnerInt1: Int = 0
    var spinnerInt2: Int = 0
    var spinnerInt3: Int = 0
    var spinnerInt4: Int = 0
    var spinnerInt5: Int = 0
    var spinnerInt6: Int = 0
    var nameString: String = ""
    var emailString: String = ""
    var mobileString: String = ""
    var profileUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        spinner()
        setToolBar()

        addItemsOnSpinner()
        spinnerListener()
        getIntentValues(intent)
        init()
        var dob=findViewById(R.id.dobEditText) as TextInputEditText
        var ld=findViewById(R.id.lastdateEditText) as TextInputEditText
        dob.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                dob.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)

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
                ld.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)

            }, year, month, day)

            dpd.show()

        }

    }






    private fun init() {
        //network receiver
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        //get profile data
        getMyProfileData()
       // arjunpassEditText.setText("" + prefHelper.getUserPassword())

        //btn listeners
        passLayout.setOnClickListener {
            openActivity(ChangePasswordActivity::class.java, this){}
        }
        passLayout.setEndIconOnClickListener {
            openActivity(ChangePasswordActivity::class.java, this){}
        }
        submitButton.setOnClickListener {
            // validate all fields
//           arjun if (firstNameEditText.text.toString() == "") {
//
//                Toast.makeText(this, "Enter the Firstname", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

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
            val fnme: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    firstNameEditText.text.toString()
                )
            val lnme: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    lastNameEditText.text.toString()
                )
            val mnme: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    middleNameEditText.text.toString()
                )
            val a_name: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    arabicNameEditText.text.toString()
                )
            val dob: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    dobEditText.text.toString()
                )
            val gend: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"), spinnerSelectedInt1.toString())
            val ccty: RequestBody =
                    RequestBody.create(
                            MediaType.parse("text/plain"),
                            nationalityEditText.text.toString()
                    )
            val email: RequestBody =
                    RequestBody.create(
                            MediaType.parse("text/plain"),
                            emailEditText.text.toString()
                    )
            val phon: RequestBody =
                    RequestBody.create(
                            MediaType.parse("text/plain"),
                            phoneEditText.text.toString()
                    )
            val relg: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"), spinnerSelectedInt2.toString())
            val emrcnt: RequestBody =
                    RequestBody.create(
                            MediaType.parse("text/plain"),
                            emergencyEditText.text.toString()
                    )
            val emp_code: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    empcodeEditText.text.toString()
                )
            val f_name: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    fullNameEditText.text.toString()
                )
            val dept: RequestBody =
                    RequestBody.create(MediaType.parse("text/plain"), spinnerSelectedInt4.toString())
            val jbtl: RequestBody =
                    RequestBody.create(MediaType.parse("text/plain"), spinnerSelectedInt3.toString())
            val basic: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    basicEditText.text.toString()
                )
            val hous: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    housingEditText.text.toString()
                )
            val ldate: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    lastdateEditText.text.toString()
                )
            val grade: RequestBody =
                    RequestBody.create(MediaType.parse("text/plain"), spinnerSelectedInt5.toString())
            val idno: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    idEditText.text.toString()
                )
            val idex: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    idexpiryEditText.text.toString()
                )
            val pspt: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    passEditText.text.toString()
                )
            val psptex: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    passexpiryEditText.text.toString()
                )
            val subdep: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    subDeptEditText.text.toString()
                )
            val id: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    idempEditText.text.toString()
                )
            val mart: RequestBody =
                    RequestBody.create(MediaType.parse("text/plain"), spinnerSelectedInt6.toString())
            val city: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    cityEditText.text.toString()
                )
            val loca: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    locationEditText.text.toString()
                )
            val tran: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    transportEditText.text.toString()
                )
            val cont: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    contractstsEditText.text.toString()
                )
            val medc: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    medicalEditText.text.toString()
                )
            coroutineScope.launch {

                try {

                    var result = sanedApplication.apiService.editProfile(
                    fnme, lnme, mnme, a_name, dob, gend, ccty, email, phon, relg, emrcnt, emp_code, f_name, dept, jbtl, basic, hous, ldate, grade, idno, idex, pspt, psptex, subdep, id, mart, city, loca, tran, cont, medc

                    ).await()

                    Log.e("result", "" + result)


                    if (result.success == "1") {

                        res = "true"
                        Toast.makeText(
                                applicationContext,
                                "Profile Updated",
                                Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()

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
    private fun addItemsOnSpinner() {

        val list: MutableList<String> = ArrayList()
        list.add("Select Gender")
        list.add("Male")
        list.add("Female")
        list.add("Other")

        val genderAdapter = object : ArrayAdapter<Any>(
                this, R.layout.spinner_sample_one,
                list as List<Any>
        ) {
            override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
            ): View {
                return super.getDropDownView(position, convertView, parent).also { view ->
                    if (position == genderEditText.selectedItemPosition) {
                        // view.setBackgroundColor(resources.getColor(R.color.color_light))
                        view.findViewById<TextView>(android.R.id.text1)
                                .setTextColor(resources.getColor(R.color.colorPrimary))
                        if (position != 0) {
                            view.findViewById<TextView>(android.R.id.text1)
                                    .setCompoundDrawablesWithIntrinsicBounds(
                                            0,
                                            0,
                                            R.drawable.ic_tick_24dp,
                                            0
                                    )

                        } else {
                            view.findViewById<TextView>(android.R.id.text1)
                                    .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        }
                    } else {

                        view.findViewById<TextView>(android.R.id.text1)
                                .setTextColor(resources.getColor(android.R.color.black))
                        view.findViewById<TextView>(android.R.id.text1)
                                .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    }
                }
            }
        }

        genderAdapter.setDropDownViewResource(R.layout.spinner_sample_one)
        genderEditText.adapter = genderAdapter
        val list2: MutableList<String> = ArrayList()
        list2.add("Select religion")
        list2.add("Hindu")
        list2.add("Christian")
        list2.add("Muslim")

        val religionAdapter = object : ArrayAdapter<Any>(
                this, R.layout.spinner_sample_one,
                list as List<Any>
        ) {
            override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
            ): View {
                return super.getDropDownView(position, convertView, parent).also { view ->
                    if (position == religionEditText.selectedItemPosition) {
                        // view.setBackgroundColor(resources.getColor(R.color.color_light))
                        view.findViewById<TextView>(android.R.id.text1)
                                .setTextColor(resources.getColor(R.color.colorPrimary))
                        if (position != 0) {
                            view.findViewById<TextView>(android.R.id.text1)
                                    .setCompoundDrawablesWithIntrinsicBounds(
                                            0,
                                            0,
                                            R.drawable.ic_tick_24dp,
                                            0
                                    )

                        } else {
                            view.findViewById<TextView>(android.R.id.text1)
                                    .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        }
                    } else {

                        view.findViewById<TextView>(android.R.id.text1)
                                .setTextColor(resources.getColor(android.R.color.black))
                        view.findViewById<TextView>(android.R.id.text1)
                                .setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    }
                }
            }
        }

        religionAdapter.setDropDownViewResource(R.layout.spinner_sample_one)
        religionEditText.adapter = genderAdapter
    }

    private fun spinnerListener() {

        genderEditText.setOnTouchListener(View.OnTouchListener { v, event ->
            Utils.hideKeyBoard(genderEditText, this@EditProfileActivity)
            false
        })

        // Set an on item selected listener for spinner object
        genderEditText.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
            ) {

                when (parent.getItemAtPosition(position).toString()) {
                    "Male" -> spinnerSelectedInt1 = 1
                    "Female" -> spinnerSelectedInt1 = 2
                    "Other" -> spinnerSelectedInt1 = 3
                    else -> {
                        spinnerSelectedInt1 = 0

                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }

        }
        religionEditText.setOnTouchListener(View.OnTouchListener { v, event ->
            Utils.hideKeyBoard(genderEditText, this@EditProfileActivity)
            false
        })

        // Set an on item selected listener for spinner object
        religionEditText.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
            ) {

                when (parent.getItemAtPosition(position).toString()) {
                    "Hindu" -> spinnerSelectedInt2 = 1
                    "Christian" -> spinnerSelectedInt2 = 2
                    "Muslim" -> spinnerSelectedInt2 = 3
                    else -> {
                        spinnerSelectedInt2 = 0

                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }

        }
    }


    private fun getIntentValues(intent: Intent?) {

        nameString = "" + intent!!.getStringExtra("name")
        emailString = "" + intent.getStringExtra("email")
        mobileString = "" + intent.getStringExtra("mobile")
        spinnerInt1 = intent.getIntExtra("gender", spinnerInt1)
        profileUrl = "" + intent.getStringExtra("profile_pic")

        setIntentValues()
    }

    private fun setIntentValues() {

        fullNameEditText.setText(nameString)
        emailEditText.setText(emailString)
        phoneEditText.setText(mobileString)
        genderEditText.setSelection(spinnerInt1)
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


//                      arjn  firstNameEditText.setText("" + result.user!!.t_nama)
//                        lastNameEditText.setText("" + result.user!!.t_mail)
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
        val grade = resources.getStringArray(R.array.grade)
        val maritial = resources.getStringArray(R.array.maritial)

        // access the spinner
        val spinner = findViewById<Spinner>(R.id.genderEditText)
        val spinner2 = findViewById<Spinner>(R.id.religionEditText)
        val spinner3 = findViewById<Spinner>(R.id.jobTitleEditText)
        val spinner4 = findViewById<Spinner>(R.id.departmentEditText)
        val spinner6 = findViewById<Spinner>(R.id.gradeEditText)
        val spinner7 = findViewById<Spinner>(R.id.maritialEditText)
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
        if (spinner7 != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, maritial)
            spinner7.adapter = adapter

            spinner7.onItemSelectedListener = object :
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
