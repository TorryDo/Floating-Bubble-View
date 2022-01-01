package com.torrydo.floatingbubbleview.main.bubble

import android.annotation.SuppressLint
import android.graphics.Point
import android.graphics.PointF
import android.util.Size
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import com.torrydo.floatingbubbleview.R
import com.torrydo.floatingbubbleview.databinding.IconMainBinding
import com.torrydo.floatingbubbleview.main.FloatingBubbleBuilder
import com.torrydo.floatingbubbleview.main.base.BaseFloatingView
import com.torrydo.floatingbubbleview.physics.anim.AnimState
import com.torrydo.floatingbubbleview.physics.anim.MyAnimationHelper
import com.torrydo.floatingbubbleview.utils.getXYPointOnScreen
import com.torrydo.floatingbubbleview.utils.toBitmap

class FloatingBubbleIcon(
    private val bubbleBuilder: FloatingBubbleBuilder,
    private val screenSize: Size
) : BaseFloatingView(bubbleBuilder.context!!) {

    private val TAG = javaClass.simpleName

    var binding = IconMainBinding.inflate(LayoutInflater.from(bubbleBuilder.context))


    private val prevPoint = Point(0, 0)
    private val pointF = PointF(0f, 0f)
    private val newPoint = Point(0, 0)

    private val screenHalfWidth = screenSize.width / 2
    private val screenHalfHeight = screenSize.height / 2

    init {
        setupDefaultLayoutParams()
        setupIconProperties()
        customTouch()
    }

    // must be root view
    fun show() {
        super.show(binding.root)
    }

    fun remove() {
        super.remove(binding.root)
    }

    private val myAnimationHelper = MyAnimationHelper()
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
                    object : AnimState {
                        override fun onUpdate(float: Float) {
                            try {
                                windowParams!!.x = -(float.toInt())
                                windowManager?.updateViewLayout(binding.root, windowParams)
                            } catch (e: Exception) {
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
                    object : AnimState {
                        override fun onUpdate(float: Float) {
                            try {
                                windowParams!!.x = float.toInt()
                                windowManager?.updateViewLayout(binding.root, windowParams)
                            } catch (e: Exception) {
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

    override fun setupDefaultLayoutParams() {
        super.setupDefaultLayoutParams()
        windowParams?.apply {

            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//            windowAnimations = R.style.IconStyle
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

        binding.homeLauncherMainIcon.let { imageView ->
            imageView.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {

                        prevPoint.x = windowParams!!.x
                        prevPoint.y = windowParams!!.y

                        pointF.x = motionEvent.rawX
                        pointF.y = motionEvent.rawY

                        bubbleBuilder.listener?.onDown(prevPoint.x, prevPoint.y)

                        return@setOnTouchListener true
                    }
                    MotionEvent.ACTION_MOVE -> {

                        val mIconDeltaX = motionEvent.rawX - pointF.x
                        val mIconDeltaY = motionEvent.rawY - pointF.y

                        // prev code here onmove

                        newPoint.x = prevPoint.x + mIconDeltaX.toInt()  // -540 .. 540
                        newPoint.y = prevPoint.y + mIconDeltaY.toInt()  // -1xxx .. 1xxx

                        windowParams!!.x = newPoint.x
                        windowParams!!.y = newPoint.y
                        update(binding.root)

                        bubbleBuilder.listener?.onMove(newPoint.x, newPoint.y)

                        return@setOnTouchListener true
                    }
                    MotionEvent.ACTION_UP -> {

                        // k cho tọa độ Y của view ra ngoài màn hình khiến user khó vuốt
                        if (newPoint.y > screenHalfHeight - 150) {
                            newPoint.y = screenHalfHeight - 150
                        } else if (newPoint.y < -screenHalfHeight + 100) {
                            newPoint.y = -screenHalfHeight + 100
                        }
                        windowParams!!.y = newPoint.y
                        update(binding.root)

                        bubbleBuilder.listener?.onUp(newPoint.x, newPoint.y)

//                        animateIconToEdge(68) {}

                        return@setOnTouchListener true
                    }

                    else -> return@setOnTouchListener false
                }
            }
        }
    }
}