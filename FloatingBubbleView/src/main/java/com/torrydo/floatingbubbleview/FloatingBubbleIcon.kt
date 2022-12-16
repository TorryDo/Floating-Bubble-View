package com.torrydo.floatingbubbleview

import android.annotation.SuppressLint
import android.graphics.Point
import android.graphics.PointF
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import com.torrydo.floatingbubbleview.databinding.BubbleBinding

internal class FloatingBubbleIcon(
    private val builder: FloatingBubble.Builder,
) : BaseFloatingViewBinding<BubbleBinding>(
    context = builder.context,
    initializer = BubbleBinding.inflate(LayoutInflater.from(builder.context))
),
    Logger by LoggerImpl() {

    companion object {
        internal var widthPx = 0
        internal var heightPx = 0

        private val SAFE_TOP_AREA_PX get() = ScreenInfo.statusBarHeightPx
        private val SAFE_BOTTOM_AREA_PX get() = ScreenInfo.softNavBarHeightPx
    }

    private val prevPoint = Point(0, 0)
    private val pointF = PointF(0f, 0f)
    private val newPoint = Point(0, 0)

    private val halfScreenWidth = ScreenInfo.widthPx / 2
    private val halfScreenHeight = ScreenInfo.heightPx / 2

    private val halfIconWidthPx by lazy { widthPx / 2 }

    init {
        setupLayoutParams()
        setupBubbleProperties()
        customTouch()
    }


    private val animHelper = AnimHelper()
    private var isAnimatingToEdge = false
    fun animateIconToEdge(onFinished: (() -> Unit)? = null) {
        if (isAnimatingToEdge) return

        isAnimatingToEdge = true
        d("---------------------------------------------------------------------------------------")
        val iconX = binding.root.getXYPointOnScreen().x // 0..X

        d("iconX = $iconX | halfIconWidth = $halfIconWidthPx")

        val isOnTheLeftSide = iconX + halfIconWidthPx < halfScreenWidth
        val startX: Int
        val endX: Int
        if (isOnTheLeftSide) {
            startX = iconX
            endX = 0
        } else {
            startX = iconX
            endX = ScreenInfo.widthPx - widthPx
        }
        animHelper.startSpringX(
            startValue = startX.toFloat(),
            finalPosition = endX.toFloat(),
            animationListener = object : AnimHelper.Event {
                override fun onUpdate(float: Float) {
                    tryOnly {
                        params.x = float.toInt()
                        update()
                    }
                }

                override fun onEnd() {
                    isAnimatingToEdge = false
                    onFinished?.invoke()
                }
            }
        )
    }

    // private func --------------------------------------------------------------------------------

    private fun setupBubbleProperties() {

        val iconBitmap = builder.iconBitmap ?: R.drawable.ic_rounded_blue_diamond.toBitmap(
            builder.context
        )

        binding.bubbleView.apply {
            setImageBitmap(iconBitmap)
            layoutParams.width = widthPx
            layoutParams.height = heightPx

            elevation = builder.elevation.toFloat()

            alpha = builder.alphaF
        }

        params.apply {
            x = builder.startingPoint.x
            y = builder.startingPoint.y
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun customTouch() {

        // actions ---------------------------------------------------------------------------------

        fun onActionDown(motionEvent: MotionEvent) {
            prevPoint.x = params.x
            prevPoint.y = params.y

            pointF.x = motionEvent.rawX
            pointF.y = motionEvent.rawY

            builder.listener?.onDown(prevPoint.x, prevPoint.y)
        }

        fun onActionMove(motionEvent: MotionEvent) {
            val mIconDeltaX = motionEvent.rawX - pointF.x
            val mIconDeltaY = motionEvent.rawY - pointF.y

            newPoint.x = prevPoint.x + mIconDeltaX.toInt()
            newPoint.y = prevPoint.y + mIconDeltaY.toInt()

            // prevent bubble's Y coordinate moving outside the screen
            val safeTopY = 0
            val safeBottomY =
                ScreenInfo.heightPx - ScreenInfo.softNavBarHeightPx - ScreenInfo.statusBarHeightPx - heightPx
            val isAboveStatusBar = newPoint.y < safeTopY
            val isUnderSoftNavBar = newPoint.y > safeBottomY
            if (isAboveStatusBar) {
                newPoint.y = safeTopY
            } else if (isUnderSoftNavBar) {
                newPoint.y = safeBottomY
            }

            params.x = newPoint.x
            params.y = newPoint.y
            update()

            builder.listener?.onMove(newPoint.x, newPoint.y)
        }

        fun onActionUp() {
            builder.listener?.onUp(newPoint.x, newPoint.y)
        }

        // listen actions --------------------------------------------------------------------------

        val gestureDetector = GestureDetector(builder.context, SingleTapConfirm())

        binding.bubbleView.apply {

            afterMeasured { updateGestureExclusion(builder.context) }

            setOnTouchListener { _, motionEvent ->

                // detect onTouch event first. If event is consumed, return@setOnTouch...
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    builder.listener?.onClick()
                    return@setOnTouchListener true
                }

                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> onActionDown(motionEvent)
                    MotionEvent.ACTION_MOVE -> onActionMove(motionEvent)
                    MotionEvent.ACTION_UP -> onActionUp()
                }

                return@setOnTouchListener true
            }
        }
    }

    private class SingleTapConfirm : SimpleOnGestureListener() {
        override fun onSingleTapUp(event: MotionEvent): Boolean {
            return true
        }
    }

    // override

    override fun setupLayoutParams() {
        super.setupLayoutParams()

        logIfError {

            params.apply {
//                gravity = Gravity.BOTTOM
                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

//                builder.bubbleStyle?.let {
//                    windowAnimations = it
//                }

            }

        }


    }
}