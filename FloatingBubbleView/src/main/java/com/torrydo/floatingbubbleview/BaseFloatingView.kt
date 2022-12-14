package com.torrydo.floatingbubbleview

import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager


open class BaseFloatingView(
    context: Context,
) {

    protected var windowManager: WindowManager? = null
    protected var windowParams: WindowManager.LayoutParams? = null

    init {
        windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        windowParams = WindowManager.LayoutParams()

    }

    // public --------------------------------------------------------------------------------------

    protected fun show(view: View) {
        windowManager!!.addView(view, windowParams)
    }

    protected fun remove(view: View) {
        if(view.windowToken == null) return
        windowManager!!.removeView(view)
    }

    protected fun update(view: View) {
        windowManager!!.updateViewLayout(view, windowParams)
    }

    protected open fun destroy() {
        windowManager = null
        windowParams = null
    }


    // override ------------------------------------------------------------------------------------
    /**
     * it's required to call `super.setupLayoutParams()` before override windowParams properties
     * */
    protected open fun setupLayoutParams() {

        windowParams!!.apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT

            gravity = Gravity.CENTER
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH

            type = if (Build.VERSION.SDK_INT >= AndroidVersions.`8`) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }

        }

    }

}