package com.torrydo.floatingbubbleview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap

class FloatingBubble(
    private val bubbleBuilder: FloatingBubble.Builder
) {

    private val REMOVE_BIN_ICON_SIZE = 150  // check later
    private var DEFAULT_HALF_ICON_SIZE_PX = 68  // should replace this


    // listener ------------------------------------------------------------------------------------


    interface Action{

        /**
         * if you do not override expandable view, throw exception
         * */
        fun navigateToExpandableView(){}

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

    fun showRemoveIcon() {
        floatingRemoveIcon.show()
    }

    fun removeRemoveIcon() {
        floatingRemoveIcon.remove()
    }


    // private func --------------------------------------------------------------------------------

    private inner class CustomBubbleTouchListener : TouchEvent {

        private var isBubbleMoving = false

        override fun onMove(x: Int, y: Int) {
            if (isBubbleMoving) return
            showRemoveIcon()
            isBubbleMoving = true
        }

        override fun onUp(x: Int, y: Int) {
            removeRemoveIcon()
            isBubbleMoving = false

            stopServiceIfSuitableCondition()

        }
    }

    private var floatingIcon: FloatingBubbleIcon = FloatingBubbleIcon(
        bubbleBuilder.addFloatingBubbleTouchListener(CustomBubbleTouchListener()),
        ScreenInfo.getScreenSize(bubbleBuilder.context!!)
    )

    private var floatingRemoveIcon: FloatingRemoveBubbleIcon = FloatingRemoveBubbleIcon(
        bubbleBuilder,
        ScreenInfo.getScreenSize(bubbleBuilder.context!!)
    )

    private fun stopServiceIfSuitableCondition(): Boolean {
        // get X and Y of binIcon
        val arrBin = floatingRemoveIcon.binding.homeLauncherMainBinIcon.getXYPointOnScreen()

        val binXmin = arrBin.x - REMOVE_BIN_ICON_SIZE
        val binXmax = arrBin.x + REMOVE_BIN_ICON_SIZE

        val binYmin = arrBin.y - REMOVE_BIN_ICON_SIZE
        val binYmax = arrBin.y + REMOVE_BIN_ICON_SIZE

        // get X and Y of Main Icon
        val iconArr = floatingIcon.binding.homeLauncherMainIcon.getXYPointOnScreen()

        val currentIconX = iconArr.x
        val currentIconY = iconArr.y

        fun isIconWidthInsideRemoveIcon() = binXmin < currentIconX && currentIconX < binXmax
        fun isIconHeightInsideRemoveIcon() = binYmin < currentIconY && currentIconY < binYmax

        if (isIconWidthInsideRemoveIcon() && isIconHeightInsideRemoveIcon()) {
            bubbleBuilder.listener?.onDestroy()
            return true
        }

        floatingIcon.animateIconToEdge(DEFAULT_HALF_ICON_SIZE_PX) {}

        return false
    }

    // builder class -------------------------------------------------------------------------------

    class Builder : IFloatingBubbleBuilder {

        var context: Context? = null

        var iconBitmap: Bitmap? = null
        var iconRemoveBitmap: Bitmap? = null

        var listener: FloatingBubble.TouchEvent? = null

        var bubleSizePx = 100
        var movable = true
        var startingPoint = Point(0, 0)
        var elevation = 0
        var alphaF = 1f

        // required
        override fun with(context: Context): Builder {
            this.context = context

            return this
        }

        override fun setIcon(resource: Int): Builder {
            iconBitmap = ContextCompat.getDrawable(context!!, resource)!!.toBitmap()
            return this
        }

        override fun setIcon(bitmap: Bitmap): Builder {
            iconBitmap = bitmap
            return this
        }

        override fun setRemoveIcon(resource: Int): Builder {
            iconRemoveBitmap = ContextCompat.getDrawable(context!!, resource)!!.toBitmap()
            return this
        }

        override fun setRemoveIcon(bitmap: Bitmap): Builder {
            iconRemoveBitmap = bitmap
            return this
        }

        override fun addFloatingBubbleTouchListener(event: FloatingBubble.TouchEvent): Builder {
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

        override fun setBubbleSizeDp(dp: Int): Builder {
            bubleSizePx = dp.toPx
            return this
        }

        override fun isMovable(boolean: Boolean): Builder {
            movable = boolean
            return this
        }

        override fun setStartPoint(x: Int, y: Int): Builder {
            startingPoint.x = x
            startingPoint.y = y
            return this
        }

        override fun setElevation(dp: Int): Builder {
            elevation = dp
            return this
        }

        override fun setAlpha(alpha: Float): Builder {
            this.alphaF = alpha
            return this
        }

        override fun build(): FloatingBubble {
            return FloatingBubble(this)
        }
    }

}

private interface IFloatingBubbleBuilder {

    fun with(context: Context): IFloatingBubbleBuilder

    fun setIcon(resource: Int): IFloatingBubbleBuilder
    fun setIcon(bitmap: Bitmap): IFloatingBubbleBuilder

    fun setRemoveIcon(resource: Int): IFloatingBubbleBuilder
    fun setRemoveIcon(bitmap: Bitmap): IFloatingBubbleBuilder

    fun addFloatingBubbleTouchListener(event: FloatingBubble.TouchEvent): IFloatingBubbleBuilder

    fun setBubbleSizeDp(dp: Int): IFloatingBubbleBuilder

    fun isMovable(boolean: Boolean): IFloatingBubbleBuilder
    fun setStartPoint(x: Int, y: Int): IFloatingBubbleBuilder
    fun setElevation(dp: Int): IFloatingBubbleBuilder
    fun setAlpha(alpha: Float): IFloatingBubbleBuilder

    fun build(): FloatingBubble

}