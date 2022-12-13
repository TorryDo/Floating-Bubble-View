package com.torrydo.floatingbubbleview

import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import com.torrydo.floatingbubbleview.databinding.CloseIconBinding

internal class FloatingCloseBubbleIcon(
    private val bubbleBuilder: FloatingBubble.Builder,
) : BaseFloatingView(bubbleBuilder.context), Logger by LoggerImpl() {

    private var _binding: CloseIconBinding? = null
    val binding get() = _binding!!

    companion object {
        internal var widthPx = 0
        internal var heightPx = 0

        const val DEFAULT_LARGER_PX = 20
    }

    init {
        _binding = CloseIconBinding.inflate(LayoutInflater.from(bubbleBuilder.context))

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

//                windowAnimations = R.style.default_close_icon_style
            }
        }


    }

    // public --------------------------------------------------------------------------------------

    fun show() = logIfError {
        d("show close-bubble")
        super.show(binding.root)
    }

    fun remove() = logIfError {
        d("remove close-bubble")
        super.remove(binding.root)
    }

    override fun destroy() {
        _binding = null
        super.destroy()
    }

    // private -------------------------------------------------------------------------------------

    private fun setupRemoveBubbleProperties() {
        val icBitmap = bubbleBuilder.iconRemoveBitmap ?: R.drawable.ic_close_icon.toBitmap(
            bubbleBuilder.context
        )
        binding.homeLauncherMainBinIcon.apply {
            setImageBitmap(icBitmap)

            if (bubbleBuilder.bubbleSizePx.notZero()) {
                layoutParams.width = widthPx
                layoutParams.height = heightPx
            }

            elevation = bubbleBuilder.elevation.toFloat()

            alpha = bubbleBuilder.alphaF
        }
    }

}