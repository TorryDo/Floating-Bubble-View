package com.torrydo.floatingview.view

import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.core.content.ContextCompat

class FloatingView {

    private var view: View? = null

    fun isOverlayAllowed(context: Context): Boolean{
        val systemAlertWindowPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.SYSTEM_ALERT_WINDOW)
        return systemAlertWindowPermission == PackageManager.PERMISSION_GRANTED
    }

}