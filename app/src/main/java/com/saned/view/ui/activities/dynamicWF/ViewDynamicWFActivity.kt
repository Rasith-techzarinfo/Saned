package com.saned.view.ui.activities.dynamicWF

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.saned.R

class ViewDynamicWFActivity : AppCompatActivity() {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.toolbar_title)
    lateinit var toolbarTitle: TextView

    @BindView(R.id.empty_nodata_view)
    lateinit var emptyView: LinearLayout

    @BindView(R.id.root_layout)
    lateinit var rootLayout: LinearLayout

    var formID: String = ""
    var formName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_dynamic_w_f)
        ButterKnife.bind(this)
        setToolBar()
        init()
    }









    private fun init() {
        //get intent data
        formID = "" + intent.getStringExtra("formID")
        formName = "" + intent.getStringExtra("formName")
        Log.e("itt", "" + formID)
        toolbarTitle.text = formName + " Detail"


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
