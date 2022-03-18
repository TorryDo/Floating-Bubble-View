package com.torrydo.floatingbubbleview

import android.app.Service

abstract class FloatingBubbleServiceConfig : Service(), Logger by LoggerImpl() {


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

        onMainThread {
            try {
                floatingBubble!!.showIcon()
            } catch (e: Exception) {
                e(e.message.toString())
            }

        }
    }

    // private func --------------------------------------------------------------------------------

    private val customExpandableViewListener = object : ExpandableView.Action {
        override fun popToBubble() {

            removeExpandableViewAndShowBubble()

            d("pop to bubble")
        }
    }

    private val customFloatingBubbleTouchEvent = object : FloatingBubble.TouchEvent {
        override fun onClick() {
            removeBubbleAndShowExpandableView()
        }

        override fun onDestroy() {
            tryStopService()
        }

    }

    private fun removeExpandableViewAndShowBubble() {

        expandableView?.let { nonNullExpandableView ->
            nonNullExpandableView.remove()
            floatingBubble?.showIcon()
        }
    }

    private fun removeBubbleAndShowExpandableView() {

        floatingBubble?.removeIcon()

        try {
            expandableView!!.show()
        } catch (e: Exception) {
            e(e.message.toString())
        }

    }

    private fun tryStopService() {
        try {
            removeAllView()
        } catch (e: Exception) {
            e(e.message.toString())
        }
        stopSelf()
    }

    // splitter ------------------------------------------------------------------------------------

    private fun removeAllView(){
        removeExpandableViewAndShowBubble()
        floatingBubble?.removeIcon()
        floatingBubble?.removeRemoveIcon()
    }



}

