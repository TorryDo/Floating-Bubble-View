package com.torrydo.floatingbubbleview.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings


object Permissions {

    const val REQUEST_CODE_ASK_PERMISSIONS = 696

    /**
     * Checks if the permissions is required
     *
     * @param context the application context
     * @return is the permission request needed
     */
    private fun requiresPermission(context: Context?): Boolean {
        return Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(context)
    }

    fun requestOverlay(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23 && requiresPermission(activity)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + activity.packageName)
            )
            activity.startActivityForResult(
                intent,
                REQUEST_CODE_ASK_PERMISSIONS
            )
        }
    }

}