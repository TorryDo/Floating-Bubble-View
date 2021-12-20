package com.torrydo.floatingbubbleview.main

import android.content.Context
import android.graphics.Bitmap
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableViewBuilder

interface IFloatingBubbleBuilder {

    fun with(context: Context): IFloatingBubbleBuilder

    fun setIcon(resource: Int): IFloatingBubbleBuilder
    fun setIcon(bitmap: Bitmap): IFloatingBubbleBuilder

    fun setRemoveIcon(resource: Int): IFloatingBubbleBuilder
    fun setRemoveIcon(bitmap: Bitmap): IFloatingBubbleBuilder

    fun setExpandableViewBuilder(evBuilder: ExpandableViewBuilder): IFloatingBubbleBuilder

    fun setBubbleSize(dp: Int): IFloatingBubbleBuilder

    fun setMovable(boolean: Boolean): IFloatingBubbleBuilder
    fun setElevation(dp: Int): IFloatingBubbleBuilder
    fun setAlpha(alpha: Float): IFloatingBubbleBuilder

    fun build(): FloatingBubble

}