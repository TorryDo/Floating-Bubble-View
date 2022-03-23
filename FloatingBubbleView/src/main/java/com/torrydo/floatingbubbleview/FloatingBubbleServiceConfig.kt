package com.torrydo.floatingbubbleview

import android.app.Service

abstract class FloatingBubbleServiceConfig : Service(), Logger by LoggerImpl() {


    private var floatingBubble: FloatingBubble? = null
    private var expandableView: ExpandableView? = null

    // lifecycle -----------------------------------------------------------------------------------

    override fun onDestroy() {
        tryRemoveAllView()
        super.onDestroy()
    }

    // overridable ---------------------------------------------------------------------------------

    /**
     * bubble is required
     * */
    abstract fun setupBubble(): FloatingBubble.Builder

    open fun setupExpandableView(action: ExpandableView.Action): ExpandableView.Builder? = null

    // public func ---------------------------------------------------------------------------------
    fun setupViewAppearance() {

        floatingBubble = setupBubble()
            .addFloatingBubbleTouchListener(customFloatingBubbleTouchEvent)
            .build()

        expandableView = setupExpandableView(customExpandableViewListener)
            ?.build()


        onMainThread {
            tryShowBubbles()
        }
    }

    // private func --------------------------------------------------------------------------------

    private val customExpandableViewListener = object : ExpandableView.Action {
        override fun popToBubble() {
            tryRemoveExpandableView()
            tryShowBubbles()
        }
    }

    private val customFloatingBubbleTouchEvent = object : FloatingBubble.TouchEvent {
        override fun onClick() {
            tryRemoveBubbles()
            tryShowExpandableView()
        }

        override fun onDestroy() {
            tryStopService()
        }

    }

    private fun tryStopService() {
        try {
            tryRemoveAllView()
        } catch (e: Exception) {
            e(e.message.toString())
        }
        stopSelf()
    }

    private fun tryRemoveAllView() {
        tryRemoveExpandableView()

        tryRemoveBubbles()
    }

    // shorten  ------------------------------------------------------------------------------------

    private fun tryRemoveExpandableView() {
        try {
            expandableView!!.remove();
        } catch (e: Exception) {
            e(e.message.toString())
        }
    }

    private fun tryShowExpandableView() {
        try {
            expandableView!!.show();
        } catch (e: Exception) {
            e(e.message.toString())
        }
    }

    private fun tryShowBubbles() {
        try {
            floatingBubble!!.showIcon()
        } catch (e: Exception) {
            e(e.message.toString())
        }
    }

    private fun tryRemoveBubbles() {
        try {
            floatingBubble!!.removeIcon()
            floatingBubble!!.removeRemoveIcon()
        } catch (e: Exception) {
            e(e.message.toString())
        }
    }


}

