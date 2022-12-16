package com.torrydo.floatingbubbleview

import android.view.LayoutInflater
import android.view.WindowManager
import com.torrydo.floatingbubbleview.databinding.CloseBubbleBinding
import kotlin.math.abs

internal class FloatingCloseBubbleIcon(
    private val builder: FloatingBubble.Builder,
) : BaseFloatingViewBinding<CloseBubbleBinding>(
    context = builder.context,
    initializer = CloseBubbleBinding.inflate(LayoutInflater.from(builder.context)),
), Logger by LoggerImpl() {

    companion object {
        internal var widthPx = 0
        internal var heightPx = 0

        private const val DEFAULT_PADDING_BOTTOM_PX = 50
        private const val LIMIT_FLY_HEIGHT = 100

    }


    private val halfScreenWidth by lazy { ScreenInfo.widthPx / 2 }
    private val halfScreenHeight by lazy { ScreenInfo.heightPx / 2 }

    private val halfWidthPx by lazy { widthPx / 2 }
    private val halfHeightPx by lazy { heightPx / 2 }

    private val baseX = 0
    private val baseY by lazy { halfScreenHeight - ScreenInfo.softNavBarHeightPx * 2 - DEFAULT_PADDING_BOTTOM_PX }


    init {
        setupLayoutParams()
        setupCloseBubbleProperties()
    }

    override fun setupLayoutParams() {
        super.setupLayoutParams()

        logIfError {
            params.apply {
//                width = WindowManager.LayoutParams.MATCH_PARENT
//                gravity = Gravity.BOTTOM or Gravity.CENTER
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

            if (builder.closeBubbleSizePx.notZero()) {
                layoutParams.width = widthPx
                layoutParams.height = heightPx
            }

            elevation = builder.elevation.toFloat()

            alpha = builder.alphaF

        }

        params.apply {
            this.x = 0
            this.y = baseY
        }
    }

    private val limit_catch = 100
    fun animateCloseIconByBubble(x: Int, y: Int) {

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
        d("distanceRatio = ${distanceRatio}")
        if (distanceRatio == 0.0f) {
            stickToBubble(x,y)
        } else {
            val isUpperHalf = y < 0
            params.x = ((x * distanceRatio) / 4).toInt()
            params.y = baseY - if (isUpperHalf) {
                ((halfScreenHeight + abs(y)) * distanceRatio) / 5
            } else {
                ((halfScreenHeight - y) * distanceRatio) / 5
            }.toInt().let {
                if(it > LIMIT_FLY_HEIGHT){
                    return@let LIMIT_FLY_HEIGHT
                }else{
                    return@let it
                }
            }
            update()
        }
    }

    private fun stickToBubble(x: Int, y: Int) {
        params.x = x
        params.y = y - FloatingBubbleIcon.heightPx/2
        update()
    }

}