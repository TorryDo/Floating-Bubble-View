package com.torrydo.floatingbubbleview

import android.view.LayoutInflater
import android.view.WindowManager
import com.torrydo.floatingbubbleview.databinding.CloseBubbleBinding

internal class FloatingCloseBubbleIcon(
    private val builder: FloatingBubble.Builder,
) : BaseFloatingViewBinding<CloseBubbleBinding>(
    context = builder.context,
    initializer = CloseBubbleBinding.inflate(LayoutInflater.from(builder.context)),
), Logger by LoggerImpl() {

    companion object {
        internal var widthPx = 0
        internal var heightPx = 0

        private const val DEFAULT_PADDING_BOTTOM_PX = 30
    }


    private val LIMIT_FLY_HEIGHT by lazy { ScreenInfo.heightPx / 15 }


    private val halfScreenWidth by lazy { ScreenInfo.widthPx / 2 }
    private val halfScreenHeight by lazy { ScreenInfo.heightPx / 2 }

    private val halfWidthPx by lazy { widthPx / 2 }
    private val halfHeightPx by lazy { heightPx / 2 }

    private val baseX = halfScreenWidth - halfWidthPx
    private val baseY by lazy {
        ScreenInfo.heightPx -
                heightPx -
                ScreenInfo.softNavBarHeightPx -
                ScreenInfo.statusBarHeightPx -
                DEFAULT_PADDING_BOTTOM_PX
    }

    init {
        setupLayoutParams()
        setupCloseBubbleProperties()
    }

    override fun setupLayoutParams() {
        super.setupLayoutParams()

        logIfError {
            params.apply {
                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION

//                builder.closeBubbleStyle?.let {
//                    windowAnimations = R.style.default_bubble_style
//                }
            }
        }

    }

    // private -------------------------------------------------------------------------------------


    private fun setupCloseBubbleProperties() {
        val icBitmap = builder.closeIconBitmap ?: R.drawable.ic_close_icon.toBitmap(
            builder.context
        )

        binding.closeBubbleImg.apply {
            setImageBitmap(icBitmap)

            layoutParams.width = widthPx
            layoutParams.height = heightPx

            elevation = builder.elevation.toFloat()

            alpha = builder.alphaF

        }

        params.apply {
            this.x = baseX
            this.y = baseY
        }
    }

    /**
     * @return x=0.0 means inside close area, 0.0 < x < 1.0 means outside
     * */
    fun distanceRatioToCloseBubble(x: Int, y: Int): Float {
        val distanceToBubble = MathHelper.distance(
            x1 = baseX.toDouble(),
            y1 = baseY.toDouble(),
            x2 = x.toDouble(),
            y2 = y.toDouble()
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

            params.x = if (isXOnTheLeft) {
                baseX - ((halfScreenWidth - x) * distanceRatio) / 5
            } else {
                baseX + ((x - halfScreenWidth) * distanceRatio) / 5
            }.toInt()

            params.y = baseY - (((ScreenInfo.heightPx - y) * distanceRatio) / 10)
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

        val middleBubbleX = x + FloatingBubbleIcon.widthPx / 2
        val middleBubbleY = y + FloatingBubbleIcon.heightPx / 2

        params.x = middleBubbleX - halfWidthPx
        params.y = middleBubbleY - halfHeightPx

        update()
    }

}