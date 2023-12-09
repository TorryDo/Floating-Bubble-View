package com.torrydo.floatingbubbleview.bubble

import android.app.Service
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.torrydo.floatingbubbleview.ComposeLifecycleOwner


open class Bubble(
    context: Context,
    root: View?,
    private val containCompose: Boolean = false
) {

    private var _windowManager: WindowManager? = null
    private var _rootParams: WindowManager.LayoutParams? = null

    val windowManager get() = _windowManager!!

    private var composeOwner: ComposeLifecycleOwner? = null
    private var isComposeOwnerInitialized: Boolean = false

    private var _root: View? = null
    var root
        get() = _root!!
        set(value) {
            _root = value
        }

    var layoutParams
        get() = _rootParams!!
        set(value) {
            _rootParams = value
        }

    val rootGroup get() = root as ViewGroup

    init {
        _windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        _rootParams = WindowManager.LayoutParams()
        _root = root

        if (containCompose) {
            composeOwner = ComposeLifecycleOwner()
            composeOwner!!.attachToDecorView(root)
        }
    }

    // public --------------------------------------------------------------------------------------

    open fun show() {
        try {
            if (containCompose) {
                if (isComposeOwnerInitialized.not()) {
                    composeOwner!!.onCreate() // only call this once
                    isComposeOwnerInitialized = true

                }
                composeOwner!!.onStart()
                composeOwner!!.onResume()
            }

            windowManager.addView(root, _rootParams)
        } catch (e: Exception) {
//            e.printStackTrace() // xxx has already added to WindowManager
        }
    }

    /**
     * - don't call remove if the view did not call show() previously
     * - call windowManager.removeViewImmediate() will make the view can't change when added again
     * - add this line 'if (root.windowToken == null) return' will prevent the view from being removed in some cases
     * */
    open fun remove() {
//        if (root.windowToken == null) return
        try {
            windowManager.removeView(root)

            if (containCompose) {
                composeOwner!!.onPause()
                composeOwner!!.onStop()
                composeOwner!!.onDestroy()
            }
        }catch (_: Exception){}
    }

    fun update() {
//        if (root.windowToken == null) return
        try {
            windowManager.updateViewLayout(root, _rootParams)
        }catch (e: Exception){}
    }

}