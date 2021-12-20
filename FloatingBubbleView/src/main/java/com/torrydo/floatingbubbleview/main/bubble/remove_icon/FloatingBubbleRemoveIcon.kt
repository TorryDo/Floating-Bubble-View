package com.torrydo.floatingbubbleview.main.bubble.remove_icon

import android.util.Size
import com.torrydo.floatingbubbleview.main.FloatingBubbleBuilder
import com.torrydo.floatingbubbleview.main.base.BaseFloatingView

class FloatingBubbleRemoveIcon(
    private val bubbleBuilder: FloatingBubbleBuilder,
    private val screenSize: Size
) : BaseFloatingView(bubbleBuilder.context) {

    init {
        setupDefaultLayoutParams()
    }



}