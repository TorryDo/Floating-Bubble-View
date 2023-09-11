package com.torrydo.floatingbubbleview.bubble

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.torrydo.floatingbubbleview.AndroidVersions
import com.torrydo.floatingbubbleview.XMath
import com.torrydo.floatingbubbleview.afterMeasured
import com.torrydo.floatingbubbleview.getXYOnScreen
import com.torrydo.floatingbubbleview.sez

class FloatingCloseBubble(
    context: Context,
    root: View,
    private val distanceToClosePx: Int,
    private val bottomPaddingPx: Int
) : Bubble(context, root) {

    private var LIMIT_FLY_HEIGHT: Int = 0

    private var halfWidthPx: Int = 0
    private var halfHeightPx: Int = 0

    /**
     * able to interact with bubble, because when the close bubble initialized.
     *
     * the close-bubble appears on the top left and I can't get it's size, therefore this attribute shows
     * that is the bubble ready to interact (attract, follow, close) bubble or not
     * */
    var ableToInteract = false

    var width = 0
    var height = 0

    private var halfSafeScreenWidth: Int = 0
    private var baseX: Int = 0
    private var baseY: Int = 0

    private var centerCloseBubbleX: Int = 0
    private var centerCloseBubbleY: Int = 0

    init {
        setupLayoutParams()

        root.visibility = View.INVISIBLE

        root.afterMeasured {

            width = root.width
            height = root.height

            LIMIT_FLY_HEIGHT = sez.fullHeight / 10

            halfSafeScreenWidth = sez.safeWidth / 2
            halfWidthPx = width / 2
            halfHeightPx = height / 2
            baseX = halfSafeScreenWidth - halfWidthPx
            baseY = sez.safeHeight - height - bottomPaddingPx

            centerCloseBubbleX = halfSafeScreenWidth
            centerCloseBubbleY = baseY + halfHeightPx

            layoutParams.apply {
                this.x = baseX
                this.y = baseY
            }

            update()
            ableToInteract = true
            root.visibility = View.VISIBLE

        }

    }

    private fun setupLayoutParams() {

        layoutParams.apply {
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH

            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT

            gravity = Gravity.TOP or Gravity.LEFT
            format = PixelFormat.TRANSLUCENT

            type = if (Build.VERSION.SDK_INT >= AndroidVersions.`8`) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
        }
    }

    //region Public methods ------------------------------------------------------------------------

    private var isBubbleAnimated = false

    /**
     * x, y: point on screen
     * - TRUE: if the bubble is attracted to the center of the close-bubble
     * - FALSE: if the bubble is too far from the close-bubble
     * */
    fun tryAttractBubble(floatingBubble: FloatingBubble, x: Float, y: Float): Boolean {

        if (isFingerInsideClosableArea(x, y) && ableToInteract) {
            if (isBubbleAnimated.not()) {

                val bWidth = floatingBubble.root.width
                val bHeight = floatingBubble.root.height

                val xOffset = (width - bWidth) / 2
                val yOffset = (height - bHeight) / 2

                val xUpdated = (baseX + xOffset).toFloat()
                val yUpdated = (baseY + yOffset).toFloat()

                floatingBubble.animateTo(xUpdated, yUpdated)
                floatingBubble.setLocation(xUpdated, yUpdated)

                isBubbleAnimated = true
            }

            return true
        } else {
            isBubbleAnimated = false


            return false
        }
    }

    /**
     * pass bubble location
     * */
    fun isBubbleInsideClosableArea(bubble: Bubble) =
        distanceRatioFromBubbleToClosableArea(bubble) == 0.0f


    fun isFingerInsideClosableArea(x: Float, y: Float): Boolean {
        // because x and y of the finger which we got from MotionEvent included the cutout and the nav-bar, so we must exclude them
        val mX = x - sez.safePaddingLeft
        val mY = y - sez.safePaddingTop
        return distanceRatioFromLocationToClosableArea(
            x = mX,
            y = mY
        ) == 0.0f
    }

    /**
     * @param x is the top left x axis of the bubble
     * @param y is the top left y axis of the bubble
     * @return x=0.0 means the bubble is inside the closable area, 0.0 < x < 1.0 means outside
     * */
    private fun distanceRatioFromBubbleToClosableArea(bubble: Bubble): Float {

        val bWidth = bubble.root.width
        val bHeight = bubble.root.height
        val (x, y) = bubble.root.getXYOnScreen()

        val centerBubbleX = x + bWidth / 2
        val centerBubbleY = y + bHeight / 2

        val distanceToBubble = XMath.distance(
            x1 = centerCloseBubbleX.toDouble(),
            y1 = centerCloseBubbleY.toDouble(),
            x2 = centerBubbleX.toDouble(),
            y2 = centerBubbleY.toDouble()
        )
        val distanceRatio = (distanceToClosePx.toDouble() / distanceToBubble).let {
            if (it > 1) return@let 0
            return@let 1 - it
        }.toFloat()

        return distanceRatio
    }

    /**
     * Important: the x and y is the location after exclude the nav bar and cutout
     * */
    private fun distanceRatioFromLocationToClosableArea(x: Float, y: Float): Float {
        val distanceToLocation = XMath.distance(
            x1 = centerCloseBubbleX.toDouble(),
            y1 = centerCloseBubbleY.toDouble(),
            x2 = x.toDouble(),
            y2 = y.toDouble()
        )
        val distanceRatio = (distanceToClosePx.toDouble() / distanceToLocation).let {
            if (it > 1) return@let 0
            return@let 1 - it
        }.toFloat()

        return distanceRatio
    }


    /**
     * x and y are point which exclude status bar
     * */
    fun followBubble(x: Int, y: Int, bubble: Bubble) {

        val bWidth = bubble.root.width
        val bHeight = bubble.root.height

        val distanceRatio = distanceRatioFromBubbleToClosableArea(bubble)

        if (distanceRatio == 0.0f) {
            stickToBubble(x, y, bWidth, bHeight)
        } else {

            val centerBubbleX = (x + bWidth / 2)

            val isXOnTheLeft = centerBubbleX < halfSafeScreenWidth

            layoutParams.x = if (isXOnTheLeft) {
                baseX - ((halfSafeScreenWidth - centerBubbleX) * distanceRatio) / 5
            } else {
                baseX + ((centerBubbleX - halfSafeScreenWidth) * distanceRatio) / 5
            }.toInt()

            layoutParams.y = baseY - (((sez.fullHeight - y) * distanceRatio) / 10)
                .toInt().let {
                    return@let if (it > LIMIT_FLY_HEIGHT) {
                        LIMIT_FLY_HEIGHT
                    } else {
                        it
                    }
                }
            update()
        }
    }

    //endregion ------------------------------------------------------------------------------------

    private fun stickToBubble(x: Int, y: Int, bWidth: Int, bHeight: Int) {

        val midBubbleX = x + bWidth / 2
        val midBubbleY = y + bHeight / 2

        layoutParams.x = midBubbleX - halfWidthPx
        layoutParams.y = midBubbleY - halfHeightPx

        update()
    }

}