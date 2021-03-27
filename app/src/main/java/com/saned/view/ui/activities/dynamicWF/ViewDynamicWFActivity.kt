package com.saned.view.ui.activities.dynamicWF

import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.saned.R
import com.saned.model.*
import com.saned.sanedApplication
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.error.SANEDError
import com.saned.view.ui.activities.DocumentViewerActivity
import com.saned.view.ui.activities.SliderDemo
import com.saned.view.utils.Constants
import com.saned.view.utils.Utils
import com.saned.view.utils.Utils.Companion.openActivity
import kotlinx.android.synthetic.main.activity_history_dynamic_w_f.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_view_dynamic_w_f.*
import kotlinx.android.synthetic.main.activity_view_dynamic_w_f.emptyView
import kotlinx.android.synthetic.main.activity_view_dynamic_w_f.rootLayout
import kotlinx.android.synthetic.main.activity_view_dynamic_w_f.shimmerLayout
import kotlinx.android.synthetic.main.activity_view_dynamic_w_f.toolbar
import kotlinx.android.synthetic.main.activity_view_dynamic_w_f.toolbarTitle
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.RegExp
import org.jetbrains.anko.textColor
import java.io.File
import java.lang.Exception

class ViewDynamicWFActivity : AppCompatActivity() {


    var formID: String = ""
    var formName: String = ""
    var wkid: String = ""
    lateinit var haDetail: HAData
    var res: String = ""
    var listImages = ArrayList<String>()

