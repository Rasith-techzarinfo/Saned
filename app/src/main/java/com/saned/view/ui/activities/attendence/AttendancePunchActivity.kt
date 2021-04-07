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
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.saned.R
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.get24FormattedTime
import com.saned.view.utils.Utils.Companion.getFormattedDate
import com.saned.view.utils.Utils.Companion.openActivity
import kotlinx.android.synthetic.main.activity_attendance_punch.*
import kotlinx.android.synthetic.main.activity_attendance_punch.swipeRefreshLayout
import kotlinx.android.synthetic.main.activity_attendance_punch.toolbar
import kotlinx.android.synthetic.main.activity_my_employees.*
import java.text.SimpleDateFormat
import java.util.*


class AttendancePunchActivity : AppCompatActivity() {

    lateinit var latLong : Location
    var inOffice = false
    var type = ""
    lateinit var clockHandler: Handler
    var stop: Boolean = false
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var googleApiClient: GoogleApiClient? = null
    private val REQUESTLOCATION = 199

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_punch)
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

        //click listeners
        submitButton.setOnClickListener {
            if(!inOffice){
                locationTextView.text = "Location: " + "You're not in office reach"
                Snackbar.make(parentLayout, "You're not in office reach", Snackbar.LENGTH_LONG).show()
                submitButton.isEnabled = false
                return@setOnClickListener
            }

            if(submitButton.text.toString() == "TIME IN"){
                sendDataToServer()
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
                if(!stop) {
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
            sendDataToServer()
            mDialog.dismiss()
        }

        cancelButton.setOnClickListener {
            mDialog.dismiss()
        }
        mDialog.show()
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
                submitButton.text = "TIME IN"
            }

            progressDialog.dismiss()


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

            inOffice = true
            if(inOffice){
                //check if in office range, then enable btn
                submitButton.isEnabled = true
            } else {
                submitButton.isEnabled = false
            }

            progressDialog.dismiss()


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
            LocationManager.NETWORK_PROVIDER)
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
                        Log.e("Location","" + "${latLong.latitude} ${latLong.longitude}")
                        locationTextView.text = "Location: " + getLocationAddress(latLong.latitude, latLong.longitude)
                        getDataFromService()
                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
               // RequestPermission()
                checkLocationEnabled()
            }
//        }else{
//            RequestPermission()
//        }
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
            Log.e("Location","" + "${latLong.latitude} ${latLong.longitude}")
            locationTextView.text = "Location: " + getLocationAddress(latLong.latitude, latLong.longitude)
            getDataFromService()
        }
    }

    private fun getLocationAddress(lat: Double,long: Double) : String {
        var cityName:String = ""
        var countryName = ""
        var street = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var address = geoCoder.getFromLocation(lat,long,1)
        Log.e("Location", "" + address)
        cityName = address[0].locality
        countryName = address[0].countryName
        street = address[0].getAddressLine(0)
//        Log.e("Location","Your City: " + cityName + " your Country " + countryName + " your street: " + street)

        return street
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
                    Log.e("abc","OK")
                    getLastLocation()
                }
                Activity.RESULT_CANCELED -> {
                    Log.e("abc","CANCEL")
                    checkLocationEnabled()
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

    override fun onBackPressed() {

        finishAfterTransition()
    }
}