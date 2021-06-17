package com.saned.view.ui.activities

import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.*
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Button
import com.saned.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val back=findViewById<Button>(R.id.back)
        back.setOnClickListener {
            finishAfterTransition()
        }

    }

}