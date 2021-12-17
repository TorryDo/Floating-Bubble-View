package com.torrydo.floatingbubble

import android.content.Context
import android.graphics.Bitmap
import com.torrydo.floatingbubble.utils.Extension.toBitmap

class FloatingBubbleBuilder : IFloatingBubbleBuilder {

    lateinit var context: Context

    var iconBitmap: Bitmap? = null
    var iconRemoveBitmap: Bitmap? = null

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

    override fun setRemoveIcon(resource: Int): FloatingBubbleBuilder {
        iconRemoveBitmap = resource.toBitmap()
        return this
    }

    override fun setRemoveIcon(bitmap: Bitmap): FloatingBubbleBuilder {
        iconRemoveBitmap = bitmap
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
        return FloatingBubble(this)
    }
}