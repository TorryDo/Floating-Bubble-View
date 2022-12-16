package com.torrydo.floatingbubbleview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.util.Size
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap

class FloatingBubble
internal constructor(
    private val builder: Builder
) : Logger by LoggerImpl() {

    private var floatingBottomBackground: FloatingBottomBackground? = null

    init {
        ScreenInfo.getScreenSize(builder.context).also {
            ScreenInfo.widthPx = it.width
            ScreenInfo.heightPx = it.height
        }
        ScreenInfo.statusBarHeightPx = ScreenInfo.getStatusBarHeight(builder.context)
        ScreenInfo.softNavBarHeightPx = ScreenInfo.getSoftNavigationBarSize(builder.context)

        builder.iconBitmap?.let {

            FloatingBubbleIcon.apply {
                builder.bubbleSizePx.also {
                    if (it.notZero()) {
                        widthPx = it.width
                        heightPx = it.height
                    }
                }
            }

            FloatingCloseBubbleIcon.apply {
                builder.closeBubbleSizePx.also {
                    if (it.notZero()) {
                        widthPx = it.width
                        heightPx = it.height
                    } else {
                        widthPx = FloatingBubbleIcon.widthPx
                        heightPx = FloatingBubbleIcon.heightPx
                    }
                }
            }

        }

        if(builder.isBottomBackgroundEnabled){
            floatingBottomBackground = FloatingBottomBackground(builder)
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
        floatingIcon.show()
    }

    fun removeIcon() {
        floatingIcon.remove()
    }

    fun showCloseIconAndBackground() {
        floatingBottomBackground?.show()
        floatingCloseIcon.show()

    }

    fun removeCloseIconAndBackground() {
        floatingBottomBackground?.remove()
        floatingCloseIcon.remove()
    }


    // private func --------------------------------------------------------------------------------

    private inner class CustomBubbleTouchListener : TouchEvent {

        private var isBubbleMoving = false

        override fun onMove(x: Int, y: Int) {
            if (isBubbleMoving) {
                floatingCloseIcon.animateCloseIconByBubble(x, y)
                return
            }
            if (builder.isCloseIconEnabled.not()) return

            showCloseIconAndBackground()

            if (isBubbleMoving.not()) {
                isBubbleMoving = true
            }

        }

        override fun onUp(x: Int, y: Int) {
            isBubbleMoving = false
            removeCloseIconAndBackground()

            if (isBubbleInsideCloseIcon()) {
                builder.listener?.onDestroy()
            } else {
                if (builder.isAnimateToEdgeEnabled) {
                    floatingIcon.animateIconToEdge()
                }
            }
        }
    }

    private val floatingIcon: FloatingBubbleIcon = FloatingBubbleIcon(
        builder.addFloatingBubbleTouchListener(CustomBubbleTouchListener())
    )

    private val floatingCloseIcon: FloatingCloseBubbleIcon = FloatingCloseBubbleIcon(builder)

    private fun isBubbleInsideCloseIcon(): Boolean {
        return floatingCloseIcon.distanceRatioToCloseBubble(
            x = floatingIcon.x,
            y = floatingIcon.y
        ) == 0.0f
    }

    // builder -------------------------------------------------------------------------------------

    class Builder(internal val context: Context) {

        private val DEFAULT_BUBBLE_SIZE_PX = 160

        internal var iconView: View? = null
        internal var iconBitmap: Bitmap? = null
        internal var closeIconBitmap: Bitmap? = null

        internal var bubbleStyle: Int? = R.style.default_bubble_style
        internal var closeBubbleStyle: Int? = R.style.default_close_bubble_style

        internal var listener: TouchEvent? = null

        internal var bubbleSizePx: Size = Size(DEFAULT_BUBBLE_SIZE_PX, DEFAULT_BUBBLE_SIZE_PX)
        internal var closeBubbleSizePx: Size = Size(DEFAULT_BUBBLE_SIZE_PX, DEFAULT_BUBBLE_SIZE_PX)

        internal var startingPoint = Point(0, 0)
        internal var elevation = 0
        internal var alphaF = 1f
        internal var isCloseIconEnabled = true
        internal var isAnimateToEdgeEnabled = true
        internal var isBottomBackgroundEnabled = false

        fun bottomBackground(enabled: Boolean): Builder {
            this.isBottomBackgroundEnabled = enabled
            return this
        }

        fun enableAnimateToEdge(enabled: Boolean): Builder {
            isAnimateToEdgeEnabled = enabled
            return this
        }

        fun enableCloseIcon(enabled: Boolean): Builder {
            isCloseIconEnabled = enabled
            return this
        }

        fun setBubble(view: View): Builder {
            iconView = view
            return this
        }

        fun setBubble(@DrawableRes drawable: Int): Builder {
            iconBitmap = ContextCompat.getDrawable(context, drawable)!!.toBitmap()
            return this
        }

        fun setBubble(bitmap: Bitmap): Builder {
            iconBitmap = bitmap
            return this
        }

        fun setBubbleStyle(@StyleRes style: Int?): Builder {
            this.bubbleStyle = style
            return this
        }

        fun setCloseBubble(@DrawableRes drawable: Int): Builder {
            closeIconBitmap = ContextCompat.getDrawable(context, drawable)!!.toBitmap()
            return this
        }

        fun setCloseBubble(bitmap: Bitmap): Builder {
            closeIconBitmap = bitmap
            return this
        }

        fun setCloseBubbleStyle(@StyleRes style: Int?): Builder {
            this.closeBubbleStyle = style
            return this
        }

        fun setBubbleSizeDp(width: Int, height: Int): Builder {
            bubbleSizePx = Size(width.toPx, height.toPx)
            return this
        }

        fun setCloseBubbleSizeDp(width: Int, height: Int): Builder {
            closeBubbleSizePx = Size(width.toPx, height.toPx)
            return this
        }

        fun addFloatingBubbleTouchListener(event: TouchEvent): Builder {

            val tempListener = this.listener
            this.listener = object : TouchEvent {

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
            return this
        }

        /**
         * examples: x=0, y=0 show bubble on the top-left corner of the screen.
         *
         * you can set x/y as negative value, but the bubble will be outside the screen.
         *
         * @param x 0 ... screenWidth (px).
         * @param y 0 ... screenHeight (px).
         * */
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