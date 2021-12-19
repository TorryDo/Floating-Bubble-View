package com.torrydo.floatingbubble

import com.torrydo.floatingbubble.logger.Logger
import com.torrydo.floatingbubble.main_icon.FloatingBubbleIcon
import com.torrydo.floatingbubble.physics.FloatingBubbleTouchListener
import com.torrydo.floatingbubble.utils.Constant
import com.torrydo.floatingbubble.utils.ScreenInfo
import com.torrydo.floatingbubble.utils.toTag

class FloatingBubble(
    bubbleBuilder: FloatingBubbleBuilder
) {

    private val logger =
        Logger().setTag(javaClass.simpleName.toTag()).setDebugEnabled(Constant.IS_DEBUG_ENABLED)

    private var floatingIcon =
        FloatingBubbleIcon(bubbleBuilder, ScreenInfo.getScreenSize(bubbleBuilder.context))

    private var IS_BUBBLE_CLICKABLE = true

    init {
        addBubbleListener()
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
            override fun onDown(x: Float, y: Float) {
                logger.log("on down $x | $y")

                IS_BUBBLE_CLICKABLE = true
            }

            override fun onMove(x: Float, y: Float) {
//                logger.log("on move $x | $y")
                if(IS_BUBBLE_CLICKABLE) IS_BUBBLE_CLICKABLE = false
            }

            override fun onUp(x: Float, y: Float) {
                logger.log("on up $x | $y")
                if(IS_BUBBLE_CLICKABLE) {
                    logger.log("remove bubble")
                    remove()
                }
                floatingIcon.animateIconToEdge(68){}
            }

            override fun onRemove() {
                logger.log("on remove")
            }
        })
    }

}