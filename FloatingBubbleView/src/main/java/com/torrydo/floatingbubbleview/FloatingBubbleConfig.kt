package com.torrydo.floatingbubbleview

import android.app.Service
import com.torrydo.floatingbubbleview.main.FloatingBubble
import com.torrydo.floatingbubbleview.main.FloatingBubbleBuilder
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableView
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableViewBuilder
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableViewListener
import com.torrydo.floatingbubbleview.physics.FloatingBubbleTouchListener
import com.torrydo.floatingbubbleview.utils.Constant
import com.torrydo.floatingbubbleview.utils.ThreadHelper
import com.torrydo.floatingbubbleview.utils.logger.Logger
import com.torrydo.floatingbubbleview.utils.toTag

abstract class FloatingBubbleConfig : Service() {

    private val logger = Logger()
        .setTag(javaClass.simpleName.toTag())
        .setDebugEnabled(Constant.IS_DEBUG_ENABLED)

    private var floatingBubble: FloatingBubble? = null

    private var expandableView: ExpandableView? = null

    // abstract func -------------------------------------------------------------------------------

    abstract fun setupBubble(): FloatingBubbleBuilder

    abstract fun setupExpandableView(listener: ExpandableViewListener): ExpandableViewBuilder

    // override func
    fun setup() {

        floatingBubble = setupBubble()
            .setExpandableViewBuilder(setupExpandableView(CustomExpandableViewListener()))
            .addFloatingBubbleTouchListener(CustomFloatingBubbleTouchListener())
            .build()

        expandableView = setupExpandableView(CustomExpandableViewListener())
            .build()

        ThreadHelper().runOnMainThread {
            try {
                floatingBubble!!.show()
                logger.log("bubble show")
            } catch (e: Exception) {
                logger.error(e.message.toString())
            }
        }
    }

    // private func --------------------------------------------------------------------------------

    inner class CustomExpandableViewListener : ExpandableViewListener {
        override fun popToBubble() {

            removeExpandableView()

            logger.log("heelo from floating bubble service")
        }
    }

    private var IS_BUBBLE_CLICKABLE = true

    inner class CustomFloatingBubbleTouchListener : FloatingBubbleTouchListener {
        override fun onDown(x: Int, y: Int) {

            IS_BUBBLE_CLICKABLE = true
        }

        override fun onMove(x: Int, y: Int) {

            if (IS_BUBBLE_CLICKABLE) IS_BUBBLE_CLICKABLE = false

        }

        override fun onUp(x: Int, y: Int) {
            if (IS_BUBBLE_CLICKABLE) {
                onClick()
            }
        }

        override fun onClick() {
            showExpandableView()
        }


    }

    private fun removeExpandableView() {

        expandableView?.let { nonNullExpandableView ->
            nonNullExpandableView.remove()
            floatingBubble?.show()
        }
    }

    private fun showExpandableView() {

        floatingBubble?.remove()

        try {
            expandableView!!.show()
        } catch (e: Exception) {
            logger.error(e.message.toString())
        }

    }

}