package com.torrydo.floatingbubbleview

import android.annotation.SuppressLint
import android.graphics.Point
import android.graphics.PointF
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import com.torrydo.floatingbubbleview.databinding.IconMainBinding


internal class FloatingBubbleIcon(
    private val bubbleBuilder: FloatingBubble.Builder,
) : BaseFloatingView(bubbleBuilder.context), Logger by LoggerImpl() {

    companion object {
        internal var widthPx = 0
        internal var heightPx = 0
    }

    private val MARGIN_PX_FROM_TOP = 100
    private val MARGIN_PX_FROM_BOTTOM = 150


    private var _binding: IconMainBinding? = null
    val binding get() = _binding!!


    private val prevPoint = Point(0, 0)
    private val pointF = PointF(0f, 0f)
    private val newPoint = Point(0, 0)

    private val halfScreenWidth = ScreenInfo.screenSize.width / 2
    private val halfScreenHeight = ScreenInfo.screenSize.height / 2

    init {
        _binding = IconMainBinding.inflate(LayoutInflater.from(bubbleBuilder.context))
        setupLayoutParams()
        setupIconProperties()
        customTouch()
    }

    /**
     * must be root view
     * */
    fun show() = logIfError {
        super.show(binding.root)
    }

    /**
     * must be root view
     * */
    fun remove() = logIfError {
        super.remove(binding.root)
    }


    override fun destroy() {
        _binding = null
        super.destroy()
    }


    private val animHelper = AnimHelper()
    private var isAnimatingToEdge = false
    fun animateIconToEdge(onFinished: (() -> Unit)? = null) {
        if (isAnimatingToEdge) return

        isAnimatingToEdge = true
        d("---------------------------------------------------------------------------------------")
        val iconX = binding.root.getXYPointOnScreen().x // 0..X
        val halfIconWidthPx = if (widthPx == 0) 0 else widthPx / 2

        d("iconX = $iconX | halfIconWidth = $halfIconWidthPx | screenHalfWidth = $halfScreenWidth")

        // animate icon to the LEFT side
        if (iconX + halfIconWidthPx < halfScreenWidth) {

            val startX = halfScreenWidth - iconX - halfIconWidthPx
            val endX = halfScreenWidth - halfIconWidthPx

            d("startX = $startX | endX = $endX ")

            animHelper.startSpringX(
                startValue = startX.toFloat(),
                finalPosition = endX.toFloat(),
                animationListener = object : AnimHelper.Event {
                    override fun onUpdate(float: Float) {
                        tryOnly {
                            windowParams!!.x = -(float.toInt())
                            windowManager?.updateViewLayout(binding.root, windowParams)
                        }
                    }

                    override fun onFinish() {
                        isAnimatingToEdge = false
                        onFinished?.invoke()
                    }
                }
            )

            // animate icon to the RIGHT side
        } else {
            val startX = iconX - halfScreenWidth + halfIconWidthPx
            val endX = halfScreenWidth - halfIconWidthPx

            d("startX = $startX | endX = $endX ")

            animHelper.startSpringX(
                startValue = startX.toFloat(),
                finalPosition = endX.toFloat(),
                animationListener = object : AnimHelper.Event {
                    override fun onUpdate(float: Float) {
                        tryOnly {
                            windowParams!!.x = float.toInt()
                            windowManager?.updateViewLayout(binding.root, windowParams)
                        }
                    }

                    override fun onFinish() {
                        isAnimatingToEdge = false
                        onFinished?.invoke()
                    }
                }
            )

        }
    }

    // private func --------------------------------------------------------------------------------

    private fun setupIconProperties() {

        val iconBitmap = bubbleBuilder.iconBitmap ?: R.drawable.ic_rounded_blue_diamond.toBitmap(
            bubbleBuilder.context
        )

        binding.homeLauncherMainIcon.apply {
            setImageBitmap(iconBitmap)
            layoutParams.width = widthPx
            layoutParams.height = heightPx

            elevation = bubbleBuilder.elevation.toFloat()

            alpha = bubbleBuilder.alphaF
        }

        windowParams?.apply {
            x = bubbleBuilder.startingPoint.x
            y = bubbleBuilder.startingPoint.y
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun customTouch() {

        // actions ---------------------------------------------------------------------------------

        fun onActionDown(motionEvent: MotionEvent) {
            prevPoint.x = windowParams!!.x
            prevPoint.y = windowParams!!.y

            pointF.x = motionEvent.rawX
            pointF.y = motionEvent.rawY

            bubbleBuilder.listener?.onDown(prevPoint.x, prevPoint.y)
        }

        fun onActionMove(motionEvent: MotionEvent) {
            val mIconDeltaX = motionEvent.rawX - pointF.x
            val mIconDeltaY = motionEvent.rawY - pointF.y

            newPoint.x = prevPoint.x + mIconDeltaX.toInt()  // eg: -X .. X  |> (-540 .. 540)
            newPoint.y = prevPoint.y + mIconDeltaY.toInt()  // eg: -Y .. Y  |> (-1xxx .. 1xxx)

            windowParams!!.x = newPoint.x
            windowParams!!.y = newPoint.y
            update(binding.root)

            bubbleBuilder.listener?.onMove(newPoint.x, newPoint.y)
        }

        fun onActionUp() {
            // prevent bubble's Y coordinate move outside the screen
            if (newPoint.y > halfScreenHeight - MARGIN_PX_FROM_BOTTOM) {
                newPoint.y = halfScreenHeight - MARGIN_PX_FROM_BOTTOM
            } else if (newPoint.y < -halfScreenHeight + MARGIN_PX_FROM_TOP) {
                newPoint.y = -halfScreenHeight + MARGIN_PX_FROM_TOP
            }
            windowParams!!.y = newPoint.y
            update(binding.root)

            bubbleBuilder.listener?.onUp(newPoint.x, newPoint.y)
        }

        // listen actions --------------------------------------------------------------------------

        val gestureDetector = GestureDetector(bubbleBuilder.context, SingleTapConfirm())

        binding.homeLauncherMainIcon.apply {

            afterMeasured { updateGestureExclusion(bubbleBuilder.context) }

            setOnTouchListener { _, motionEvent ->

                // detect onTouch event first. If event is consumed, return@setOnTouch...
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    bubbleBuilder.listener?.onClick()
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

            windowParams!!.apply {

                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            }

        }


    }
}