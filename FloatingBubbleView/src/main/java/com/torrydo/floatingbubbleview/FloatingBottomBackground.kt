package com.torrydo.floatingbubbleview

import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import com.torrydo.floatingbubbleview.databinding.BottomBackgroundBinding

internal class FloatingBottomBackground(
    private val builder: FloatingBubble.Builder
) : BaseFloatingViewBinding<BottomBackgroundBinding>(
    context = builder.context,
    initializer = BottomBackgroundBinding.inflate(LayoutInflater.from(builder.context))
) {

    internal var isShowing = false


    init {
        setupLayoutParams()
        setupBottomBackgroundProperties()
    }

    private fun setupBottomBackgroundProperties() {

    }

    override fun show() {
        if (isShowing) return
        isShowing = true
        super.show()
    }

    override fun remove() {
        isShowing = false
        super.remove()
    }

    public override fun destroy() {
        super.destroy()
    }

    override fun setupLayoutParams() {
        super.setupLayoutParams()

        logIfError {
            params.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = ScreenInfo.heightPx/5
                gravity = Gravity.BOTTOM or Gravity.CENTER
                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION

//                windowAnimations = R.style.default_close_bubble_style

            }
        }

    }

}