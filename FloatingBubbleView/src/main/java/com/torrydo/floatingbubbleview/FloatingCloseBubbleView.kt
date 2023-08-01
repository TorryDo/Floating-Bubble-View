package com.torrydo.floatingbubbleview

import android.view.LayoutInflater
import android.view.WindowManager
import com.torrydo.floatingbubbleview.databinding.CloseBubbleBinding

internal class FloatingCloseBubbleView(
    private val builder: FloatingBubble.Builder,
) : BaseFloatingViewBinding<CloseBubbleBinding>(
    context = builder.context,
    initializer = CloseBubbleBinding.inflate(LayoutInflater.from(builder.context)),
) {

    private var LIMIT_FLY_HEIGHT: Int

    var halfWidthPx: Int
    var halfHeightPx: Int

    private var halfSafeScreenWidth: Int
    var baseX: Int
    var baseY: Int

    private var centerCloseBubbleX: Int
    private var centerCloseBubbleY: Int

    init {
        builder.closeBubbleSizePx.also {
            width = it.width
            height = it.height
        }

        LIMIT_FLY_HEIGHT = sez.fullHeight / 10

        halfSafeScreenWidth = sez.safeWidth / 2
        halfWidthPx = width / 2
        halfHeightPx = height / 2
        baseX = halfSafeScreenWidth - halfWidthPx
        baseY = sez.safeHeight - height - builder.closeBubbleBottomPaddingPx

        centerCloseBubbleX = halfSafeScreenWidth
        centerCloseBubbleY = baseY + halfHeightPx

        setupLayoutParams()
        setupCloseBubbleProperties()
    }

    override fun setupLayoutParams() {
        super.setupLayoutParams()

        windowParams.apply {
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        }
    }

    //region Public methods ------------------------------------------------------------------------

    /**
     * @param x is the top left x axis of the bubble
     * @param y is the top left y axis of the bubble
     * @return x=0.0 means the bubble is inside the closable area, 0.0 < x < 1.0 means outside
     * */
    fun distanceRatioFromBubbleToClosableArea(x: Int, y: Int): Float {

        val bWidth = builder.bubbleView!!.width
        val bHeight = builder.bubbleView!!.height

        val centerBubbleX = x + bWidth / 2
        val centerBubbleY = y + bHeight / 2

        val distanceToBubble = XMath.distance(
            x1 = centerCloseBubbleX.toDouble(),
            y1 = centerCloseBubbleY.toDouble(),
            x2 = centerBubbleX.toDouble(),
            y2 = centerBubbleY.toDouble()
        )
        val distanceRatio = (builder.distanceToClosePx.toDouble() / distanceToBubble).let {
            if (it > 1) return@let 0
            return@let 1 - it
        }.toFloat()

        return distanceRatio
    }

    /**
     * Important: the x and y is the location after exclude the nav bar and cutout
     * */
    fun distanceRatioFromLocationToClosableArea(x: Float, y: Float): Float {

        val distanceToLocation = XMath.distance(
            x1 = centerCloseBubbleX.toDouble(),
            y1 = centerCloseBubbleY.toDouble(),
            x2 = x.toDouble(),
            y2 = y.toDouble()
        )
        val distanceRatio = (builder.distanceToClosePx.toDouble() / distanceToLocation).let {
            if (it > 1) return@let 0
            return@let 1 - it
        }.toFloat()

        return distanceRatio

    }

    fun animateCloseIconByBubble(x: Int, y: Int) {

        val distanceRatio = distanceRatioFromBubbleToClosableArea(x, y)

        if (distanceRatio == 0.0f) {
            stickToBubble(x, y)
        } else {

            val bubbleWidth = builder.bubbleView!!.width
            val centerBubbleX = (x + bubbleWidth / 2)

            val isXOnTheLeft = centerBubbleX < halfSafeScreenWidth

            windowParams.x = if (isXOnTheLeft) {
                baseX - ((halfSafeScreenWidth - centerBubbleX) * distanceRatio) / 5
            } else {
                baseX + ((centerBubbleX - halfSafeScreenWidth) * distanceRatio) / 5
            }.toInt()

            windowParams.y = baseY - (((sez.fullHeight - y) * distanceRatio) / 10)
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

    private fun stickToBubble(x: Int, y: Int) {

        val bubbleSizeCompat = builder.bubbleSize()

        val middleBubbleX = x + bubbleSizeCompat.width / 2
        val middleBubbleY = y + bubbleSizeCompat.height / 2

        windowParams.x = middleBubbleX - halfWidthPx
        windowParams.y = middleBubbleY - halfHeightPx

        update()
    }

    private fun setupCloseBubbleProperties() {
        val icBitmap = builder.closeIconBitmap ?: R.drawable.ic_close_bubble.toBitmap(
            builder.context
        )

        binding.closeBubbleImg.apply {
            setImageBitmap(icBitmap)

            layoutParams.width = this@FloatingCloseBubbleView.width
            layoutParams.height = this@FloatingCloseBubbleView.height
        }

        windowParams.apply {
            this.x = baseX
            this.y = baseY
        }
    }

}