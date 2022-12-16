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

    private var _windowManager: WindowManager? = null
    private var _windowParams: WindowManager.LayoutParams? = null

    protected val params get() = _windowParams!!

    init {
        _windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        _windowParams = WindowManager.LayoutParams()
    }

    // public --------------------------------------------------------------------------------------

    protected fun show(view: View) {
        _windowManager!!.addView(view, _windowParams)
    }

    protected fun remove(view: View) {
        if(view.windowToken == null) return
        _windowManager!!.removeView(view)
    }

    protected open fun update(view: View) {
        _windowManager!!.updateViewLayout(view, _windowParams)
    }

    protected open fun destroy() {
        _windowManager = null
        _windowParams = null
    }


    // override ------------------------------------------------------------------------------------
    /**
     * it's required to call `super.setupLayoutParams()` before override windowParams properties
     * */
    protected open fun setupLayoutParams() {

        _windowParams!!.apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT

            gravity = Gravity.TOP or Gravity.LEFT
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