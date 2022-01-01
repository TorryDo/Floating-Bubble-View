package com.torrydo.floatingbubbleview.main

import android.content.Context
import android.graphics.Bitmap
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableViewBuilder
import com.torrydo.floatingbubbleview.physics.FloatingBubbleTouchListener

interface IFloatingBubbleBuilder {

    fun with(context: Context): IFloatingBubbleBuilder

    fun setIcon(resource: Int): IFloatingBubbleBuilder
    fun setIcon(bitmap: Bitmap): IFloatingBubbleBuilder

    fun setRemoveIcon(resource: Int): IFloatingBubbleBuilder
    fun setRemoveIcon(bitmap: Bitmap): IFloatingBubbleBuilder

    fun addFloatingBubbleTouchListener(listener: FloatingBubbleTouchListener): IFloatingBubbleBuilder

    fun setBubbleSizeDp(dp: Int): IFloatingBubbleBuilder

    fun setMovable(boolean: Boolean): IFloatingBubbleBuilder
    fun setStartingPoint(x: Int, y: Int): IFloatingBubbleBuilder
    fun setElevation(dp: Int): IFloatingBubbleBuilder
    fun setAlpha(alpha: Float): IFloatingBubbleBuilder

    fun build(): FloatingBubble

}