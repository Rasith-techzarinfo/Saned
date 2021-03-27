package com.saned.view.ui.activities.dynamicWF

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.google.android.material.snackbar.Snackbar
import com.saned.R
import com.saned.sanedApplication.Companion.apiService
import com.saned.sanedApplication.Companion.coroutineScope
import com.saned.sanedApplication.Companion.prefHelper
import com.saned.view.error.SANEDError
import com.saned.view.utils.URIPathHelper
import com.saned.view.utils.Utils
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.models.sort.SortingTypes
import kotlinx.android.synthetic.main.activity_create_dynamic_w_f.*
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class CreateDynamicWFActivity : AppCompatActivity() {


    var formID: String = ""
    var formName: String = ""

    var monthsSpinnerSelected = ""
    var hashMap: HashMap<String, Int> = HashMap<String, Int>()
    val list: MutableList<String> = ArrayList()
    var listImages = ArrayList<String>()
    private var docPaths: ArrayList<Uri> = ArrayList()
    private val REQUEST_CODE_CHOOSE = 104
    val PICKPHOTO_RESULT_CODE = 1105
    val PICKFILE_RESULT_CODE = 1106


    //dynamic form using fields
    //static for now
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_dynamic_w_f)
        setToolBar()
        init()
    }

//TODO: check spinner action & file picker fix



    private fun init() {
        //get intent data
        formID = "" + intent.getStringExtra("formID")
        formName = "" + intent.getStringExtra("formName")
        Log.e("itt", "" + formID)
        toolbarTitle.text = "New " + formName

        //listeners
        attach_layout.setOnClickListener {
            val mDialog = Dialog(this)
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mDialog.setContentView(R.layout.attachment_dialog_layout)
            mDialog.setCancelable(false)
            val galleryLayout = mDialog.findViewById(R.id.gallery_layout) as LinearLayout
            val documentLayout = mDialog.findViewById(R.id.document_layout) as LinearLayout
            //hidden for now
            galleryLayout.visibility = View.GONE

            galleryLayout.setOnClickListener {
                mDialog.dismiss()
                addPhotoVideoFromDevice()

            }

            documentLayout.setOnClickListener {
                mDialog.dismiss()
                openDocuments()

            }
            mDialog.setCancelable(true)
            mDialog.show()
        }
        //spinner
        addToSpinner()
        //btn listener
        submitButton.setOnClickListener{

            // validate all fields
            if (monthsSpinner.selectedItemPosition == 0) {

                  Toast.makeText(this, "Select No of months", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (reasonEditText.text.toString() == "") {

                Snackbar.make(rootLayout, "Enter the Reason", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            //attachment optional for now


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


            var attachmentArrayList: ArrayList<MultipartBody.Part> = ArrayList()
            var toArrayList: ArrayList<MultipartBody.Part> = ArrayList()

            // image linearlayout
            for (d in 0 until imageLinearLayout.childCount) {
                val image = imageLinearLayout.getChildAt(d)
                val imageName = image.findViewById<TextView>(R.id.image_name)

                Log.e("AttachmentImage:", " " + d + ":  " + imageName.text.toString() + "  ")
                val f = File(imageName.text.toString())

                Log.e("AttachmentImage:", " " + d + ":  " + f.length() + "  ")
                Log.e("AttachmentImage:", " " + d + ":  " + f.absolutePath + "  ")

                val reqFile: RequestBody = RequestBody.create(MediaType.parse("*/*"), f)
                attachmentArrayList.add(
                    MultipartBody.Part.createFormData(
                        //"attachments[" + d + "]",
                        "Document",
                        f!!.name,
                        reqFile
                    )
                )

            }

            val monthsBody: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    monthsSpinnerSelected.toString()
                )
            val reasonBody: RequestBody =
                RequestBody.create(
                    MediaType.parse("text/plain"),
                    reasonEditText.text.toString()
                )
            val userIdBody: RequestBody =
                    RequestBody.create(
                            MediaType.parse("text/plain"),
                            prefHelper.getUserId().toString()
                    )


            coroutineScope.launch {

                try {

                    var result = apiService.sendHA(
                        reasonBody,
                        monthsBody,
                        userIdBody,
                        attachmentArrayList
                    ).await()

                    Log.e("result", "" + result)


                    if (result.success == "1") {

                        //navigateback
                        onBackPressed()
                        Toast.makeText(
                            this@CreateDynamicWFActivity,
                            "Workflow submitted",
                            Toast.LENGTH_SHORT
                        ).show()


                    } else {

                        Toast.makeText(
                            this@CreateDynamicWFActivity,
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


    private fun addToSpinner() {
        //spinner 1  //static for now
        list.add("Select No of months")
        list.add("3")
        list.add("6")
        list.add("12")
//        Log.e("List", list.toString())

        val subscriberAdapter = object : ArrayAdapter<Any>(
            this, R.layout.spinner_item_layout,
            list as List<Any>
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return super.getDropDownView(position, convertView, parent).also { view ->
                    if (position == monthsSpinner.selectedItemPosition) {
                        // view.setBackgroundColor(resources.getColor(R.color.color_light))
//                        view.findViewById<TextView>(android.R.id.text1)
//                            .setTextColor(Color.parseColor(primaryHexColor))
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
                                .setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    0,
                                    0
                                )
                        }
                    } else {

                        view.findViewById<TextView>(android.R.id.text1)
                            .setTextColor(
                                resources.getColor(
                                    android.R.color.black
                                )
                            )
                        view.findViewById<TextView>(android.R.id.text1)
                            .setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                0,
                                0
                            )
                    }
                }
            }
        }

        subscriberAdapter.setDropDownViewResource(R.layout.spinner_sample_one)
        monthsSpinner.adapter = subscriberAdapter

        //spinner 1
        monthsSpinner.setOnTouchListener(View.OnTouchListener { v, event ->
            Utils.hideKeyBoard(monthsSpinner, this)
            false
        })

        // Set an on item selected listener for spinner object
        monthsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                Log.e("Res", parent.getItemAtPosition(position).toString())

                //check list
                if(parent.getItemAtPosition(position).toString() == "Select No of months"){
                    monthsSpinnerSelected = "Select No of months"
                } else {
                    for (item in list){
                        if(parent.getItemAtPosition(position).toString() == item) {
                            Log.e("ResSelected", item)
                            monthsSpinnerSelected = item
                            Log.e("ResSelected", monthsSpinnerSelected.toString())
                        }
                    }
                }

                }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }

        }
    }

    private fun openDocuments(){
        var iCount = 1 - listImages.size
        Log.e("size", "${listImages.size} $iCount")

        if (listImages.size < 1) {
            FilePickerBuilder.instance
                    .setMaxCount(iCount)
                    .setActivityTheme(R.style.LibAppTheme)
                    .pickFile(this, PICKFILE_RESULT_CODE)
        } else {
            Toast.makeText(this, "Maximum files added", Toast.LENGTH_SHORT).show()
        }
    }


    private fun addPhotoVideoFromDevice() {
        var iCount = 1 - listImages.size
        Log.e("size", "${listImages.size} $iCount")

        //maximum pick limit set 1
        if (listImages.size < 1) {
            Utils.hideKeyBoard(rootLayout, this@CreateDynamicWFActivity)

            val options: Options = Options.init()
                    .setRequestCode(REQUEST_CODE_CHOOSE) //Request code for activity results
                    .setCount(iCount) //Number of images to restict selection count
                    .setFrontfacing(false) //Front Facing camera on start
//            .setPreSelectedUrls(returnValue) //Pre selected Image Urls
                    .setExcludeVideos(false) //Option to exclude videos
                    .setVideoDurationLimitinSeconds(30) //Duration for video recording
                    .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientaion
                    .setPath("/pix/images") //Custom Path For media Storage


            Pix.start(this@CreateDynamicWFActivity, options)

        } else {
            Toast.makeText(this, "Maximum files added", Toast.LENGTH_SHORT).show()
        }


    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICKPHOTO_RESULT_CODE -> if (resultCode == Activity.RESULT_OK && data != null) {
                var returnValue: ArrayList<Uri> = data!!.getParcelableArrayListExtra<Uri>(
                    FilePickerConst.KEY_SELECTED_MEDIA
                )!!
                //has to be an ArrayList
                val uris = ArrayList<Uri>()
                //convert from paths to Android friendly Parcelable Uri's
                //val returnValue: ArrayList<Uri> = data!!.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)
                Log.e("chooser", "${returnValue.toString()}")


            }


            PICKFILE_RESULT_CODE -> if (resultCode == Activity.RESULT_OK && data != null) {
                val returnValue: ArrayList<Uri> =
                    data?.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS)!!
                Log.e("chooser", "${returnValue.toString()}")
                docPaths.addAll(data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_DOCS)!!)


                for (uri in returnValue) {
                    //Log.e("chooser", "${item.toString()}")
                    val uriPathHelper = URIPathHelper()
                    val filePath = uriPathHelper.getPath(this@CreateDynamicWFActivity, uri)

                    //get emulated file path
                    Log.e("file path", filePath.toString())
                    var docFile: File = File(filePath)
                    Log.e("file path", docFile.absolutePath)

                    //Log.e("Path", uri.toString())
                    Log.e("Path2", uri!!.getPath().toString())

                    val absolutePath: String = File(uri.path).absolutePath
                    Log.e("absolutePath", absolutePath)

                    //resultIntent.setDataAndType(fileUri, contentResolver.getType(fileUri))


                    val layoutInflater =
                        baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

                    val layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )


                    layoutParams.setMargins(8, 8, 8, 8)

                    val uploadedImageViewItem = layoutInflater.inflate(
                        R.layout.action_custom_image_item,
                        null
                    )

                    if (docFile.name.toLowerCase().endsWith("doc") || docFile.name.toLowerCase()
                            .endsWith(
                                "docx"
                            ) || docFile.name.toLowerCase()
                            .endsWith("pdf") || docFile.name.toLowerCase().endsWith(
                            "odt"
                        ) ||
                        docFile.name.toLowerCase().endsWith("rtf") || docFile.name.toLowerCase()
                            .endsWith(
                                "text"
                            ) || docFile.name.toLowerCase()
                            .endsWith("text") || docFile.name.toLowerCase().endsWith(
                            "tex"
                        ) ||
                        docFile.name.toLowerCase().endsWith("txt") || docFile.name.toLowerCase()
                            .endsWith(
                                "wpd"
                            )
                    ) {
                        gotAttachmentImages(docFile, uploadedImageViewItem, layoutParams)
                    } else {
                        Toast.makeText(this, "Unsupported File Format", Toast.LENGTH_SHORT).show()
                    }

                }
            }


            REQUEST_CODE_CHOOSE -> if (resultCode == Activity.RESULT_OK && data != null) {
                var returnValue = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)

                Log.e("chooser", "${returnValue.toString()}")

                if (returnValue != null) {
                    for (item in returnValue) {

                        val layoutInflater =
                            baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


                        val layoutParams = RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                        )


                        layoutParams.setMargins(8, 8, 8, 8)

                        val uploadedImageViewItem = layoutInflater.inflate(
                            R.layout.action_custom_image_item, null
                        )


                        var imageFile: File = File(item)

                        if (imageFile.name.endsWith("jpeg") || imageFile.name.endsWith("jpg") || imageFile.name.endsWith(
                                "png"
                            ) || imageFile.name.endsWith("gif") ||
                            imageFile.name.endsWith("tiff") || imageFile.name.endsWith("raw") || imageFile.name.endsWith(
                                "mp4"
                            ) || imageFile.name.endsWith("mpeg-4") ||
                            imageFile.name.endsWith("mov") || imageFile.name.endsWith("wmv") || imageFile.name.endsWith(
                                "flv"
                            ) || imageFile.name.endsWith("avi") || imageFile.name.endsWith("avchd") ||
                            imageFile.name.endsWith("webm") || imageFile.name.endsWith("mkv")
                        ) {

                            //tiff,raw,mp4,mpeg-4,mov,wmv,flv,avi,avchd,webm,mkv
                            gotAttachmentImages(
                                imageFile,
                                uploadedImageViewItem,
                                layoutParams
                            )

                        } else {
                            Toast.makeText(this, "Unsupported File Format", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

            } else {

                //none
            }

        }
    }

    private fun gotAttachmentImages(
        file: File,
        uploadedImageViewItem: View,
        layoutParams: RelativeLayout.LayoutParams
    ) {

        //image or video choose before compression
        var isFile = false
        var newFile: File
        //check type
        if (file.name.endsWith("png") || file.name.endsWith("jpg") || file.name.endsWith("jpeg") || file.name.endsWith(
                "jfif"
            )
        ) {
            newFile = Utils.convertCustomFileSize(file, 1024, file.name)
            isFile = false
        } else {
            newFile = file
            isFile = true
        }

        val selectedImage = BitmapFactory.decodeFile(newFile!!.absolutePath)


        listImages.add("" + newFile!!.absolutePath)

        imageLinearLayout!!.addView(uploadedImageViewItem, layoutParams)

        val uploadedImage = uploadedImageViewItem.findViewById(R.id.img_lab_img) as ImageView
//        uploadedImage.setImageBitmap(selectedImage)

        if (isFile) {
            Glide.with(this)
//               .asBitmap()
                    .load(R.drawable.ic_file_alt)
                    .placeholder(R.drawable.placeholder_bg)
                    .error(R.drawable.placeholder_bg)
                    .into(uploadedImage)
        } else {
            Glide.with(this)
                    .load(selectedImage)
                    .placeholder(R.drawable.placeholder_bg)
                    .error(R.drawable.placeholder_bg)
                    .into(uploadedImage)
        }


        val removeUploadedImage = uploadedImageViewItem.findViewById(R.id.remove_image) as ImageView
        val uploadedImageNameTextView = uploadedImageViewItem.findViewById(R.id.image_name) as TextView

        uploadedImageNameTextView.setText("" + newFile.absolutePath)

        removeUploadedImage.setOnClickListener {

            Utils.hideKeyBoard(it, this@CreateDynamicWFActivity)
            Log.e("Clicked", "Remove")
            (uploadedImageViewItem.parent as LinearLayout).removeView(uploadedImageViewItem)
            val uploadedImageNameTextView = uploadedImageViewItem.findViewById(R.id.image_name) as TextView
            listImages.remove("" + uploadedImageNameTextView.text.toString())
            //           Log.e("size remove", "${listImages.size} ${uploadedImageNameTextView.text.toString()}")
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


//fun onPickDoc() {
//    val zips = arrayOf("zip", "rar")
//    val pdfs = arrayOf("pdf")
//    val maxCount: Int = MAX_ATTACHMENT_COUNT - photoPaths.size()
//    if (docPaths.size() + photoPaths.size() === MAX_ATTACHMENT_COUNT) {
//        Toast.makeText(
//            this, "Cannot select more than " + MAX_ATTACHMENT_COUNT.toString() + " items",
//            Toast.LENGTH_SHORT
//        ).show()
//    } else {
//        FilePickerBuilder.getInstance()
//            .setMaxCount(maxCount)
//            .setSelectedFiles(docPaths)
//            .setActivityTheme(R.style.FilePickerTheme)
//            .setActivityTitle("Please select doc")
//            .addFileSupport("ZIP", zips)
//            .addFileSupport("PDF", pdfs, R.drawable.pdf_blue)
//            .enableDocSupport(true)
//            .enableSelectAll(true)
//            .sortDocumentsBy(SortingTypes.name)
//            .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//            .pickFile(this)
//    }
//}