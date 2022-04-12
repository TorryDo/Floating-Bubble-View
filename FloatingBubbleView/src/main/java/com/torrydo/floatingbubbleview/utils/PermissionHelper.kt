package com.torrydo.floatingbubbleview.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat


//object PermissionHelper {
//
//    @Deprecated("not yet implemented")
//    fun navigateToPermission(context: Context) {
//        val intent = Intent(
//            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//            Uri.parse("package:" + context.packageName)
//        )
////        startActivityForResult(intent, 0)
//    }
//
//}

fun Context.isPermissionGranted(): Boolean {

    val displayOverOtherAppPermission = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.SYSTEM_ALERT_WINDOW
    )

    if (displayOverOtherAppPermission == PackageManager.PERMISSION_GRANTED) {
        return true
    }

    return false
}