    //dynamic view page needed
    // static for now
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_dynamic_w_f)
        setToolBar()
        init()
    }



    private fun getDataFromServer() {
        if (Utils.isInternetAvailable(this)) {

            Utils.startShimmerRL(shimmerLayout, rootLayout)
            emptyView.visibility = View.GONE

            coroutineScope.launch {
                try {
                    val result = apiService.getHousingWFDetail(wkid).await()
                    Log.e("result", "" + result)

                    if (result.success == "1") {


                        var firstArrayList: ArrayList<HousingWFData> = ArrayList()
                        var secondArrayList: ArrayList<HAData1> = ArrayList()

                        for (item in result.data!!) {

                            val v1 = HousingWFData(
                                    "" + item.id,
                                    "" + item.wkid,
                                    "" + item.sern,
                                    "" + item.labl,
                                    "" + item.data,
                                    "" + item.form_name,
                                    "" + item.email
                            )
                            firstArrayList.add(v1)

                            var v2 = HAData1((firstArrayList.size - 1), "" + item.wkid)
                            secondArrayList.add(v2)
                        }

                        val hashMap: HashMap<String, MutableList<Int>> = HashMap()


                        for (i in 0 until secondArrayList.size) {
                            if (hashMap[secondArrayList[i].wkid] != null) {
                                var indexList = hashMap[secondArrayList.get(i).wkid]
                                indexList!!.add(i)
                                hashMap[secondArrayList.get(i).wkid] = indexList!!
                            } else {
                                var indexList: MutableList<Int> = ArrayList()
                                indexList.add(i)
                                hashMap[secondArrayList.get(i).wkid] = indexList
                            }
                        }
                        Log.e("HashMap", "" + hashMap.toString())

                        for (item in hashMap){

                            var indexList: MutableList<Int> = ArrayList()
                            indexList = item.value
                            Log.e("HashMap Item", "" + item.key + " " +  indexList)

                            var month = ""
                            var reason = ""
                            var userID = ""
                            var document = ""
                            var wkid = ""
                            for (item in indexList){
                                var t1 = firstArrayList[item]
                                if(t1.sern == "1" ){ //month no
                                    month = t1.data
                                }
                                if(t1.sern == "2"){ //reason
                                    reason = t1.data
                                }
                                if(t1.sern == "3"){ //user id
                                    userID = t1.data
                                }
                                if(t1.sern == "4"){ //document
                                    document = t1.data
                                }
                                wkid = t1.wkid

                            }
                            haDetail = HAData(
                                    "" + month,
                                    "" + reason,
                                    "" + userID,
                                    "" + document,
                                    "" + wkid
                            )
//                            Log.e("WF", "" + v2)
                        }

                        labelVal1.text = "" + haDetail.noofmonths
                        labelVal2.text = "" + haDetail.reason
                        if(haDetail.document.isEmpty()){
                            label3.visibility = View.GONE
                            attachment_layout.visibility = View.GONE
                        } else {
                            label3.visibility = View.VISIBLE
                            attachment_layout.visibility = View.VISIBLE
                            //show document here
                            showDocuments(haDetail.document)
                        }

                        if(result.approvalstatus == null){
                            statusTextView.text = "Pending"
                            statusTextView.textColor = Color.parseColor("#ffad46")
                        } else if(result.approvalstatus.step == "100"){
                            statusTextView.text = "Approved"
                            statusTextView.textColor = Color.parseColor("#00aa00")
                        } else if(result.approvalstatus.step == "200"){
                            statusTextView.text = "Rejected"
                            statusTextView.textColor = Color.parseColor("#ff0000")
                        }


                        // no profile for now
//                        if (result.user!!.profile_pic != null) {
//
//                            Glide.with(this@ProfileActivity).load(BASE_URL + result.user.profile_pic).placeholder(
//                                    R.drawable.ic_user
//                            ).into(profileImage)
//                        }


                    } else {
                        Toast.makeText(applicationContext, "" + result.message, Toast.LENGTH_SHORT).show()
                    }

                    Utils.stopShimmerRL(shimmerLayout, rootLayout)

                } catch (e: Exception) {
                    Utils.stopShimmerRL(shimmerLayout, rootLayout)
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
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDocuments(documentUrl: String) {
        Log.e("tempUrl", "$documentUrl ${documentUrl.replace("\\", "/") }")
        //full path
        var docUrl = Constants.BASE_URL + documentUrl

        //img preview
        val uploadedImageViewItem = layoutInflater.inflate(
                R.layout.action_custom_image_item, null
        )
        val layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 8, 8, 8)


        //image - check type
        var isImage = (docUrl.endsWith("png") || docUrl.endsWith("jpg") || docUrl.endsWith("jpeg") || docUrl.endsWith("jfif"))
        Log.e("Url" , "" + docUrl + " " + isImage)

        listImages.add("" + docUrl)

        imageLinearLayout!!.addView(uploadedImageViewItem, layoutParams)

        val uploadedImage = uploadedImageViewItem.findViewById(R.id.img_lab_img) as ImageView
        if (!isImage) {
            Glide.with(this)
//               .asBitmap()
                    .load(R.drawable.ic_file_alt)
                    .placeholder(R.drawable.placeholder_bg)
                    .error(R.drawable.placeholder_bg)
                    .into(uploadedImage)
        } else {
            Glide.with(this)
                    .load(docUrl)
                    .placeholder(R.drawable.placeholder_bg)
                    .error(R.drawable.placeholder_bg)
                    .into(uploadedImage)
        }
        //img listener
        uploadedImage.setOnClickListener {
            //img or doc
            if (docUrl.toString().toLowerCase().endsWith("png") || docUrl.toString().toLowerCase()
                            .endsWith("jpg") || docUrl.toString().toLowerCase().endsWith("jpeg") ||  docUrl.toString().toLowerCase().endsWith("jfif")
            ) {

                //handling url
                Log.e("file", "image")

                val fileName = docUrl.substring(docUrl.lastIndexOf('/') + 1, docUrl.length)
                var fileNameWithoutExtn: String =
                        fileName.substring(0, fileName.lastIndexOf('.'))

                var mediaList: ArrayList<String> = ArrayList<String>()
                mediaList.add(docUrl)

                //send form data to new activity
//                openActivity(SliderDemo::class.java, this){
//                    putExtra("list", mediaList)
//                    putString("media", "image")
//                    putString("title", "" + fileNameWithoutExtn)
//                }
                //for now
                val intent = Intent(this, SliderDemo::class.java)
                intent.putExtra("list",  mediaList)
                intent.putExtra("media", "image")
                intent.putExtra("title", "" + fileNameWithoutExtn)
                startActivity(intent)
                overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right)

            } else {
                //document
                Log.e("file", "doc")
                //send form data to new activity
                openActivity(DocumentViewerActivity::class.java, this){
                    putString("url", "" + docUrl)
                }

            }
        }

        //hide close icon
        val removeUploadedImage = uploadedImageViewItem.findViewById(R.id.remove_image) as ImageView
        removeUploadedImage.visibility = View.GONE
    }


    private fun init() {
        //get intent data
        formID = "" + intent.getStringExtra("formID")
        formName = "" + intent.getStringExtra("formName")
        wkid = "" + intent.getStringExtra("wkid")
        Log.e("itt", "" + wkid)
        toolbarTitle.text = formName + " Detail"

        //user permission
        verifyLayout.visibility = if(sanedApplication.prefHelper.getUserType() == "2") View.VISIBLE else View.GONE
        approvalStatusLayout.visibility = if(sanedApplication.prefHelper.getUserType() == "2") View.GONE else View.VISIBLE

        //btn listeners
        submitButton.setOnClickListener {
            //validate
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                val radioButtonValue = findViewById<RadioButton>(selectedId)
                Log.e("RadioValue", "" + radioButtonValue.text)

                var radVal = ""
                if (radioButtonValue.text == "Approve") {
                    radVal = "100"
                } else {
                    radVal = "200"
                }
                //data
                sendDataToServer(radVal)

            } else {
                Toast.makeText(applicationContext, "Select the Approve status", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

        }

        //get data
        getDataFromServer()
    }

    private fun sendDataToServer(status: String) {

        Utils.hideKeyBoard(submitButton, this)

        if (Utils.isInternetAvailable(this)) {


            // Custom Progress dialog
            val progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setContentView(R.layout.custom_progress_dialog_layout)
            progressDialog.setCancelable(false)
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()



            val hashMap: java.util.HashMap<String, String> = java.util.HashMap()

            hashMap["wkid"] = "" + wkid
            hashMap["step"] = "" + status



            coroutineScope.launch {
                try {

                    if(prefHelper.getUserType() != "3") {
                        val result = if (prefHelper.getManagerLevel() == "1") apiService.verifyHAStatus1(hashMap).await() else apiService.verifyHAStatus2(hashMap).await()

                        Log.e("result", "" + result)

                        if (result.success == "1") {

                            //on success
                            res = "true"
                            onBackPressed()
                            Toast.makeText(applicationContext, "Status Updated", Toast.LENGTH_SHORT).show()


                        } else {

                            Toast.makeText(applicationContext, "" + result.message, Toast.LENGTH_SHORT).show()
                        }
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

        } else {
//            Snackbar.make(nested_scroll_view, "No Internet Available", Snackbar.LENGTH_LONG).show()
            Toast.makeText(applicationContext, "No Internet Available", Toast.LENGTH_SHORT).show()
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
}
