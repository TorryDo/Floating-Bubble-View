package com.torrydo.floatingbubbleview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.util.Size
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.torrydo.floatingbubbleview.FloatingCloseBubbleIcon.Companion.DEFAULT_LARGER_PX

class FloatingBubble
internal constructor(
    private val bubbleBuilder: Builder
) : Logger by LoggerImpl() {

    init {
        ScreenInfo.screenSize = ScreenInfo.getScreenSize(bubbleBuilder.context)

        bubbleBuilder.iconBitmap?.let {

            FloatingBubbleIcon.apply {
                if(bubbleBuilder.bubbleSizePx.notZero()){
                    widthPx = bubbleBuilder.bubbleSizePx.width
                    heightPx = bubbleBuilder.bubbleSizePx.height
                }else{
                    widthPx = it.width
                    heightPx = it.height
                }
            }

            FloatingCloseBubbleIcon.apply {
                widthPx = FloatingBubbleIcon.widthPx + DEFAULT_LARGER_PX
                heightPx = FloatingBubbleIcon.heightPx + DEFAULT_LARGER_PX
            }

        }

    }

    // listener ------------------------------------------------------------------------------------


    interface Action {

        /**
         * if you do not override expandable view, throw exception
         * */
        fun navigateToExpandableView() {}

    }

    interface TouchEvent {

        fun onDown(x: Int, y: Int) {}

        fun onUp(x: Int, y: Int) {}

        fun onMove(x: Int, y: Int) {}

        fun onClick() {}

        fun onDestroy() {}

    }

    // public func ---------------------------------------------------------------------------------

    fun showIcon() {
        floatingIcon.show().onError {
            e("force destroy because not show")
            bubbleBuilder.listener?.onDestroy()
        }
    }

    fun removeIcon() {
        floatingIcon.remove()
    }

    fun showCloseIcon() {
        floatingCloseIcon.show()
    }

    fun removeCloseIcon() {
        floatingCloseIcon.remove()
    }


    // private func --------------------------------------------------------------------------------

    private inner class CustomBubbleTouchListener : TouchEvent {

        private var isBubbleMoving = false

        override fun onMove(x: Int, y: Int) {
            if (isBubbleMoving) return
            if (bubbleBuilder.isCloseIconEnabled.not()) return

            showCloseIcon()
            isBubbleMoving = true

        }

        override fun onUp(x: Int, y: Int) {
            removeCloseIcon()
            isBubbleMoving = false

            stopServiceIfSuitableCondition()

        }
    }

    private var floatingIcon: FloatingBubbleIcon = FloatingBubbleIcon(
        bubbleBuilder.addFloatingBubbleTouchListener(CustomBubbleTouchListener())
    )

    private var floatingCloseIcon: FloatingCloseBubbleIcon = FloatingCloseBubbleIcon(
        bubbleBuilder
    )

    private fun stopServiceIfSuitableCondition(): Boolean {
        val closePoint = floatingCloseIcon.binding.homeLauncherMainBinIcon.getXYPointOnScreen()

        val closeXmin = closePoint.x - FloatingCloseBubbleIcon.widthPx
        val closeXmax = closePoint.x + FloatingCloseBubbleIcon.widthPx

        val closeYmin = closePoint.y - FloatingCloseBubbleIcon.heightPx
        val closeYmax = closePoint.y + FloatingCloseBubbleIcon.heightPx

        val bubblePoint = floatingIcon.binding.homeLauncherMainIcon.getXYPointOnScreen()

        val bubbleX = bubblePoint.x
        val bubbleY = bubblePoint.y

        fun isBubbleXInsideCloseWidth() = (closeXmin < bubbleX) && (bubbleX < closeXmax)
        fun isBubbleYInsideCloseHeight() = (closeYmin < bubbleY) && (bubbleY < closeYmax)

        if (isBubbleXInsideCloseWidth() && isBubbleYInsideCloseHeight()) {
            bubbleBuilder.listener?.onDestroy()
            return true
        }

        if (bubbleBuilder.isAnimateToEdgeEnabled) {
            floatingIcon.animateIconToEdge()
        }

        return false
    }

    // builder -------------------------------------------------------------------------------------

    class Builder(internal val context: Context) {

        internal var iconView: View? = null
        internal var iconBitmap: Bitmap? = null
        internal var iconRemoveBitmap: Bitmap? = null

        internal var listener: FloatingBubble.TouchEvent? = null

        internal var bubbleSizePx: Size = Size(0, 0)

        internal var isMovable = true
        internal var startingPoint = Point(0, 0)
        internal var elevation = 0
        internal var alphaF = 1f
        internal var isCloseIconEnabled = true
        internal var isAnimateToEdgeEnabled = true

        fun enableAnimateToEdge(enabled: Boolean): Builder {
            isAnimateToEdgeEnabled = enabled
            return this
        }

        fun enableCloseIcon(enabled: Boolean): Builder {
            isCloseIconEnabled = enabled
            return this
        }

        fun setIcon(view: View): Builder {
            iconView = view
            return this
        }

        fun setIcon(@DrawableRes drawable: Int): Builder {
            iconBitmap = ContextCompat.getDrawable(context, drawable)!!.toBitmap()
            return this
        }

        fun setIcon(bitmap: Bitmap): Builder {
            iconBitmap = bitmap
            return this
        }

        fun setCloseIcon(resource: Int): Builder {
            iconRemoveBitmap = ContextCompat.getDrawable(context, resource)!!.toBitmap()
            return this
        }

        fun setCloseIcon(bitmap: Bitmap): Builder {
            iconRemoveBitmap = bitmap
            return this
        }

        fun setBubbleSizeDp(width: Int, height: Int): Builder {
            bubbleSizePx = Size(width.toPx, height.toPx)
            return this
        }

        fun addFloatingBubbleTouchListener(event: FloatingBubble.TouchEvent): Builder {

            val tempListener = this.listener
            this.listener = object : FloatingBubble.TouchEvent {

                override fun onClick() {
                    tempListener?.onClick()
                    event.onClick()
                }

                override fun onDown(x: Int, y: Int) {
                    tempListener?.onDown(x, y)
                    event.onDown(x, y)
                }

                override fun onMove(x: Int, y: Int) {
                    tempListener?.onMove(x, y)
                    event.onMove(x, y)
                }

                override fun onUp(x: Int, y: Int) {
                    tempListener?.onUp(x, y)
                    event.onUp(x, y)
                }

                override fun onDestroy() {
                    tempListener?.onDestroy()
                    event.onDestroy()
                }

            }
//        tempListener = null
            return this
        }

        fun isMovable(enabled: Boolean): Builder {
            throw Exception("currently working on this, not implemented yet")
//            isMovable = enabled
//            return this
        }

        fun setStartPoint(x: Int, y: Int): Builder {
            startingPoint.x = x
            startingPoint.y = y
            return this
        }

        fun setElevation(dp: Int): Builder {
            elevation = dp
            return this
        }

        fun setAlpha(alpha: Float): Builder {
            this.alphaF = alpha
            return this
        }

        internal fun build(): FloatingBubble {
            return FloatingBubble(this)
        }
    }

}