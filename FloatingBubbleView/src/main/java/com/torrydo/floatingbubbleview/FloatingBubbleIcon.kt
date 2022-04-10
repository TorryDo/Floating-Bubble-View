package com.torrydo.floatingbubbleview

import android.annotation.SuppressLint
import android.graphics.Point
import android.graphics.PointF
import android.util.Size
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import com.torrydo.floatingbubbleview.databinding.IconMainBinding

internal class FloatingBubbleIcon(
    private val bubbleBuilder: FloatingBubble.Builder,
    private val screenSize: Size
) : BaseFloatingView(bubbleBuilder.context!!) {

    companion object {

        val MARGIN_PX_FROM_TOP = 100
        val MARGIN_PX_FROM_BOTTOM = 150

    }


    var _binding: IconMainBinding? = null
    val binding get() = _binding!!


    private val prevPoint = Point(0, 0)
    private val pointF = PointF(0f, 0f)
    private val newPoint = Point(0, 0)

    private val screenHalfWidth = screenSize.width / 2
    private val screenHalfHeight = screenSize.height / 2

    init {

        _binding = IconMainBinding.inflate(LayoutInflater.from(bubbleBuilder.context))

        setupLayoutParams()
        setupIconProperties()
        customTouch()

    }

    /**
     * must be root view
     * */
    fun show() {
        super.show(binding.root)
    }

    /**
     * must be root view
     * */
    fun remove() {
        super.remove(binding.root)
    }

    fun destroy() {
        _binding = null
    }


    private val myAnimationHelper = AnimHelper()
    private var isAnimating = false
    fun animateIconToEdge(
        offsetPx: Int,        //    68
        onFinished: () -> Unit
    ) {
        if (!isAnimating) {
            isAnimating = true

            val currentIconX = binding.root.getXYPointOnScreen().x

            if (currentIconX < screenHalfWidth - offsetPx) {    // animate icon to the LEFT side

                val realX = screenHalfWidth - currentIconX  // 235
                val leftEdgeX = screenHalfWidth - offsetPx  // 540 - 68 = 472

                myAnimationHelper.startSpringX(
                    realX.toFloat(),
                    leftEdgeX.toFloat(),
                    object : AnimHelper.Event {
                        override fun onUpdate(float: Float) {

                            logIfError {
                                windowParams!!.x = -(float.toInt())
                                windowManager?.updateViewLayout(binding.root, windowParams)
                            }

                        }

                        override fun onFinish() {
                            isAnimating = false
                            onFinished()
                        }
                    }
                )

            } else {                                            // animate icon to the RIGHT side

                val realX = currentIconX - screenHalfWidth + offsetPx  // 235
                val rightEdgeX = screenHalfWidth - offsetPx            // 540 - 68 = 472

                myAnimationHelper.startSpringX(
                    realX.toFloat(),
                    rightEdgeX.toFloat(),
                    object : AnimHelper.Event {
                        override fun onUpdate(float: Float) {

                            logIfError {
                                windowParams!!.x = float.toInt()
                                windowManager?.updateViewLayout(binding.root, windowParams)
                            }

                        }

                        override fun onFinish() {
                            isAnimating = false
                            onFinished()
                        }
                    }
                )
            }
        }
    }

    // private func --------------------------------------------------------------------------------

    private fun setupIconProperties() {

        val icBitmap = bubbleBuilder.iconBitmap ?: R.drawable.ic_rounded_blue_diamond.toBitmap(
            bubbleBuilder.context!!
        )

        binding.homeLauncherMainIcon.apply {
            setImageBitmap(icBitmap)
            layoutParams.width = bubbleBuilder.bubleSizePx
            layoutParams.height = bubbleBuilder.bubleSizePx

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
        var isBubbleClickable = false

        fun onActionDown(motionEvent: MotionEvent) {
            prevPoint.x = windowParams!!.x
            prevPoint.y = windowParams!!.y

            pointF.x = motionEvent.rawX
            pointF.y = motionEvent.rawY

            bubbleBuilder.listener?.onDown(prevPoint.x, prevPoint.y)

            isBubbleClickable = true
        }

        fun onActionMove(motionEvent: MotionEvent) {
            val mIconDeltaX = motionEvent.rawX - pointF.x
            val mIconDeltaY = motionEvent.rawY - pointF.y

            // prev code here onmove

            newPoint.x =
                prevPoint.x + mIconDeltaX.toInt()  // -540 .. 540                                   (examples, those numbers are not important)
            newPoint.y = prevPoint.y + mIconDeltaY.toInt()  // -1xxx .. 1xxx

            windowParams!!.x = newPoint.x
            windowParams!!.y = newPoint.y
            update(binding.root)

            bubbleBuilder.listener?.onMove(newPoint.x, newPoint.y)
            if (isBubbleClickable) isBubbleClickable = false
        }

        fun onActionUp() {
            // k cho tọa độ Y của view ra ngoài màn hình khiến user khó vuốt
            if (newPoint.y > screenHalfHeight - MARGIN_PX_FROM_BOTTOM) {
                newPoint.y = screenHalfHeight - MARGIN_PX_FROM_BOTTOM
            } else if (newPoint.y < -screenHalfHeight + MARGIN_PX_FROM_TOP) {
                newPoint.y = -screenHalfHeight + MARGIN_PX_FROM_TOP
            }
            windowParams!!.y = newPoint.y
            update(binding.root)

            bubbleBuilder.listener?.onUp(newPoint.x, newPoint.y)

            if (isBubbleClickable) {
                bubbleBuilder.listener?.onClick()
                d("onClick")
            }

//                        animateIconToEdge(68) {}
        }


        binding.homeLauncherMainIcon.also { imgView ->

            imgView.afterMeasured {
                bubbleBuilder.context?.let { nonNullContext ->
                    imgView.updateGestureExclusion(nonNullContext)
                }
            }
        }

        binding.homeLauncherMainIcon.also { imageView ->

            imageView.setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {

                        onActionDown(motionEvent)

                        return@setOnTouchListener true
                    }
                    MotionEvent.ACTION_MOVE -> {

                        onActionMove(motionEvent)

                        return@setOnTouchListener true
                    }
                    MotionEvent.ACTION_UP -> {

                        onActionUp()

                        return@setOnTouchListener true
                    }

                    else -> return@setOnTouchListener false
                }
            }
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
//            windowAnimations = R.style.IconStyle
            }

        }


    }
}