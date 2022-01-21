package com.torrydo.floatingbubbleview

import android.util.Size
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import com.torrydo.floatingbubbleview.databinding.RemoveIconBinding

internal class FloatingRemoveBubbleIcon(
    private val bubbleBuilder: FloatingBubbleBuilder,
    private val screenSize: Size
) : BaseFloatingView(bubbleBuilder.context!!) {

    val binding = RemoveIconBinding.inflate(LayoutInflater.from(bubbleBuilder.context))

    init {
        setupDefaultLayoutParams()
        setupRemoveBubbleProperties()
    }


    override fun setupDefaultLayoutParams() {
        super.setupDefaultLayoutParams()

        windowParams?.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            gravity = Gravity.BOTTOM or Gravity.CENTER
            flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//            windowAnimations = R.style.IconStyle
        }


    }

    // public --------------------------------------------------------------------------------------

    fun show() {
        super.show(binding.root)
    }

    fun remove() {
        super.remove(binding.root)
    }

    // private -------------------------------------------------------------------------------------

    private fun setupRemoveBubbleProperties() {
        val icBitmap = bubbleBuilder.iconRemoveBitmap ?: R.drawable.ic_remove_icon.toBitmap(
            bubbleBuilder.context!!
        )
        binding.homeLauncherMainBinIcon.apply {
            setImageBitmap(icBitmap)
            layoutParams.width = bubbleBuilder.bubleSizePx
            layoutParams.height = bubbleBuilder.bubleSizePx

            elevation = bubbleBuilder.elevation.toFloat()

            alpha = bubbleBuilder.alphaF
        }
    }

}