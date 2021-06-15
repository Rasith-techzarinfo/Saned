package com.saned.view.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import com.saned.R

class ProfileView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)
        val prof=findViewById(R.id.edit) as ImageView
        prof.setOnClickListener {
            val intent=Intent(applicationContext, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}