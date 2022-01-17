package com.torrydo.floatingbubbleview.main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.torrydo.floatingbubbleview.physics.FloatingBubbleTouchListener
import com.torrydo.floatingbubbleview.utils.logger.Logger
import com.torrydo.floatingbubbleview.utils.toPx
import com.torrydo.floatingbubbleview.utils.toTag

class FloatingBubbleBuilder : IFloatingBubbleBuilder {

    private val logger = Logger()
        .setTag(javaClass.simpleName.toTag())
        .setDebugEnabled(true)

    var context: Context? = null

    var iconBitmap: Bitmap? = null
    var iconRemoveBitmap: Bitmap? = null
    
    var listener: FloatingBubbleTouchListener? = null

    var bubleSizePx = 100
    var movable = true
    var startingPoint = Point(0, 0)
    var elevation = 0
    var alphaF = 1f

    // required
    override fun with(context: Context): FloatingBubbleBuilder {
        this.context = context
        return this
    }

    override fun setIcon(resource: Int): FloatingBubbleBuilder {
        iconBitmap = ContextCompat.getDrawable(context!!, resource)!!.toBitmap()
        return this
    }

    override fun setIcon(bitmap: Bitmap): FloatingBubbleBuilder {
        iconBitmap = bitmap
        return this
    }

    override fun setRemoveIcon(resource: Int): FloatingBubbleBuilder {
        iconRemoveBitmap = ContextCompat.getDrawable(context!!, resource)!!.toBitmap()
        return this
    }

    override fun setRemoveIcon(bitmap: Bitmap): FloatingBubbleBuilder {
        iconRemoveBitmap = bitmap
        return this
    }

    override fun addFloatingBubbleTouchListener(listener: FloatingBubbleTouchListener): FloatingBubbleBuilder {
        var tempListener = this.listener
        this.listener = object : FloatingBubbleTouchListener{

            override fun onClick() {
                tempListener?.onClick()
                listener.onClick()
            }

            override fun onDown(x: Int, y: Int) {
                tempListener?.onDown(x, y)
                listener.onDown(x, y)
            }

            override fun onMove(x: Int, y: Int) {
                tempListener?.onMove(x, y)
                listener.onMove(x, y)
            }

            override fun onUp(x: Int, y: Int) {
                tempListener?.onUp(x, y)
                listener.onUp(x, y)
            }

            override fun onDestroy() {
                tempListener?.onDestroy()
                listener.onDestroy()
            }

        }
//        tempListener = null
        return this
    }

    override fun setBubbleSizeDp(dp: Int): FloatingBubbleBuilder {
        bubleSizePx = dp.toPx
        return this
    }

    override fun isMovable(boolean: Boolean): FloatingBubbleBuilder {
        movable = boolean
        return this
    }

    override fun setStartPoint(x: Int, y: Int): FloatingBubbleBuilder {
        startingPoint.x = x
        startingPoint.y = y
        return this
    }

    override fun setElevation(dp: Int): FloatingBubbleBuilder {
        elevation = dp
        return this
    }

    override fun setAlpha(alpha: Float): FloatingBubbleBuilder {
        this.alphaF = alpha
        return this
    }

    override fun build(): FloatingBubble {
        return FloatingBubble(this)
    }
}