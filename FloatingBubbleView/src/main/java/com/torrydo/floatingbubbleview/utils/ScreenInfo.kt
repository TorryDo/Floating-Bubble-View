package com.torrydo.floatingbubbleview.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.Display
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.annotation.RequiresApi


internal object ScreenInfo {

    private val api: Api =
        when {
            // android 4 to 5
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.R -> ApiLevel23()
            // android 11 and above
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> ApiLevel30()
            // android 5 to 11
            else -> Api()
        }

    /**
     * Returns screen size in pixels.
     */
    fun getScreenSize(context: Context): Size = api.getScreenSize(context)

    private open class Api {

        // api level 21 to 23
        open fun getScreenSize(context: Context): Size {

            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display: Display = wm.defaultDisplay
            return Size(display.width, display.height)

        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private class ApiLevel30 : Api() {
        override fun getScreenSize(context: Context): Size {
            val metrics: WindowMetrics =
                context.getSystemService(WindowManager::class.java).currentWindowMetrics
            return Size(metrics.bounds.width(), metrics.bounds.height())
        }
    }

    // api level 23 to 30
    private class ApiLevel23 : Api() {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun getScreenSize(context: Context): Size {
            val display = context.getSystemService(WindowManager::class.java).defaultDisplay
            val metrics = if (display != null) {
                DisplayMetrics().also { display.getRealMetrics(it) }
            } else {
                Resources.getSystem().displayMetrics
            }
            return Size(metrics.widthPixels, metrics.heightPixels)
        }
    }


}