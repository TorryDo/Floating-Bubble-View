package com.torrydo.floatingbubbleview

import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.torrydo.floatingbubbleview.utils.logIfError


open class BaseFloatingView(
    context: Context
) : Logger by LoggerImpl() {


    var windowManager: WindowManager? = null
    var windowParams: WindowManager.LayoutParams? = null

    init {
        windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        windowParams = WindowManager.LayoutParams()
    }

    // public --------------------------------------------------------------------------------------

    protected fun show(view: View) {
        logIfError {
            windowManager!!.addView(view, windowParams)
        }
    }

    protected fun remove(view: View) {
        logIfError {
            windowManager!!.removeView(view)
        }
    }

    protected fun update(view: View) {
        logIfError {
            windowManager!!.updateViewLayout(view, windowParams)
        }
    }


    // override ------------------------------------------------------------------------------------

    open fun setupLayoutParams() {

        logIfError {

            windowParams!!.apply {
                width = WindowManager.LayoutParams.WRAP_CONTENT
                height = WindowManager.LayoutParams.WRAP_CONTENT

                gravity = Gravity.CENTER
                format = PixelFormat.TRANSLUCENT
                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH

                type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    // for android version lower than 8
                    WindowManager.LayoutParams.TYPE_PHONE
                }
            }

        }
    }

}