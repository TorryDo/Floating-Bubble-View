package com.torrydo.floatingbubbleview.service.expandable

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.Discouraged
import androidx.annotation.StyleRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.torrydo.floatingbubbleview.AndroidVersions
import com.torrydo.floatingbubbleview.CloseBubbleBehavior
import com.torrydo.floatingbubbleview.bubble.FloatingBubble
import com.torrydo.floatingbubbleview.FloatingBubbleListener
import com.torrydo.floatingbubbleview.R
import com.torrydo.floatingbubbleview.toPx

class BubbleBuilder(
    private val context: Context
) {

    // bubble
    internal var bubbleView: View? = null
    internal var bubbleCompose: ComposeView? = null
    private var bubbleStyle: Int? = R.style.default_bubble_style

    // close-bubble
    internal var closeView: View? = null
    internal var closeBubbleStyle: Int? = null

    // config
    private var startPoint = Point(0, 0)
    internal var isAnimateToEdgeEnabled = true
    internal var isBottomBackgroundEnabled = false

    internal var distanceToClosePx = 200
    internal var closeBubbleBottomPaddingPx = 80
    internal var triggerClickablePerimeterPx = 5f

    internal var listener: FloatingBubbleListener? = null
    internal var behavior: CloseBubbleBehavior = CloseBubbleBehavior.FIXED_CLOSE_BUBBLE

    internal var forceDragging: Boolean = true
    internal var isBubbleDraggable: Boolean = true

    fun defaultLayoutParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT

            x = startPoint.x
            y = startPoint.y

            bubbleStyle?.let {
                windowAnimations = it
            }

            gravity = Gravity.TOP or Gravity.LEFT
            format = PixelFormat.TRANSLUCENT

            type = if (Build.VERSION.SDK_INT >= AndroidVersions.`8`) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
        }

    }

    /**
     * Set the clickable perimeter of the bubble in pixels (default = 5f).
     *
     * For example, when the bubble is dragged, it will still perform a click if
     * the new location (when the bubble is released) is not further than the last location by the specified pixel amount.
     *
     * @param f The size of the clickable area in pixels.
     * @return This BubbleBuilder instance for method chaining.
     */
    @Discouraged("the name will be changed if I found a better one")
    fun triggerClickablePerimeterPx(f: Float): BubbleBuilder{
        this.triggerClickablePerimeterPx = f
        return this
    }

    fun bubbleDraggable(b: Boolean): BubbleBuilder{
        isBubbleDraggable = b
        return this
    }

    fun forceDragging(b: Boolean): BubbleBuilder {
        this.forceDragging = b
        return this
    }

    /**
     * choose behavior for the bubbles
     * */
    fun closeBehavior(behavior: CloseBubbleBehavior): BubbleBuilder {
        this.behavior = behavior
        return this
    }

    /**
     * the more value, the larger closeable area
     *
     * @param dp distance between bubble and close-bubble
     * */
    fun distanceToClose(dp: Int): BubbleBuilder {
        this.distanceToClosePx = dp.toPx()
        return this
    }

    /**
     * @param enabled show gradient dark background on the bottom of the screen
     * */
    fun bottomBackground(enabled: Boolean): BubbleBuilder {
        this.isBottomBackgroundEnabled = enabled
        return this
    }

    /**
     * @param enabled animate the bubble to the left/right side of the screen when finger is released, true by default
     * */
    fun enableAnimateToEdge(enabled: Boolean): BubbleBuilder {
        isAnimateToEdgeEnabled = enabled
        return this
    }

    @Discouraged("when using bubbleCompose, you should set `forceDragging` to false. otherwise the bubbleCompose may not works correctly in some cases")
    fun bubbleCompose(content: @Composable () -> Unit): BubbleBuilder {
        this.bubbleCompose = ComposeView(context).apply {
            setContent(content)
        }
        return this
    }

    /**
     * set view to bubble
     */
    fun bubbleView(view: View): BubbleBuilder {
        bubbleView = view
        return this
    }


    /**
     * set open and exit animation to bubble
     * */
    fun bubbleStyle(@StyleRes style: Int?): BubbleBuilder {
        this.bubbleStyle = style
        return this
    }

    fun closeBubbleView(view: View): BubbleBuilder {
        this.closeView = view
        return this
    }

    /**
     * set open and exit style to close-bubble
     * */
    fun closeBubbleStyle(@StyleRes style: Int?): BubbleBuilder {
        this.closeBubbleStyle = style
        return this
    }

    /**
     * add a listener, pass an instance of FloatingBubble.Action
     * @param FloatingBubble.Listener
     * */
    fun addFloatingBubbleListener(listener: FloatingBubbleListener): BubbleBuilder {

        val tempListener = this.listener
        this.listener = object : FloatingBubbleListener {

            override fun onFingerDown(x: Float, y: Float) {
                tempListener?.onFingerDown(x, y)
                listener.onFingerDown(x, y)
            }

            override fun onFingerMove(x: Float, y: Float) {
                tempListener?.onFingerMove(x, y)
                listener.onFingerMove(x, y)
            }

            override fun onFingerUp(x: Float, y: Float) {
                tempListener?.onFingerUp(x, y)
                listener.onFingerUp(x, y)
            }

        }
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
    fun startLocation(x: Int, y: Int): BubbleBuilder {
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
    fun startLocationPx(x: Int, y: Int): BubbleBuilder {
        startPoint.x = x
        startPoint.y = y
        return this
    }

}