package com.torrydo.floatingbubble.utils

import android.content.Context

class TestScreenDetails {

    fun getStr(context: Context): String{
        val size = ScreenInfo.getScreenSize(context)
        return "width = ${size.width} -- height = ${size.height}"
    }
}