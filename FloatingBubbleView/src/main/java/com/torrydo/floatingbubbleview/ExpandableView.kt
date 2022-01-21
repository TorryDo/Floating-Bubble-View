package com.torrydo.floatingbubbleview

import android.view.Gravity
import android.view.WindowManager

class ExpandableView(
    private val builder: ExpandableViewBuilder
) : BaseFloatingView(builder.context) {

    private val logger = Logger()
        .setTag(javaClass.simpleName.toTag())
        .setDebugEnabled(Constants.IS_DEBUG_ENABLED)


    init {
        setupDefaultLayoutParams()
    }

    // public --------------------------------------------------------------------------------------

    fun show() {
        builder.rootView?.let { nonNullableView ->
            super.show(nonNullableView)
            logger.log("expandable view showing")
            return
        }
        logger.error("expandableView = null")
    }

    fun remove() {
        builder.rootView?.let { nonNullableView ->
            super.remove(nonNullableView)
            logger.log("expandable view removed")
            return
        }
        logger.error("expandableView = null")
    }

    // private -------------------------------------------------------------------------------------

    override fun setupDefaultLayoutParams() {
        super.setupDefaultLayoutParams()

        windowParams?.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            gravity = Gravity.TOP
            flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dimAmount =
                builder.dim                                                           // default = 0.5f
            softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
//            windowAnimations = R.style.TransViewStyle
        }

    }
}