package com.saned.view.ui.activities

import android.os.Bundle
import android.util.Log
import com.zeuskartik.mediaslider.MediaSliderActivity


class SliderDemo : MediaSliderActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var list: ArrayList<String> = ArrayList<String>()
        list = intent.getSerializableExtra("list") as ArrayList<String>
        var media = "" + intent.getStringExtra("media")
        var title = "" + intent.getStringExtra("title")
        Log.e("slider", "${list.size} $list")        //isnavigationvisible: list.size > 1
        loadMediaSliderView(list, media, title != "null", list.size > 1, false, title, "#000000", "#ffffff", 0)
    }

}