package com.torrydo.floatingbubbleview

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.StyleRes

class ExpandableView(
    private val builder: Builder,
) : BaseFloatingView(builder.context) {

    init {
        setupLayoutParams()
    }

    // interface -----------------------------------------------------------------------------------

    interface Action {
        fun popToBubble() {}

    }

    interface Listener{
        fun onOpenExpandableView() {}
        fun onCloseExpandableView() {}
    }

    // public --------------------------------------------------------------------------------------

    fun show() = logIfError {
        super.show(builder.view!!)
    }.onComplete {
        builder.listener.onOpenExpandableView()
    }


    fun remove() = logIfError {
        super.remove(builder.view!!)
    }.onComplete {
        builder.listener.onCloseExpandableView()
    }


    // private -------------------------------------------------------------------------------------

    override fun setupLayoutParams() {
        super.setupLayoutParams()

        logIfError {

            windowParams.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                gravity = Gravity.TOP
                flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
                dimAmount = builder.dim         // default = 0.5f
                softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE

                builder.viewStyle?.let {
                    windowAnimations = it
                }

            }

        }

    }

    // builder class -------------------------------------------------------------------------------

    class Builder(internal val context: Context) {

        internal var view: View? = null
        internal var viewStyle: Int? = R.style.default_bubble_style

        internal var listener = object : Listener {}

        internal var dim = 0.5f

        fun expandableView(view: View): Builder {
            this.view = view
            return this
        }

        fun addExpandableViewListener(listener: Listener): Builder {
            this.listener = listener
            return this
        }

        fun expandableViewStyle(@StyleRes style: Int?): Builder{
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