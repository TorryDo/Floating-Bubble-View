package com.torrydo.floatingbubbleview

import android.app.Service
import com.torrydo.floatingbubbleview.main.FloatingBubble
import com.torrydo.floatingbubbleview.main.FloatingBubbleBuilder
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableView
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableViewBuilder
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableViewEvent
import com.torrydo.floatingbubbleview.physics.FloatingBubbleTouchListener
import com.torrydo.floatingbubbleview.utils.Constants
import com.torrydo.floatingbubbleview.utils.ThreadHelper
import com.torrydo.floatingbubbleview.utils.logger.Logger
import com.torrydo.floatingbubbleview.utils.toTag

abstract class FloatingBubbleConfig : Service() {

    private val logger = Logger()
        .setTag(javaClass.simpleName.toTag())
        .setDebugEnabled(Constants.IS_DEBUG_ENABLED)

    private var floatingBubble: FloatingBubble? = null
    private var expandableView: ExpandableView? = null

    // abstract func -------------------------------------------------------------------------------

    abstract fun setupBubble(): FloatingBubbleBuilder

    abstract fun setupExpandableView(event: ExpandableViewEvent): ExpandableViewBuilder

    // override func
    fun setup() {

        floatingBubble = setupBubble()
            .addFloatingBubbleTouchListener(CustomFloatingBubbleTouchEvent)
            .build()

        expandableView = setupExpandableView(CustomExpandableViewListener)
            .build()

        ThreadHelper.runOnMainThread {
            try {
                floatingBubble!!.showIcon()
                logger.log("bubble show")
            } catch (e: Exception) {
                logger.error(e.message.toString())
            }
        }
    }

    // private func --------------------------------------------------------------------------------

    private val CustomExpandableViewListener = object : ExpandableViewEvent {
        override fun popToBubble() {

            removeExpandableViewAndShowBubble()

            logger.log("pop to bubble")
        }
    }

    private val CustomFloatingBubbleTouchEvent = object : FloatingBubbleTouchListener {
        override fun onClick() {
            removeBubbleAndshowExpandableView()
        }

        override fun onDestroy() {
            stopThisService()
        }

    }

    private fun removeExpandableViewAndShowBubble() {

        expandableView?.let { nonNullExpandableView ->
            nonNullExpandableView.remove()
            floatingBubble?.showIcon()
        }
    }

    private fun removeBubbleAndshowExpandableView() {

        floatingBubble?.removeIcon()

        try {
            expandableView!!.show()
        } catch (e: Exception) {
            logger.error(e.message.toString())
        }

    }

    private fun stopThisService() {
        try {
            removeExpandableViewAndShowBubble()
            floatingBubble?.removeIcon()
            floatingBubble?.removeRemoveIcon()

        } catch (e: Exception) {
            logger.error(e.message.toString())
        }
        stopSelf()
    }

}