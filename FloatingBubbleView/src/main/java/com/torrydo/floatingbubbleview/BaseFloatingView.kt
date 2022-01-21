package com.torrydo.floatingbubbleview

import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager


open class BaseFloatingView(
    context: Context
) {

    private val logger = Logger()
        .setTag(javaClass.simpleName.toTag())
        .setDebugEnabled(Constants.IS_DEBUG_ENABLED)

    var windowManager: WindowManager? = null
    var windowParams: WindowManager.LayoutParams? = null

    init {
        windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        windowParams = WindowManager.LayoutParams()
    }

    protected fun show(view: View) {
        try {
            windowManager?.addView(view, windowParams)
        } catch (e: Exception) {
            logger.error(e.message.toString())
        }
    }

    protected fun remove(view: View) {
        try {
            windowManager?.removeView(view)
        } catch (e: Exception) {
            logger.error(e.message.toString())
        }
    }

    protected fun update(view: View) {
        try {
            windowManager?.updateViewLayout(view, windowParams)
        } catch (e: Exception) {
            logger.error(e.message.toString())
        }
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