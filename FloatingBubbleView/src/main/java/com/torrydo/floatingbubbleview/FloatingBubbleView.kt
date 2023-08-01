package com.torrydo.floatingbubbleview

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.PointF
import android.view.*
import androidx.compose.ui.platform.ComposeView
import com.torrydo.floatingbubbleview.databinding.BubbleBinding
import kotlin.math.abs

internal class FloatingBubbleView(
    private val builder: FloatingBubble.Builder,
) : BaseFloatingViewBinding<BubbleBinding>(
    context = builder.context,
    initializer = BubbleBinding.inflate(LayoutInflater.from(builder.context))
) {

    /**
     * store previous point for later usage, reset after finger down
     * */
    private val prevPoint = Point(0, 0)
    private val rawPointOnDown = PointF(0f, 0f)
    private val newPoint = Point(0, 0)

    private var halfScreenWidth = sez.fullWidth / 2

    private var orientation = -1

    init {

        orientation = if (sez.fullHeight >= sez.fullWidth) {
            Configuration.ORIENTATION_PORTRAIT
        } else {
            Configuration.ORIENTATION_LANDSCAPE
        }

        setupLayoutParams()
        setupBubbleProperties()
        customTouch()

    }

    private var isAnimatingToEdge = false
    fun animateIconToEdge(onFinished: (() -> Unit)? = null) {
        if (isAnimatingToEdge) return

        val bubbleWidthCompat = if (builder.bubbleView != null) {
            builder.bubbleView!!.width
        } else {
            width
        }

        isAnimatingToEdge = true
        val iconX = binding.root.getXYPointOnScreen().x

        val isOnTheLeftSide = iconX + bubbleWidthCompat / 2 < halfScreenWidth
        val startX: Int
        val endX: Int
        if (isOnTheLeftSide) {
            startX = iconX
            endX = 0
        } else {
            startX = iconX
            endX = sez.safeWidth - bubbleWidthCompat
        }

        AnimHelper.startSpringX(
            startValue = startX.toFloat(),
            finalPosition = endX.toFloat(),
            event = object : AnimHelper.Event {
                override fun onUpdate(float: Float) {
                    try {
                        windowParams.x = float.toInt()
                        update()
                    } catch (_: Exception) {
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

        windowParams.apply {
            x = builder.startPoint.x
            y = builder.startPoint.y
        }

        if (builder.bubbleView != null) {

            binding.bubbleRoot.addView(builder.bubbleView)

            return
        }

        if (builder.composeLifecycleOwner != null) {

            builder.bubbleView =
                binding.bubbleRoot.findViewById<ComposeView>(R.id.view_compose).apply {
                    setContent {
                        builder.composeView!!()
                    }
                    visibility = View.VISIBLE
                }

            builder.composeLifecycleOwner?.attachToDecorView(binding.bubbleRoot)

            return
        }

    }

    fun updateLocationUI(x: Float, y: Float) {
        val mIconDeltaX = x - rawPointOnDown.x
        val mIconDeltaY = y - rawPointOnDown.y

        newPoint.x = prevPoint.x + mIconDeltaX.toInt()
        newPoint.y = prevPoint.y + mIconDeltaY.toInt()

        //region prevent bubble Y point move outside the screen
        val safeTopY = 0
        val safeBottomY = sez.safeHeight - binding.root.height

        val isAboveStatusBar = newPoint.y < safeTopY
        val isUnderSoftNavBar = newPoint.y > safeBottomY
        if (isAboveStatusBar) {
            newPoint.y = safeTopY
        } else if (isUnderSoftNavBar) {
            newPoint.y = safeBottomY
        }
        //endregion

        windowParams.x = newPoint.x
        windowParams.y = newPoint.y
        update()
    }

    /**
     * set location without updating UI
     * */
    fun setLocation(x: Float, y: Float) {
        newPoint.x = x.toInt()
        newPoint.y = y.toInt()
    }

    fun rawLocationOnScreen(): Pair<Float, Float> {
        return Pair(newPoint.x.toFloat(), newPoint.y.toFloat())
    }

    /**
     * pass close bubble point
     * */
    fun animateTo(x: Float, y: Float) {
        AnimHelper.animateSpringPath(
            startX = newPoint.x.toFloat(),
            startY = newPoint.y.toFloat(),
            endX = x,
            endY = y,
            event = object : AnimHelper.Event {
                override fun onUpdatePoint(x: Float, y: Float) {

                    windowParams.x = x.toInt()
                    windowParams.y = y.toInt()

//                    builder.listener?.onMove(x.toFloat(), y.toFloat()) // don't call this line, it'll spam multiple MotionEvent.OnActionMove
                    update()

                }
            }
        )
    }

    private val MAX_X_MOVE = 1f
    private val MAX_Y_MOVE = 1f
    private var ignoreClick: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    private fun customTouch() {

        fun handleMovement(event: MotionEvent) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    prevPoint.x = windowParams.x
                    prevPoint.y = windowParams.y

                    rawPointOnDown.x = event.rawX
                    rawPointOnDown.y = event.rawY

                    builder.listener?.onDown(event.rawX, event.rawY)
                }

                MotionEvent.ACTION_MOVE -> {
                    builder.listener?.onMove(event.rawX, event.rawY)
                }

                MotionEvent.ACTION_UP -> {
                    builder.listener?.onUp(event.rawX, event.rawY)
                }
            }
        }

        fun ignoreChildClickEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    ignoreClick = false
                }

                MotionEvent.ACTION_MOVE -> {
                    if (abs(event.x) > MAX_X_MOVE || abs(event.y) > MAX_Y_MOVE) {
                        ignoreClick = true
                    }
                }
            }

            return ignoreClick
        }

        // listen actions --------------------------------------------------------------------------


        binding.bubbleRoot.apply {

            afterMeasured { updateGestureExclusion() }

            doOnTouchEvent = {
                handleMovement(it)
            }

            ignoreChildEvent = { motionEvent ->
                ignoreChildClickEvent(motionEvent)
            }
        }
    }

    override fun setupLayoutParams() {
        super.setupLayoutParams()

        windowParams.apply {
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS


            builder.bubbleStyle?.let {
                windowAnimations = it
            }

        }
    }
}