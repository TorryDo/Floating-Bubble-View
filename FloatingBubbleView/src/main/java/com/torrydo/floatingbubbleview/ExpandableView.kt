package com.torrydo.floatingbubbleview

import android.content.Context
import android.view.View
import android.view.WindowManager
import androidx.annotation.StyleRes
import androidx.compose.runtime.Composable
import com.torrydo.screenez.ScreenRotation

class ExpandableView(
    private val builder: Builder,
) : BaseFloatingView(builder.context) {

    private var isComposeInitialized = false

    init {
        setupLayoutParams()
    }

    // interface -----------------------------------------------------------------------------------

    interface Action {
        fun popToBubble() {}
    }

    interface Listener {
        fun onOpenExpandableView() {}
        fun onCloseExpandableView() {}
    }

    // public --------------------------------------------------------------------------------------

    fun show() {

        if (builder.view != null) {
            super.show(builder.view!!)
            builder.listener.onOpenExpandableView()
            return
        }

        if (builder.composeView == null) {
            throw Exception("You doesn't specify compose or view, please review your code!")
        }

        if (isComposeInitialized.not()) {
            builder.composeLifecycleOwner?.onCreate()
            isComposeInitialized = true
        }

        builder.composeLifecycleOwner?.onStart()
        builder.composeLifecycleOwner?.onResume()


        super.show(builder.composeView!!)

        builder.listener.onOpenExpandableView()
    }


    fun remove() {
        if (builder.view != null) {
            super.remove(builder.view!!)
            builder.listener.onCloseExpandableView()
            return
        }

        // it's important to check if the expandable-view is initialized or not,
        // if you blindly end the compose's lifecycle, error appear
        if (isComposeInitialized.not()) {
            return
        }

        builder.composeView?.also {
            builder.composeLifecycleOwner?.onPause()
            builder.composeLifecycleOwner?.onStop()
            builder.composeLifecycleOwner?.onDestroy()
            super.remove(it)
        }
        builder.listener.onCloseExpandableView()
    }

    override fun destroy() {
        builder.composeLifecycleOwner?.onStop()
        builder.composeLifecycleOwner?.onDestroy()
        super.destroy()
    }


    // private -------------------------------------------------------------------------------------

    override fun setupLayoutParams() {
        super.setupLayoutParams()

        var mFlag = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        var mHeight = WindowManager.LayoutParams.WRAP_CONTENT
        var mWidth = WindowManager.LayoutParams.MATCH_PARENT
        var mX: Int? = null

        if (builder.isDrawUnderSystemUI) {
            mFlag = WindowManager.LayoutParams.FLAG_DIM_BEHIND or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

            mWidth = sez.fullWidth
            mHeight = sez.fullHeight

            if (sez.screenRotation == ScreenRotation.LANDSCAPE) {
                mX = -sez.navBarHeight
            }

        }

        windowParams.apply {
            width = mWidth
            height = mHeight
            flags = mFlag
            dimAmount = builder.dim         // default = 0.5f

//            this.verticalMargin = 0.07f // so fun hihi
//            y = 100 // so fun hihihi
            if (mX != null) {
                x = mX
            }

            builder.viewStyle?.let {
                windowAnimations = it
            }

        }

    }

    // builder class -------------------------------------------------------------------------------

    class Builder(internal val context: Context) {

        internal var view: View? = null
        internal var viewStyle: Int? = R.style.default_bubble_style

        internal var composeLifecycleOwner: ComposeLifecycleOwner? = null
        internal var composeView: FloatingComposeView? = null

        internal var listener = object : Listener {}

        internal var isDrawUnderSystemUI: Boolean = false

        internal var dim = 0.5f

        /**
         * Allow the expandable-view to display under the system UI elements
         * */
        internal fun drawUnderSystemUI(included: Boolean): Builder {
            isDrawUnderSystemUI = included
            return this
        }

        fun view(view: View): Builder {
            if (composeView != null) {
                throw IllegalStateException("Cannot pass view after setting composable")
            }
            this.view = view
            return this
        }

        fun compose(content: @Composable () -> Unit): Builder {
            if (view != null) {
                throw IllegalStateException("Cannot pass composable after setting view")
            }
            composeView = FloatingComposeView(context).apply {
                setContent { content() }
            }
            composeLifecycleOwner = ComposeLifecycleOwner()
            composeLifecycleOwner?.attachToDecorView(composeView!!)

            return this
        }

        fun addExpandableViewListener(listener: Listener): Builder {
            this.listener = listener
            return this
        }

        fun expandableViewStyle(@StyleRes style: Int?): Builder {
            viewStyle = style
            return this
        }

        /**
         * @param dimAmount background opacity below the expandable-view
         * */
        fun dimAmount(dimAmount: Float): Builder {
            this.dim = dimAmount
            return this
        }


        fun build(): ExpandableView {
            return ExpandableView(this)
        }

    }
}