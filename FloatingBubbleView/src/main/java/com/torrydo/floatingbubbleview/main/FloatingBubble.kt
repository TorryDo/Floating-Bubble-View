package com.torrydo.floatingbubbleview.main

import com.torrydo.floatingbubbleview.main.bubble.icon.FloatingBubbleIcon
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableView
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableViewListener
import com.torrydo.floatingbubbleview.physics.FloatingBubbleTouchListener
import com.torrydo.floatingbubbleview.utils.Constant
import com.torrydo.floatingbubbleview.utils.ScreenInfo
import com.torrydo.floatingbubbleview.utils.logger.Logger
import com.torrydo.floatingbubbleview.utils.toTag

class FloatingBubble(
    private val bubbleBuilder: FloatingBubbleBuilder
) {

    private val logger = Logger()
        .setTag(javaClass.simpleName.toTag())
        .setDebugEnabled(Constant.IS_DEBUG_ENABLED)

    private var floatingIcon = FloatingBubbleIcon(
        bubbleBuilder,
        ScreenInfo.getScreenSize(bubbleBuilder.context)
    )

    private var expandableView: ExpandableView? = null

    private var IS_BUBBLE_CLICKABLE = true

    init {
        addBubbleListener()
        expandableView = bubbleBuilder.evBuilder
            ?.setExpandableViewListener(CustomExpandableViewListener())
            ?.build()
    }

    // public func ---------------------------------------------------------------------------------

    fun show() {
        floatingIcon.show()
    }

    fun remove() {
        floatingIcon.remove()
    }

    // private func --------------------------------------------------------------------------------
    private fun addBubbleListener() {
        floatingIcon.addViewListener(object : FloatingBubbleTouchListener {
            override fun onDown(x: Int, y: Int) {
                logger.log("on down $x | $y")

                IS_BUBBLE_CLICKABLE = true
            }

            override fun onMove(x: Int, y: Int) {

                if (IS_BUBBLE_CLICKABLE) IS_BUBBLE_CLICKABLE = false

            }

            override fun onUp(x: Int, y: Int) {
                logger.log("on up $x | $y")
                if (IS_BUBBLE_CLICKABLE) {
                    logger.log("remove bubble")
                    remove()
                }
                floatingIcon.animateIconToEdge(68) {}
            }

        })
    }

    inner class CustomExpandableViewListener : ExpandableViewListener {
        override fun popToBubble() {

            removeExpandableView()

            logger.log("heelo from floating bubble service")
        }
    }

    private fun removeExpandableView() {

        expandableView?.let { nonNullExpandableView ->
            nonNullExpandableView.remove()
            this.show()
            logger.log("removeExpandableViewAndPopToBubble triggered")
        }
    }

    private fun showExpandableView() {

        this.remove()

        expandableView?.show()


    }

}