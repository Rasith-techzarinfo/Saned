package com.saned.view.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import com.saned.R

class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val webView=findViewById<WebView>(R.id.wv)
        webView.loadUrl("https://dev.osaned.com/")
        val back=findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            val intent=Intent(applicationContext, DashboardActivity :: class.java)
            startActivity(intent)
        }
    }
}