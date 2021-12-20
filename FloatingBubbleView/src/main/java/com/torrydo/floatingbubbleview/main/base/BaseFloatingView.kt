package com.torrydo.floatingbubbleview.main.base

import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager

/**
 * have to do:
 * - logger class
 *
 *
 * */

open class BaseFloatingView(
    context: Context
) {

    var windowManager: WindowManager? = null
    var windowParams: WindowManager.LayoutParams? = null

    init {
        windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        windowParams = WindowManager.LayoutParams()
    }

    protected fun show(view: View){
        windowManager?.addView(view, windowParams)
    }
    protected fun remove(view: View){
        windowManager?.removeView(view)
    }
    protected fun update(view: View){
        windowManager?.updateViewLayout(view, windowParams)
    }



    // private -------------------------------------------------------------------------------------

    open fun setupDefaultLayoutParams() {
        windowParams?.apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT

            gravity = Gravity.CENTER
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH

            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                // use for android version lower than 8
                WindowManager.LayoutParams.TYPE_PHONE
            }
        }
    }

}