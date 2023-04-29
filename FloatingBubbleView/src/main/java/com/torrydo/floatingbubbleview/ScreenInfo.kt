package com.torrydo.floatingbubbleview

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.Display
import android.view.WindowInsets
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.annotation.RequiresApi
import java.lang.ref.WeakReference

internal object ScreenInfo {

    // static variables ----------------------------------------------------------------------------
    internal var widthPx = 0
    internal var heightPx = 0

    internal var statusBarHeightPx = 0
    internal var softNavBarHeightPx = 0

    var isPortrait = true
    val isLandscape get() = isPortrait.not()


    // methods -------------------------------------------------------------------------------------
    fun onOrientationChanged(context: Context) {

        statusBarHeightPx = getStatusBarHeight(context)
        softNavBarHeightPx = getSoftNavigationBarHeight(context)

        getScreenSize(context).also {
            if(it.height >= it.width){ // portrait
                widthPx = it.width
                heightPx = it.height

                isPortrait = true
            }else{
                widthPx = it.width - statusBarHeightPx - softNavBarHeightPx
                heightPx = it.height

                isPortrait = false
            }
        }

    }

    //region Internal methods ----------------------------------------------------------------------

    /**
     * @return pixel
     * */
    internal fun getStatusBarHeight(context: Context): Int {
        val statusBarHeightId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(statusBarHeightId)
    }

    fun getSoftNavigationBarHeight(context: Context): Int {
        var result = 0
        val resourceId =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * Returns screen size in pixels.
     */
    fun getScreenSize(context: Context): Size {
        return WeakReference(context).get()?.let {

            api.getScreenSize(it)

        } ?: Size(0, 0)
    }

    //endregion


    private val api: Api =
        when {
            // android 4 to 5
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.R -> ApiLevel23()
            // android 11 and above
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> ApiLevel30()
            // android 5 to 11
            else -> Api()
        }

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