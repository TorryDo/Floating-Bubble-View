package com.torrydo.floatingbubble

import android.content.Context
import android.graphics.Bitmap

interface IFloatingBubbleBuilder {

    fun with(context: Context): FloatingBubbleBuilder

    fun setIcon(resource: Int): FloatingBubbleBuilder
    fun setIcon(bitmap: Bitmap): FloatingBubbleBuilder

    fun setRemoveIcon(resource: Int): FloatingBubbleBuilder
    fun setRemoveIcon(bitmap: Bitmap): FloatingBubbleBuilder

    fun setBubbleSize(dp: Int): FloatingBubbleBuilder

    fun setMovable(boolean: Boolean): FloatingBubbleBuilder
    fun setElevation(dp: Int): FloatingBubbleBuilder
    fun setAlpha(alpha: Float): FloatingBubbleBuilder

    fun build(): FloatingBubble

}