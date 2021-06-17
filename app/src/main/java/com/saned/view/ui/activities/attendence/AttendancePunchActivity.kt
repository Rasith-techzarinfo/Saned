package com.saned.view.ui.activities.attendence

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.material.button.MaterialButton
import com.saned.R
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.error.SANEDError
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.get24FormattedTime
import com.saned.view.utils.Utils.Companion.getFormattedDate
import com.saned.view.utils.Utils.Companion.openActivity
import kotlinx.android.synthetic.main.activity_attendance_punch.*
import kotlinx.android.synthetic.main.activity_attendance_punch.swipeRefreshLayout
import kotlinx.android.synthetic.main.activity_attendance_punch.toolbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_my_employees.*
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executor


class AttendancePunchActivity : AppCompatActivity() {
    private lateinit var executor: Executor
    lateinit var latLong : Location
    var inOffice = false
    var type = ""
    lateinit var clockHandler: Handler
    var stop: Boolean = false
    lateinit var LocationText:TextView
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var googleApiClient: GoogleApiClient? = null
    private val REQUESTLOCATION = 199

    var t_emno: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_punch)
        executor = ContextCompat.getMainExecutor(this)
        LocationText=findViewById(R.id.locationTextView)
        setToolBar()
        init()

    }

    private fun init() {
        //fetch location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        //set data
        currentTime.text = get24FormattedTime()
        currentDate.text = getFormattedDate()
        startClock()
      //t_emno =  prefHelper.getEmpNo().toString()
        Log.e("arjun","hello" + prefHelper.getEmpNo().toString())
        submitButton.isEnabled = true

        //click listeners
        submitButton.setOnClickListener {
//            if(!inOffice){
//                LocationText.text = "Location: " + "You're not in office reach"
//                Snackbar.make(parentLayout, "You're not in office reach", Snackbar.LENGTH_LONG).show()
//                submitButton.isEnabled = false
//                return@setOnClickListener
//            }

            if(submitButton.text.toString() == "TIME IN"){

                val executor = ContextCompat.getMainExecutor(this)
                val biometricPrompt = BiometricPrompt(
                    this,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {

                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                                // user clicked negative button
                            } else {


                            }
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            val ct = currentTime.text.toString()
                            val ti = findViewById(R.id.timeIN) as TextView
                            ti.setText(ct)
                            sendDataToServer()
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()

                            Toast.makeText(
                                    this@AttendancePunchActivity,
                                    "Can't authorized",
                                    Toast.LENGTH_SHORT
                            ).show()

                        }
                    })

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Use Biometric Authentication")
                    .setSubtitle("Choose option to Punch")
                    .setDescription("Scan Your Biometric")
                    .setNegativeButtonText("Cancel")
                    .build()
                biometricPrompt.authenticate(promptInfo)
            } else {
                alertOutDialog()
            }

        }
        //swipe
        swipeRefreshLayout.setOnRefreshListener {
            getLastLocation()
            swipeRefreshLayout.isRefreshing = false
        }
        iconLayout.setOnClickListener {
            openActivity(AttendanceHistoryActivity::class.java, this){}
        }
    }


    private fun startClock() {
        clockHandler = Handler(mainLooper)
        clockHandler.postDelayed(object : Runnable {
            override fun run() {
                if (!stop) {
                    currentTime.text = get24FormattedTime()
                    clockHandler.postDelayed(this, 1000)
                }
            }
        }, 10)
    }

    //TIME OUT DIALOG
    private fun alertOutDialog() {
        val mDialog = Dialog(this)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        mDialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        mDialog.setContentView(R.layout.exit_layout)
        mDialog.setCancelable(false)
        val messageTextView = mDialog.findViewById(R.id.internet_text) as TextView
        val okayButton = mDialog.findViewById(R.id.okay_button) as MaterialButton
        val cancelButton = mDialog.findViewById(R.id.cancel_button) as MaterialButton
        messageTextView.text = "Are you sure want to Time Out?"

        okayButton.setOnClickListener {
            val executor = ContextCompat.getMainExecutor(this)
            val biometricPrompt = BiometricPrompt(
                    this,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {

                        } else {

                        }
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        val ct = currentTime.text.toString()
                        val to = findViewById(R.id.timeOUT) as TextView
                        to.setText(ct)
                        sendDataToServer2()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()

                        Toast.makeText(
                                this@AttendancePunchActivity,
                                "Can't authorized",
                                Toast.LENGTH_SHORT
                        ).show()


                    }
                })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Use Biometric Authentication")
                .setSubtitle("Choose option to Punch")
                .setDescription("Scan Your Biometric")
                .setNegativeButtonText("Cancel")
                .build()
            biometricPrompt.authenticate(promptInfo)
            mDialog.dismiss()
        }

        cancelButton.setOnClickListener {
            mDialog.dismiss()
        }
        mDialog.show()
    }

    private fun sendDataToServer2() {
        if (Utils.isInternetAvailable(this)) {

            //custom progress dialog
            val progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setContentView(R.layout.custom_progress_dialog_layout)
            progressDialog.setCancelable(false)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()


            if(submitButton.text.toString() == "TIME OUT"){
                type = "TIMEOUT"
                submitButton.text = "TIME IN"
            } else {
                //type = "TIMEOUT"
               // submitButton.text = "TIME IN"
            }


            //  progressDialog.dismiss()
//            val locationnBody: RequestBody =
//                    RequestBody.create(
//                            MediaType.parse("text/plain"),
//                            //  subjectEditText.text.toString()
//                            currentlo.text.toString()
//                    )
//
//            val intimeBody: RequestBody =
//                    RequestBody.create(
//                            MediaType.parse("text/plain"),
//                            // prioritySpinnerSelectedInt.toString()
//                            //timeIN.text.toString() + " " + currentDate.text.toString()
//                            wokringHrs.text.toString()
//                    )
//
//            val outtimeeBody: RequestBody =
//                    RequestBody.create(
//                            MediaType.parse("text/plain"),
//                            // categorySpinnerSelectedInt.toString()
//                            currentDate.text.toString()  + " " + currentTime.text.toString()
//                    )
//
//            val empiddBody: RequestBody =
//                    RequestBody.create(
//                            MediaType.parse("text/plain"),
//                            //messageEditText.text.toString())
//                            prefHelper.getEmpNo().toString()
//
//                    )

            val hashMap: HashMap<String, String> = HashMap()

            hashMap["location"] = "" + currentlo.text.toString()
            hashMap["in_time"] = "" + wokringHrs.text.toString()
            hashMap["out_time"] = "" + currentDate.text.toString()  + " " + currentTime.text.toString()
            hashMap["emp_id"] = "" + prefHelper.getEmpNo().toString()



            coroutineScope.launch {

                try {

                    var result = apiService.attendancePunch(hashMap).await()
                    Log.e("arjun","hello" + locationTextView.text.toString())
                    Log.e("arjun","hello" + wokringHrs.text.toString())
                    Log.e("arjun","hello" + currentDate.text.toString()  + " " + currentTime.text.toString())
                    Log.e("arjun","hello" + prefHelper.getEmpNo().toString())

                    Log.e("result", "" + result)


                    if (result.success == "1") {

                        //on success
                        //  res = "true"
                       // onBackPressed()
                        Toast.makeText(
                                this@AttendancePunchActivity,
                                "Checked Out Successfully",
                                Toast.LENGTH_SHORT
                        ).show()


                    } else {

                        Toast.makeText(
                                this@AttendancePunchActivity,
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

    private fun sendDataToServer() {
        if (Utils.isInternetAvailable(this)) {

            //custom progress dialog
            val progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setContentView(R.layout.custom_progress_dialog_layout)
            progressDialog.setCancelable(false)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()


            if(submitButton.text.toString() == "TIME IN"){
                type = "TIMEIN"
                submitButton.text = "TIME OUT"
            } else {
                type = "TIMEOUT"
               // submitButton.text = "TIME IN"
            }


          //  progressDialog.dismiss()
//            val locationBody: RequestBody =
//                    RequestBody.create(
//                            MediaType.parse("text/plain"),
//                            //  subjectEditText.text.toString()
//                    currentlo.text.toString()
//
//                    )
//            val intimeBody: RequestBody =
//                    RequestBody.create(
//                            MediaType.parse("text/plain"),
//                            // prioritySpinnerSelectedInt.toString()
//                            //timeIN.text.toString() + " " + currentDate.text.toString()
//                    currentDate.text.toString()  + " " + currentTime.text.toString()
//                    )
//            val outtimeBody: RequestBody =
//                    RequestBody.create(
//                            MediaType.parse("text/plain"),
//                            // categorySpinnerSelectedInt.toString()
//                            wokringHrs.text.toString()
//                    )
//
//            val empidBody: RequestBody =
//                    RequestBody.create(
//                            MediaType.parse("text/plain"),
//                            //messageEditText.text.toString())
//                            prefHelper.getEmpNo().toString()
//
//                    )


            val hashMap: HashMap<String, String> = HashMap()

            hashMap["location"] = "" + currentlo.text.toString()
            hashMap["in_time"] = "" + currentDate.text.toString()  + " " + currentTime.text.toString()
            hashMap["out_time"] = "" + wokringHrs.text.toString()
            hashMap["emp_id"] = "" + prefHelper.getEmpNo().toString()

            coroutineScope.launch {

                try {

                    var result = apiService.attendancePunch(hashMap).await()

                    Log.e("arjun","hello" + locationTextView.text.toString())
                    Log.e("arjun","hello" + wokringHrs.text.toString())
                    Log.e("arjun","hello" + currentDate.text.toString()  + " " + currentTime.text.toString())
                    Log.e("arjun","hello" + prefHelper.getEmpNo().toString())

                    Log.e("result", "" + result)


                    if (result.success == "1") {

                        //on success
                      //  res = "true"
                      //  onBackPressed()
                        Toast.makeText(
                                this@AttendancePunchActivity,
                                "Checked In Successfully",
                                Toast.LENGTH_SHORT
                        ).show()


                    } else {

                        Toast.makeText(
                                this@AttendancePunchActivity,
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

    private fun getDataFromService() {
        if (Utils.isInternetAvailable(this)) {

            //custom progress dialog
            val progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setContentView(R.layout.custom_progress_dialog_layout)
            progressDialog.setCancelable(false)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            t_emno = prefHelper.getEmpNo().toString()

            coroutineScope.launch {
                try {

                    val result = apiService.getEmpLocation(prefHelper.getEmpNo().toString()).await()

                    Log.e("result", "" + result)

                    if (result.success == "1") {

                        if (result.location == "Riyadh"){

                            submitButton.isEnabled = true
                        } else
                        {
                            submitButton.isEnabled = false
                        }

                    } else {

                        Toast.makeText(this@AttendancePunchActivity, "" + result.message, Toast.LENGTH_SHORT)
                                .show()
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
                        )
                                .show()
                    }
                }
            }




//            inOffice = true
//            if(inOffice){
//                //check if in office range, then enable btn
//                submitButton.isEnabled = true
//            } else {
//                submitButton.isEnabled = false
//            }

           // progressDialog.dismiss()


        } else {
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onPause() {
        super.onPause()
        stop = true
    }

    override fun onResume() {
        super.onResume()
        stop = false
        //remove & restart handler
        clockHandler.removeCallbacksAndMessages(null)
        startClock()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(clockHandler != null) {
            clockHandler.removeCallbacksAndMessages(null)
        }
    }
    private fun checkLocationEnabled() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {}
                    override fun onConnectionSuspended(i: Int) {
                        googleApiClient?.connect()
                    }
                })
                .addOnConnectionFailedListener {
                }.build()
        googleApiClient?.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 30 * 1000.toLong()
        locationRequest.fastestInterval = 5 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result: PendingResult<LocationSettingsResult> =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    status.startResolutionForResult(
                        this,
                        REQUESTLOCATION
                    )
                } catch (e: IntentSender.SendIntentException) {
                }
            }
        }
    }



    private fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getLastLocation(){
//        if(CheckPermission()){
        if(isLocationEnabled()){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
                var location: Location? = task.result
                if(location == null){
                    NewLocationData()
                }else{
                    latLong = location
                    Log.e("Location", "" + "${latLong.latitude} ${latLong.longitude}")
                    locationTextView.text = "" + getLocationAddress(
                        latLong.latitude,
                        latLong.longitude
                    )
                    //getDataFromService()
                }
            }
        }else{
            Toast.makeText(this, "Please Turn on Your device Location or refresh", Toast.LENGTH_SHORT).show()
            // RequestPermission()
            checkLocationEnabled()
        }
//
    }

    private fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest, locationCallback!!, Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            latLong = lastLocation
            Log.e("Location", "" + "${latLong.latitude} ${latLong.longitude}")
            locationTextView.text = "" + getLocationAddress(
                latLong.latitude,
                latLong.longitude
            )
            //getDataFromService()
        }
    }

    private fun getLocationAddress(lat: Double, long: Double) : String {
        var geoCoder = Geocoder(this)
        var address = geoCoder.getFromLocation(lat, long, 1)

//        Log.e("Location","Your City: " + cityName + " your Country " + countryName + " your street: " + street)

        return address[0].getAddressLine(0)
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUESTLOCATION -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.e("abc", "OK")

                    getLastLocation()
                }
                Activity.RESULT_CANCELED -> {
                    Log.e("abc", "CANCEL")

                    getLastLocation()
                }
            }
        }
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

//    override fun onBackPressed() {
//
//        finishAfterTransition()
//    }



}


