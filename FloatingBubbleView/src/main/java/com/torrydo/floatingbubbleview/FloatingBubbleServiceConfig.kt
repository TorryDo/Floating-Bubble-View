package com.torrydo.floatingbubbleview

import android.app.Service

abstract class FloatingBubbleServiceConfig : Service() {

    private var floatingBubble: FloatingBubble? = null
    private var expandableView: ExpandableView? = null

    // lifecycle -----------------------------------------------------------------------------------

    override fun onDestroy() {
        tryRemoveAllView()
        super.onDestroy()
    }

    // override ------------------------------------------------------------------------------------

    abstract fun setupBubble(action: FloatingBubble.Action): FloatingBubble.Builder

    open fun setupExpandableView(action: ExpandableView.Action): ExpandableView.Builder? = null

    // public func ---------------------------------------------------------------------------------
    protected fun setupViewAppearance() {

        floatingBubble = setupBubble(customFloatingBubbleAction)
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

        override fun onDestroy() {
            tryStopService()
        }

    }

    private val customFloatingBubbleAction = object : FloatingBubble.Action {

        override fun navigateToExpandableView() {
            tryNavigateToExpandableView()
        }
    }


    private fun tryNavigateToExpandableView() {
        tryShowExpandableView()
            .onComplete {
                tryRemoveBubbles()
            }.onError {
                throw NullViewException("you DID NOT override expandable view")
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

    // shorten -------------------------------------------------------------------------------------

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
        floatingBubble!!.removeCloseIconAndBackground()
    }


}

