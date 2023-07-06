package com.torrydo.floatingbubbleview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.util.Size
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap

class FloatingBubble
internal constructor(
    private val builder: Builder
) {

    private var bubbleView: FloatingBubbleView
    private var closeBubbleView: FloatingCloseBubbleView? = null
    private var bottomBackground: FloatingBottomBackground? = null

    private var isComposeInit = false

    init {
        ScreenInfo.onOrientationChanged(builder.context)

        bubbleView = FloatingBubbleView(
            builder.addFloatingBubbleListener(CustomBubbleListener())
        )

        if (builder.isCloseBubbleEnabled) {
            closeBubbleView = FloatingCloseBubbleView(builder)
        }

        if (builder.isBottomBackgroundEnabled) {
            bottomBackground = FloatingBottomBackground(builder)
        }

    }

    // listener ------------------------------------------------------------------------------------


    interface Action {

        /**
         * if you do not override expandable view, throw exception
         * */
        fun navigateToExpandableView() {}

    }

    interface Listener {

        /**
         * the location of the finger on the screen
         * */
        fun onDown(x: Float, y: Float) {}

        /**
         * the location of the finger on the screen
         * */
        fun onUp(x: Float, y: Float) {}

        /**
         * the location of the finger on the screen
         * */
        fun onMove(x: Float, y: Float) {}

    }

    internal interface ServiceInteractor {
        fun requestStop()
    }

    //region public methods ------------------------------------------------------------------------

    internal fun showIcon() {

        if(builder.composeLifecycleOwner != null){
            if(isComposeInit.not()){
                builder.composeLifecycleOwner?.onCreate()
                isComposeInit = true
            }

            builder.composeLifecycleOwner?.apply {
                onStart()
                onResume()
            }
        }

        bubbleView.show()
    }

    internal fun removeIcon() {

        if(builder.composeLifecycleOwner != null && isComposeInit){
            builder.composeLifecycleOwner!!.apply {
                onPause()
                onStop()
                onDestroy()
            }
        }

        bubbleView.remove()
    }

    internal fun tryShowCloseBubbleAndBackground() {
        closeBubbleView?.show()
        bottomBackground?.show()
    }

    internal fun tryRemoveCloseBubbleAndBackground() {
        closeBubbleView?.remove()
        bottomBackground?.remove()
    }

    //endregion

    //region Private func --------------------------------------------------------------------------

    private inner class CustomBubbleListener : Listener {

        private var isCloseBubbleVisible = false
        private var isBubbleAnimated = false

        override fun onMove(x: Float, y: Float) {

            val bubbleSizeCompat = Size(builder.bubbleView!!.width, builder.bubbleView!!.height)

            when (builder.behavior) {
                BubbleBehavior.DYNAMIC_CLOSE_BUBBLE -> {
                    bubbleView.updateLocationUI(x, y)
                    val (bubbleX, bubbleY) = bubbleView.rawLocationOnScreen()
                    closeBubbleView?.animateCloseIconByBubble(bubbleX.toInt(), bubbleY.toInt())
                }

                BubbleBehavior.FIXED_CLOSE_BUBBLE -> {
                    if (isFingerInsideClosableArea(x, y)) {
                        if (isBubbleAnimated.not()) {

                            val xOffset = (closeBubbleView!!.width - bubbleSizeCompat.width) / 2
                            val yOffset = (closeBubbleView!!.height - bubbleSizeCompat.height) / 2

                            val xUpdated = closeBubbleView!!.baseX.toFloat() + xOffset
                            val yUpdated = closeBubbleView!!.baseY.toFloat() + yOffset

                            bubbleView.animateTo(xUpdated, yUpdated)
                            bubbleView.setLocation(xUpdated, yUpdated)

                            isBubbleAnimated = true
                        }

                    } else {
                        isBubbleAnimated = false
                        bubbleView.updateLocationUI(x, y)
                    }
                }
            }

            if (builder.isCloseBubbleEnabled && !isCloseBubbleVisible) {
                tryShowCloseBubbleAndBackground()
                isCloseBubbleVisible = true
            }
        }

        override fun onUp(x: Float, y: Float) {
            isCloseBubbleVisible = false
            tryRemoveCloseBubbleAndBackground()

            val shouldDestroy = when (builder.behavior) {
                BubbleBehavior.FIXED_CLOSE_BUBBLE -> isFingerInsideClosableArea(x, y)
                BubbleBehavior.DYNAMIC_CLOSE_BUBBLE -> {
                    val (bubbleX, bubbleY) = bubbleView.rawLocationOnScreen()
                    isBubbleInsideClosableArea(bubbleX.toInt(), bubbleY.toInt())
                }
            }

            if (shouldDestroy) {
                builder.serviceInteractor?.requestStop()
            } else {
                if (builder.isAnimateToEdgeEnabled) {
                    bubbleView.animateIconToEdge()
                }
            }
        }
    }

    /**
     * pass bubble location
     * */
    private fun isBubbleInsideClosableArea(x: Int, y: Int): Boolean {
        return closeBubbleView?.distanceRatioFromBubbleToClosableArea(
            x = x,
            y = y
        ) == 0.0f
    }

    private fun isFingerInsideClosableArea(x: Float, y: Float): Boolean {
        return closeBubbleView?.distanceRatioFromLocationToClosableArea(
            x = x,
            y = y
        ) == 0.0f
    }

    //endregion

    //region Builder -------------------------------------------------------------------------------

    class Builder(internal val context: Context) {

        private val DEFAULT_BUBBLE_SIZE_PX = 160

        // bubble
        internal var bubbleView: View? = null
        internal var bubbleStyle: Int? = R.style.default_bubble_style

        // close-bubble
        internal var closeIconView: View? = null
        internal var closeIconBitmap: Bitmap? = null
        internal var closeBubbleStyle: Int? = R.style.default_close_bubble_style
        internal var closeBubbleSizePx: Size = Size(DEFAULT_BUBBLE_SIZE_PX, DEFAULT_BUBBLE_SIZE_PX)

        // config
        internal var startPoint = Point(0, 0)
        internal var isCloseBubbleEnabled = true
        internal var isAnimateToEdgeEnabled = true
        internal var isBottomBackgroundEnabled = false

        internal var distanceToCloseDp = 100

        internal var listener: Listener? = null
        internal var serviceInteractor: ServiceInteractor? = null

        internal var behavior: BubbleBehavior = BubbleBehavior.FIXED_CLOSE_BUBBLE

        // composable
        internal var composeView: (@Composable ()->Unit)? = null
        internal var composeLifecycleOwner: ComposeLifecycleOwner? = null


        /**
         * choose behavior for the bubbles
         * */
        fun behavior(behavior: BubbleBehavior): Builder {
            this.behavior = behavior
            return this
        }

        /**
         * the more value, the larger closeable area
         *
         * @param dp distance between bubble and close-bubble
         * */
        fun distanceToClose(dp: Int): Builder {
            this.distanceToCloseDp = dp
            return this
        }

        /**
         * @param enabled show gradient dark background on the bottom of the screen
         * */
        fun bottomBackground(enabled: Boolean): Builder {
            this.isBottomBackgroundEnabled = enabled
            return this
        }

        /**
         * @param enabled animate the bubble to the left/right side of the screen when finger is released, true by default
         * */
        fun enableAnimateToEdge(enabled: Boolean): Builder {
            isAnimateToEdgeEnabled = enabled
            return this
        }

        /**
         * @param enabled show close-bubble or not
         * */
        fun enableCloseBubble(enabled: Boolean): Builder {
            isCloseBubbleEnabled = enabled
            return this
        }

        fun compose(content: @Composable () -> Unit): Builder {
//            bubbleView = FloatingComposeView(context).apply {
//                setContent { content() }
//            }
            composeLifecycleOwner = ComposeLifecycleOwner()
//            composeLifecycleOwner?.attachToDecorView(bubbleView!!)

            composeView = content

            return this
        }

        /**
         * set view to bubble
         */
        fun bubble(view: View): Builder {
            bubbleView = view
//            bubbleSizePx = Size(view.measuredWidth, view.measuredHeight)
            return this
        }


        /**
         * set open and exit animation to bubble
         * */
        fun bubbleStyle(@StyleRes style: Int?): Builder {
            this.bubbleStyle = style
            return this
        }

        // being developed, therefore this function is not exposed to the outside packages
        internal fun closeBubble(view: View): Builder {
            this.closeIconView = view
            return this
        }

        /**
         * set drawable to close-bubble with default size
         * */
        fun closeBubble(@DrawableRes drawable: Int): Builder {
            this.closeIconBitmap = ContextCompat.getDrawable(context, drawable)!!.toBitmap()
            return this
        }

        /**
         * set drawable to close-bubble with given width and height in dp
         * */
        fun closeBubble(@DrawableRes drawable: Int, widthDp: Int, heightDp: Int): Builder {
            this.closeBubbleSizePx = Size(widthDp.toPx(), heightDp.toPx())
            return closeBubble(drawable)
        }

        /**
         * set bitmap to close-bubble with default size
         * */
        fun closeBubble(bitmap: Bitmap): Builder {
            closeIconBitmap = bitmap
            return this
        }

        /**
         * set open and exit style to close-bubble
         * */
        fun closeBubbleStyle(@StyleRes style: Int?): Builder {
            this.closeBubbleStyle = style
            return this
        }

        /**
         * add a listener, pass an instance of FloatingBubble.Action
         * @param FloatingBubble.Listener
         * */
        fun addFloatingBubbleListener(listener: Listener): Builder {

            val tempListener = this.listener
            this.listener = object : Listener {

                override fun onDown(x: Float, y: Float) {
                    tempListener?.onDown(x, y)
                    listener.onDown(x, y)
                }

                override fun onMove(x: Float, y: Float) {
                    tempListener?.onMove(x, y)
                    listener.onMove(x, y)
                }

                override fun onUp(x: Float, y: Float) {
                    tempListener?.onUp(x, y)
                    listener.onUp(x, y)
                }

            }
            return this
        }

        internal fun addServiceInteractor(interactor: ServiceInteractor): Builder {
            this.serviceInteractor = interactor
            return this
        }

        /**
         * examples: x=0, y=0 show the bubble on the top-left corner of the screen.
         *
         * you can set x/y as a negative values, but the bubble will be outside the screen.
         *
         * @param x 0 ... screenWidth (dp).
         * @param y 0 ... screenHeight (dp).
         * */
        fun startLocation(x: Int, y: Int): Builder {
            startPoint.x = x.toPx()
            startPoint.y = y.toPx()
            return this
        }

        /**
         * examples: x=0, y=0 show the bubble on the top-left corner of the screen.
         *
         * you can set x/y as negative values, but the bubble will be outside the screen.
         *
         * @param x 0 ... screenWidth (px).
         * @param y 0 ... screenHeight (px).
         * */
        fun startLocationPx(x: Int, y: Int): Builder {
            startPoint.x = x
            startPoint.y = y
            return this
        }

        internal fun build(): FloatingBubble {
            return FloatingBubble(this)
        }
    }

    //endregion

}