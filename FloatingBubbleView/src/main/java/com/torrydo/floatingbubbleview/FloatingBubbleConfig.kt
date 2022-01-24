package com.torrydo.floatingbubbleview

import android.app.Service

abstract class FloatingBubbleConfig : Service() {

    private val logger = Logger()
        .setTag(javaClass.simpleName.toTag())
        .setDebugEnabled(Constants.IS_DEBUG_ENABLED)

    private var floatingBubble: FloatingBubble? = null
    private var expandableView: ExpandableView? = null

    // abstract func -------------------------------------------------------------------------------

    abstract fun setupBubble(): FloatingBubble.Builder

    abstract fun setupExpandableView(action: ExpandableView.Action): ExpandableView.Builder

    // override func
    fun setup() {

        floatingBubble = setupBubble()
            .addFloatingBubbleTouchListener(customFloatingBubbleTouchEvent)
            .build()

        expandableView = setupExpandableView(customExpandableViewListener)
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

    private val customExpandableViewListener = object : ExpandableView.Action {
        override fun popToBubble() {

            removeExpandableViewAndShowBubble()

            logger.log("pop to bubble")
        }
    }

    private val customFloatingBubbleTouchEvent = object: FloatingBubble.TouchEvent {
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