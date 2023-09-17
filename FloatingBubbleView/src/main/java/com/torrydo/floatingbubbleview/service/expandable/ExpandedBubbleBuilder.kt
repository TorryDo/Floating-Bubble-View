package com.torrydo.floatingbubbleview.service.expandable

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.Discouraged
import androidx.annotation.StyleRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.torrydo.floatingbubbleview.AndroidVersions
import com.torrydo.floatingbubbleview.R
import com.torrydo.floatingbubbleview.toPx

class ExpandedBubbleBuilder(
    val context: Context
) {

    internal var expandedView: View? = null
    internal var expandedCompose: ComposeView? = null
    internal var expandedBubbleStyle: Int? = R.style.default_bubble_style

    private var isDrawUnderSystemUI: Boolean = false
    internal var isAnimateToEdgeEnabled: Boolean = false
    internal var isDraggable: Boolean = false

    private var dimAmount = 0.5f
    private var startLocation: Point = Point(0, 0)

    internal var onDispatchKeyEvent: ((KeyEvent) -> Boolean?)? = null

    private var isFillMaxWidth: Boolean = false

    fun defaultLayoutParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
            gravity = Gravity.TOP or Gravity.LEFT
            format = PixelFormat.TRANSLUCENT

            // danger, these may ignore match_parent
            if (isFillMaxWidth.not()) {
                width = WindowManager.LayoutParams.WRAP_CONTENT
            }
            height = WindowManager.LayoutParams.WRAP_CONTENT

            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
//                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND

            x = startLocation.x
            y = startLocation.y

            expandedBubbleStyle?.let {
                windowAnimations = it
            }

            dimAmount = this@ExpandedBubbleBuilder.dimAmount       // default = 0.5f

            type = if (Build.VERSION.SDK_INT >= AndroidVersions.`8`) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
        }
    }

    @Discouraged("specific use-case")
    fun fillMaxWidth(b: Boolean): ExpandedBubbleBuilder {
        isFillMaxWidth = b
        return this
    }

    fun onDispatchKeyEvent(callback: (KeyEvent) -> Boolean?): ExpandedBubbleBuilder {
        this.onDispatchKeyEvent = callback
        return this
    }

    fun startLocation(x: Int, y: Int): ExpandedBubbleBuilder {
        this.startLocation = Point(x.toPx(), y.toPx())
        return this
    }

    fun draggable(enabled: Boolean): ExpandedBubbleBuilder {
        this.isDraggable = enabled
        return this
    }

    /**
     * @param enabled animate the bubble to the left/right side of the screen when finger is released, true by default
     * */
    fun enableAnimateToEdge(enabled: Boolean): ExpandedBubbleBuilder {
        this.isAnimateToEdgeEnabled = enabled
        return this
    }

    /**
     * Allow the expandable-view to display under the system UI elements
     * */
    internal fun drawUnderSystemUI(included: Boolean): ExpandedBubbleBuilder {
        isDrawUnderSystemUI = included
        return this
    }

    fun expandedView(view: View): ExpandedBubbleBuilder {
        if (expandedCompose != null) {
            throw IllegalStateException("Cannot pass view after setting composable")
        }
        this.expandedView = view
        return this
    }

    fun expandedCompose(content: @Composable () -> Unit): ExpandedBubbleBuilder {
        if (expandedView != null) {
            throw IllegalStateException("Cannot pass composable after setting view")
        }
        expandedCompose = ComposeView(context).apply {
            setContent { content() }
        }

        return this
    }

    fun style(@StyleRes style: Int?): ExpandedBubbleBuilder {
        expandedBubbleStyle = style
        return this
    }

    /**
     * @param dimAmount background opacity below the expandable-view
     * */
    fun dimAmount(dimAmount: Float): ExpandedBubbleBuilder {
        this.dimAmount = dimAmount
        return this
    }

}