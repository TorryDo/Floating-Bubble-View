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

    companion object {
        internal const val DEFAULT_PADDING_BOTTOM_PX = 30
    }

    private val LIMIT_FLY_HEIGHT: Int

    private val halfWidthPx: Int
    private val halfHeightPx: Int

    private val halfScreenWidth: Int
    private val baseX: Int
    private val baseY: Int

    private val centerCloseBubbleX: Int
    private val centerCloseBubbleY: Int

    init {

        builder.closeBubbleSizePx.also {
            if (it.notZero()) {
                width = it.width
                height = it.height
            } else {
                width = builder.bubbleSizePx.width
                height = builder.bubbleSizePx.height
            }
        }

        LIMIT_FLY_HEIGHT = ScreenInfo.heightPx / 15

        halfScreenWidth = ScreenInfo.widthPx / 2
        halfWidthPx = width / 2
        halfHeightPx = height / 2
        baseX = halfScreenWidth - halfWidthPx
        baseY = ScreenInfo.heightPx -
                height -
                ScreenInfo.softNavBarHeightPx -
                ScreenInfo.statusBarHeightPx -
                DEFAULT_PADDING_BOTTOM_PX

        centerCloseBubbleX = baseX + halfWidthPx
        centerCloseBubbleY = baseY + halfHeightPx

        setupLayoutParams()
        setupCloseBubbleProperties()
    }


    override fun setupLayoutParams() {
        super.setupLayoutParams()

        windowParams.apply {
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH /*or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION*/
        }
    }

    // private -------------------------------------------------------------------------------------


    private fun setupCloseBubbleProperties() {
        val icBitmap = builder.closeIconBitmap ?: R.drawable.ic_close_bubble.toBitmap(
            builder.context
        )

        binding.closeBubbleImg.apply {
            setImageBitmap(icBitmap)

            layoutParams.width = this@FloatingCloseBubbleView.width
            layoutParams.height = this@FloatingCloseBubbleView.height

            alpha = builder.opacity

        }

        windowParams.apply {
            this.x = baseX
            this.y = baseY
        }
    }

    /**
     * @param x is the top left x axis of the bubble
     * @param y is the top left y axis of the bubble
     * @return x=0.0 means the bubble is inside the closable area, 0.0 < x < 1.0 means outside
     * */
    fun distanceRatioToCloseBubble(x: Int, y: Int): Float {

        val centerBubbleX = x + builder.bubbleSizePx.width / 2
        val centerBubbleY = y + builder.bubbleSizePx.height / 2

        val distanceToBubble = MathHelper.distance(
            x1 = centerCloseBubbleX.toDouble(),
            y1 = centerCloseBubbleY.toDouble(),
            x2 = centerBubbleX.toDouble(),
            y2 = centerBubbleY.toDouble()
        )
        val distanceRatio = (limit_catch.toDouble() / distanceToBubble).let {
            if (it > 1) return@let 0
            return@let 1 - it
        }.toFloat()

        return distanceRatio
    }

    private val limit_catch = LIMIT_FLY_HEIGHT
    fun animateCloseIconByBubble(x: Int, y: Int) {

        val distanceRatio = distanceRatioToCloseBubble(x, y)

        if (distanceRatio == 0.0f) {
            stickToBubble(x, y)
        } else {

            val isXOnTheLeft = x < halfScreenWidth

            windowParams.x = if (isXOnTheLeft) {
                baseX - ((halfScreenWidth - x) * distanceRatio) / 5
            } else {
                baseX + ((x - halfScreenWidth) * distanceRatio) / 5
            }.toInt()

            windowParams.y = baseY - (((ScreenInfo.heightPx - y) * distanceRatio) / 10)
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

    private fun stickToBubble(x: Int, y: Int) {

        val middleBubbleX = x + builder.bubbleSizePx.width / 2
        val middleBubbleY = y + builder.bubbleSizePx.height / 2

        windowParams.x = middleBubbleX - halfWidthPx
        windowParams.y = middleBubbleY - halfHeightPx

        update()
    }

}