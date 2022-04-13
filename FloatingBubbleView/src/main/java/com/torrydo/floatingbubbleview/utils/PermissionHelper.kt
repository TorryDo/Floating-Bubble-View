package com.torrydo.floatingbubbleview.utils

import android.content.Context
import android.os.Build
import android.provider.Settings

/**
 * by default, display over other app permission will be granted automatically if minor than android M
 *
 * - some MIUI devices may not work properly
 *
 * */
fun Context.isDrawOverlaysPermissionGranted(): Boolean {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        return true
    }

    return Settings.canDrawOverlays(this)
}