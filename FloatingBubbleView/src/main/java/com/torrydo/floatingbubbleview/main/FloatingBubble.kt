package com.torrydo.floatingbubbleview.main

import com.torrydo.floatingbubbleview.main.bubble.icon.FloatingBubbleIcon
import com.torrydo.floatingbubbleview.physics.FloatingBubbleTouchListener
import com.torrydo.floatingbubbleview.utils.Constant
import com.torrydo.floatingbubbleview.utils.ScreenInfo
import com.torrydo.floatingbubbleview.utils.logger.Logger
import com.torrydo.floatingbubbleview.utils.toTag

open class FloatingBubble(
    private val bubbleBuilder: FloatingBubbleBuilder
) {

    private val logger = Logger()
        .setTag(javaClass.simpleName.toTag())
        .setDebugEnabled(Constant.IS_DEBUG_ENABLED)


    private var floatingIcon = FloatingBubbleIcon(
        bubbleBuilder,
        ScreenInfo.getScreenSize(bubbleBuilder.context)
    )


    // public func ---------------------------------------------------------------------------------

    fun show() {
        floatingIcon.show()
    }

    fun remove() {
        floatingIcon.remove()
    }

    // private func --------------------------------------------------------------------------------



}