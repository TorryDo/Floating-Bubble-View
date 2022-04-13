package com.torrydo.floatingbubbleview

import android.app.Service
import com.torrydo.floatingbubbleview.utils.logIfError

abstract class FloatingBubbleServiceConfig : Service() {

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

        tryRemoveAllView()
        stopSelf()
    }

    private fun tryRemoveAllView() {

        tryRemoveExpandableView()
        tryRemoveBubbles()
    }

    // shorten  ------------------------------------------------------------------------------------

    private fun tryRemoveExpandableView() = logIfError {
        expandableView!!.remove()
    }


    private fun tryShowExpandableView() = logIfError {
        expandableView!!.show()
    }


    private fun tryShowBubbles() = logIfError {
        floatingBubble!!.showIcon()
    }


    private fun tryRemoveBubbles() = logIfError {
        floatingBubble!!.removeIcon()
        floatingBubble!!.removeRemoveIcon()
    }


}

