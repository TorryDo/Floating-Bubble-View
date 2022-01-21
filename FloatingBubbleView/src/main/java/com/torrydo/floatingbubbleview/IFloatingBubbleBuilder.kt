package com.torrydo.floatingbubbleview

import android.content.Context
import android.graphics.Bitmap

interface IFloatingBubbleBuilder {

    fun with(context: Context): IFloatingBubbleBuilder

    fun setIcon(resource: Int): IFloatingBubbleBuilder
    fun setIcon(bitmap: Bitmap): IFloatingBubbleBuilder

    fun setRemoveIcon(resource: Int): IFloatingBubbleBuilder
    fun setRemoveIcon(bitmap: Bitmap): IFloatingBubbleBuilder

    fun addFloatingBubbleTouchListener(listener: FloatingBubbleTouchListener): IFloatingBubbleBuilder

    fun setBubbleSizeDp(dp: Int): IFloatingBubbleBuilder

    fun isMovable(boolean: Boolean): IFloatingBubbleBuilder
    fun setStartPoint(x: Int, y: Int): IFloatingBubbleBuilder
    fun setElevation(dp: Int): IFloatingBubbleBuilder
    fun setAlpha(alpha: Float): IFloatingBubbleBuilder

    fun build(): FloatingBubble

}