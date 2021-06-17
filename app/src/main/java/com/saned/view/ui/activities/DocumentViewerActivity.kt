package com.saned.view.ui.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.saned.R
import com.saned.view.utils.Utils
import kotlinx.android.synthetic.main.activity_document_viewer.*


class DocumentViewerActivity : AppCompatActivity() {

    var documentURL=""
    lateinit var progressDialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_viewer)
        setToolBar()
        setupListener()
    }



    private fun setupListener() {
        //get intent
        documentURL=""+ intent!!.getStringExtra("url")

        //refresh btn
        overflowImageView.setOnClickListener {
            val popup = PopupMenu(this, toolbarIconLayout)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.overflow_document_menu, popup.menu)

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {

                    R.id.action_refersh -> {
                        //reload
                        loadWeb()

                        return@OnMenuItemClickListener true
                    }
                    R.id.action_open_with_chrome -> {
                        //open with external app
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("" + documentURL))
                        startActivity(intent)
                        overridePendingTransition(
                            R.anim.enter_right_to_left,
                            R.anim.exit_left_to_right
                        )

                        return@OnMenuItemClickListener true
                    }
                    else -> {
                    }
                }
                false
            })
            popup.show()
    }


        // Custom Progress dialog
        progressDialog = Dialog(this)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setContentView(R.layout.custom_progress_dialog_layout)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()



        //webview
        webView.settings.javaScriptEnabled=true
        webView.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView, request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                // Do something
                Log.e("err", "" + error.description.toString())
                Toast.makeText(
                    this@DocumentViewerActivity,
                    "Your Internet Connection May not be active Or " + error.description.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
//                view.loadUrl(url)
//                return true
                return false
            }

            override fun onLoadResource(view: WebView?, url: String?) {
//               do none
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                try {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                } catch (exception: java.lang.Exception) {
                    exception.printStackTrace()
                }
            }

        }


        //load url
        if(Utils.isInternetAvailable(this)) {
            loadWeb()
        } else{
            Utils.checkNetworkDialog(this, this) { loadWeb() }
        }
    }

    fun loadWeb() {
        var documentURLdrive = "http://drive.google.com/viewerng/viewer?embedded=true&url=$documentURL"
        val gdocUrl1: String = "http://docs.google.com/gview?embedded=true&url=$documentURL"
        webView.loadUrl(documentURLdrive)
    }



    class CallbackCustom : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView, url: String
        ): Boolean {
            return false
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
