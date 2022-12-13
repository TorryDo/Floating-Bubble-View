package com.torrydo.floatingbubbleview

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager

class ExpandableView(
    private val builder: Builder,
) : BaseFloatingView(builder.context) {

    init {
        setupLayoutParams()
    }

    // interface -----------------------------------------------------------------------------------

    interface Action {
        fun popToBubble() {}
        fun onOpenExpandableView() {}
        fun onCloseExpandableView() {}
    }

    // public --------------------------------------------------------------------------------------

    fun show() = logIfError {
        super.show(builder.rootView!!)
    }.onComplete {
        builder.listener.onOpenExpandableView()
    }


    fun remove() = logIfError {
        super.remove(builder.rootView!!)
    }.onComplete {
        builder.listener.onCloseExpandableView()
    }


    // private -------------------------------------------------------------------------------------

    override fun setupLayoutParams() {
        super.setupLayoutParams()

        logIfError {

            windowParams!!.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                gravity = Gravity.TOP
                flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
                dimAmount = builder.dim         // default = 0.5f
                softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
            }

        }

    }

    // builder class -------------------------------------------------------------------------------

    class Builder(internal val context: Context) {

        internal var rootView: View? = null
        internal var listener = object : Action {}

        var dim = 0.5f

        fun setExpandableView(view: View): Builder {
            this.rootView = view
            return this
        }

        fun addExpandableViewListener(action: Action): Builder {
            this.listener = action
            return this
        }

        fun setDimAmount(dimAmount: Float): Builder {
            this.dim = dimAmount
            return this
        }


        fun build(): ExpandableView {
            return ExpandableView(this)
        }

    }
}