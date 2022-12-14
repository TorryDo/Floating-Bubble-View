package com.torrydo.floatingbubbleview

import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import com.torrydo.floatingbubbleview.databinding.CloseIconBinding

internal class FloatingCloseBubbleIcon(
    private val builder: FloatingBubble.Builder,
) : BaseFloatingViewBinding<CloseIconBinding>(
    context = builder.context,
    initializer = CloseIconBinding.inflate(LayoutInflater.from(builder.context)),
), Logger by LoggerImpl() {

    companion object {
        internal var widthPx = 0
        internal var heightPx = 0

        const val DEFAULT_LARGER_PX = 20
    }

    init {
        setupLayoutParams()
        setupRemoveBubbleProperties()
    }

    override fun setupLayoutParams() {
        super.setupLayoutParams()

        logIfError {
            windowParams!!.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                gravity = Gravity.BOTTOM or Gravity.CENTER
                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION

                builder.closeBubbleStyle?.let {
                    windowAnimations = it
                }
            }
        }

    }

    // private -------------------------------------------------------------------------------------

    private fun setupRemoveBubbleProperties() {
        val icBitmap = builder.closeIconBitmap ?: R.drawable.ic_close_icon.toBitmap(
            builder.context
        )
        binding.closeBubbleLayout.apply {
            setPadding(0, 20, 0, ScreenInfo.softNavBarHeightPx.let {
                if(it < 50) return@let 50
                it
            } + 10)
        }
        binding.closeBubbleImg.apply {
            setImageBitmap(icBitmap)

            if (builder.bubbleSizePx.notZero()) {
                layoutParams.width = widthPx
                layoutParams.height = heightPx
            }

            elevation = builder.elevation.toFloat()

            alpha = builder.alphaF

        }
    }

}