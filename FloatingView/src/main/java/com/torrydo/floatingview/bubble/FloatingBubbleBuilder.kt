package com.torrydo.floatingview.bubble

import android.content.Context
import android.graphics.Bitmap
import com.torrydo.floatingview.Extension.toBitmap
import com.torrydo.floatingview.R

class FloatingBubbleBuilder : IFloatingBubbleBuilder {

    lateinit var context: Context

    var iconBitmap: Bitmap = (R.drawable.ic_rounded_blue_diamond).toBitmap()
    var iconXBitmap: Bitmap = (R.drawable.ic_rounded_blue_diamond).toBitmap()

    var bubleSize = 40
    var movable = true
    var elevation = 0
    var alpha = 0f

    // required
    override fun with(context: Context): FloatingBubbleBuilder {
        this.context = context
        return this
    }

    override fun setIcon(resource: Int): FloatingBubbleBuilder {
        iconBitmap = resource.toBitmap()
        return this
    }

    override fun setIcon(bitmap: Bitmap): FloatingBubbleBuilder {
        iconBitmap = bitmap
        return this
    }

    override fun setIconX(resource: Int): FloatingBubbleBuilder {
        iconXBitmap = resource.toBitmap()
        return this
    }

    override fun setIconX(bitmap: Bitmap): FloatingBubbleBuilder {
        iconXBitmap = bitmap
        return this
    }

    override fun setBubbleSize(dp: Int): FloatingBubbleBuilder {
        bubleSize = dp
        return this
    }

    override fun setMovable(boolean: Boolean): FloatingBubbleBuilder {
        movable = boolean
        return this
    }

    override fun setElevation(dp: Int): FloatingBubbleBuilder {
        elevation = dp
        return this
    }

    override fun setAlpha(alpha: Float): FloatingBubbleBuilder {
        this.alpha = alpha
        return this
    }

    override fun build(): FloatingBubble {
        return FloatingBubble(
            context = context,
            iconBitmap = iconBitmap,
            iconXBitmap = iconXBitmap,
            bubbleSize = bubleSize,
            movable = movable,
            elevation = elevation,
            alpha = alpha
        )
    }
}