package com.torrydo.floatingbubbleview.main

import android.content.Context
import android.graphics.Bitmap
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableViewBuilder
import com.torrydo.floatingbubbleview.physics.FloatingBubbleTouchListener
import com.torrydo.floatingbubbleview.utils.Extension.toBitmap

class FloatingBubbleBuilder : IFloatingBubbleBuilder {

    lateinit var context: Context

    var iconBitmap: Bitmap? = null
    var iconRemoveBitmap: Bitmap? = null

    var evBuilder: ExpandableViewBuilder? = null

    var listener: FloatingBubbleTouchListener? = object : FloatingBubbleTouchListener{}

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

    override fun setExpandableViewBuilder(evBuilder: ExpandableViewBuilder): FloatingBubbleBuilder {
        this.evBuilder = evBuilder
        return this
    }

    override fun addFloatingBubbleTouchListener(listener: FloatingBubbleTouchListener): IFloatingBubbleBuilder {
        this.listener = listener
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