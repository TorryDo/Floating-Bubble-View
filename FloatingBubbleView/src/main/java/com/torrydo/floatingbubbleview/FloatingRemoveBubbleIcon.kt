package com.torrydo.floatingbubbleview

import android.app.Activity
import android.util.Size
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.torrydo.floatingbubbleview.databinding.RemoveIconBinding

internal class FloatingRemoveBubbleIcon(
    private val bubbleBuilder: FloatingBubble.Builder,
    private val screenSize: Size
) : BaseFloatingView(bubbleBuilder.context!!) {

    private var _binding: RemoveIconBinding? = null
    val binding get() = _binding!!


    init {

        _binding = RemoveIconBinding.inflate(LayoutInflater.from(bubbleBuilder.context))

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

//            windowAnimations = R.style.IconStyle
            }
        }


    }

    // public --------------------------------------------------------------------------------------

    fun show() {
        super.show(binding.root)
    }

    fun remove() {
        super.remove(binding.root)
    }

    fun destroy() {
        _binding = null
    }

    // private -------------------------------------------------------------------------------------

    private fun setupRemoveBubbleProperties() {
        val icBitmap = bubbleBuilder.iconRemoveBitmap ?: R.drawable.ic_remove_icon.toBitmap(
            bubbleBuilder.context!!
        )
        binding.homeLauncherMainBinIcon.apply {
            setImageBitmap(icBitmap)
            layoutParams.width = bubbleBuilder.bubbleSizePx
            layoutParams.height = bubbleBuilder.bubbleSizePx

            elevation = bubbleBuilder.elevation.toFloat()

            alpha = bubbleBuilder.alphaF
        }
    }

}