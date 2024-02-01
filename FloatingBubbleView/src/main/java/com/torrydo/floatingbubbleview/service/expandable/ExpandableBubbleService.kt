package com.torrydo.floatingbubbleview.service.expandable

import android.content.Context
import android.content.res.Configuration
import androidx.dynamicanimation.animation.SpringForce
import com.torrydo.floatingbubbleview.CloseBubbleBehavior
import com.torrydo.floatingbubbleview.FloatingBubbleListener
import com.torrydo.floatingbubbleview.ServiceInteractor
import com.torrydo.floatingbubbleview.bubble.FloatingBottomBackground
import com.torrydo.floatingbubbleview.bubble.FloatingBubble
import com.torrydo.floatingbubbleview.bubble.FloatingCloseBubble
import com.torrydo.floatingbubbleview.service.FloatingBubbleService
import com.torrydo.floatingbubbleview.sez

abstract class ExpandableBubbleService : FloatingBubbleService() {

    private var _context: Context? = null
    private val context get() = _context!!

    // 0: nothing
    // 1: bubble
    // 2: expanded-bubble
    private var state = 0

    var bubble: FloatingBubble? = null
        private set

    var expandedBubble: FloatingBubble? = null
        private set

    private var closeBubble: FloatingCloseBubble? = null
    private var bottomBackground: FloatingBottomBackground? = null

    internal var serviceInteractor: ServiceInteractor? = null

    private fun createBubbles(
        context: Context,
        bubbleBuilder: BubbleBuilder?,
        expandedBuilder: ExpandedBubbleBuilder?
    ) {

        this._context = context

        if (bubbleBuilder != null) {
            // setup bubble
            if (bubbleBuilder.bubbleView != null) {
                bubble = FloatingBubble(
                    context,
                    forceDragging = bubbleBuilder.forceDragging,
                    containCompose = false,
                    listener = bubbleBuilder.listener,
                    animateBeforeExpandViewShow = bubbleBuilder.isAnimatedBeforeExpand,
                    locationBeforeExpand = bubbleBuilder.expandViewInitialPoint
                )
                bubble!!.rootGroup.addView(bubbleBuilder.bubbleView)
            } else {
                bubble = FloatingBubble(
                    context,
                    forceDragging = bubbleBuilder.forceDragging,
                    containCompose = true,
                    listener = bubbleBuilder.listener,
                    animateBeforeExpandViewShow = bubbleBuilder.isAnimatedBeforeExpand,
                    locationBeforeExpand = bubbleBuilder.expandViewInitialPoint
                )
                bubble!!.rootGroup.addView(bubbleBuilder.bubbleCompose)
            }
            bubble!!.mListener = CustomBubbleListener(
                bubble!!,
                bubbleBuilder.isAnimateToEdgeEnabled,
                closeBehavior = bubbleBuilder.behavior,
                isCloseBubbleEnabled = true
            )
            bubble!!.layoutParams = bubbleBuilder.defaultLayoutParams()
            bubble!!.isDraggable = bubbleBuilder.isBubbleDraggable

            // setup close-bubble
            if (bubbleBuilder.closeView != null) {
                closeBubble = FloatingCloseBubble(
                    context,
                    bubbleBuilder.closeView!!,
                    distanceToClosePx = bubbleBuilder.distanceToClosePx,
                    bottomPaddingPx = bubbleBuilder.closeBubbleBottomPaddingPx
                )
                closeBubble!!.layoutParams.apply {
                    bubbleBuilder.closeBubbleStyle?.let {
                        windowAnimations = it
                    }
                }
            }

            // setup bottom-background
            if (bubbleBuilder.isBottomBackgroundEnabled) {
                bottomBackground = FloatingBottomBackground(context)
            }

        }

        // setup expanded-bubble
        expandedBuilder?.apply {
            if (expandedView != null) {
                expandedBubble =
                    FloatingBubble(
                        context,
                        containCompose = false,
                        forceDragging = false,
                        onDispatchKeyEvent = expandedBuilder.onDispatchKeyEvent,
                    )
                expandedBubble!!.rootGroup.addView(expandedView)
            } else {
                expandedBubble =
                    FloatingBubble(
                        context = context,
                        containCompose = true,
                        forceDragging = false,
                        onDispatchKeyEvent = expandedBuilder.onDispatchKeyEvent
                    )
                expandedBubble!!.rootGroup.addView(expandedCompose)
            }
            expandedBubble!!.mListener = CustomBubbleListener(
                expandedBubble!!,
                mAnimateToEdge = expandedBuilder.isAnimateToEdgeEnabled,
                isCloseBubbleEnabled = false
            )
            expandedBubble!!.layoutParams = expandedBuilder.defaultLayoutParams().apply {
                expandedBubbleStyle?.let {
                    windowAnimations = it
                }
            }
            expandedBubble!!.isDraggable = expandedBuilder.isDraggable
        }
    }

    //region public methods

    override fun removeAll() {
        bubble?.remove()
        closeBubble?.remove()
        bottomBackground?.remove()
        expandedBubble?.remove()

        state = 0
    }

    fun expand() {
        if (bubble?.animateBeforeExpandViewShow == true) {
            animateBubbleToLocationPx(
                x = bubble?.locationBeforeExpand?.x ?: 0,
                y = bubble?.locationBeforeExpand?.y ?: 0
            ) {
                expandedBubble!!.show()
                bubble?.remove()
            }
        } else {
            expandedBubble!!.show()
            bubble?.remove()
        }

        state = 2
    }

    fun minimize() {
        bubble!!.show()
        expandedBubble?.remove()

        state = 1
    }

    fun enableBubbleDragging(b: Boolean) {
        bubble?.isDraggable = b
    }

    fun enableExpandedBubbleDragging(b: Boolean) {
        expandedBubble?.isDraggable = b
    }

    fun animateBubbleToEdge() {
        bubble?.animateIconToEdge()
    }

    fun animateExpandedBubbleToEdge() {
        expandedBubble?.animateIconToEdge()
    }

    // testing
    internal fun animateBubbleToLocationPx(x: Int, y: Int, onAnimateEnd: (() -> Unit)? = null) {
        bubble?.animateTo(
            x.toFloat(),
            y.toFloat(),
            stiffness = SpringForce.STIFFNESS_VERY_LOW,
            onEnd = onAnimateEnd
        )
    }

    // testing
    internal fun animateExpandedBubbleToLocationPx(x: Int, y: Int) {
        expandedBubble?.animateTo(
            x.toFloat(),
            y.toFloat(),
            stiffness = SpringForce.STIFFNESS_VERY_LOW
        )
    }

    //endregion

    //region private, internal methods -------------------------------------------------------------------

    private fun tryShowCloseBubbleAndBackground() {
        closeBubble?.show()
        bottomBackground?.show()
    }

    internal fun tryRemoveCloseBubbleAndBackground() {
        closeBubble?.remove()
        bottomBackground?.remove()
    }

    //endregion

    //region custom bubble touch
    private inner class CustomBubbleListener(
        private val mBubble: FloatingBubble,
        private val mAnimateToEdge: Boolean,
        private val closeBehavior: CloseBubbleBehavior = CloseBubbleBehavior.FIXED_CLOSE_BUBBLE,
        private val isCloseBubbleEnabled: Boolean = true
    ) : FloatingBubbleListener {

        private var isCloseBubbleVisible = false

        override fun onFingerDown(x: Float, y: Float) {
            mBubble.safeCancelAnimation()
        }

        override fun onFingerMove(x: Float, y: Float) {
            when (closeBehavior) {
                CloseBubbleBehavior.DYNAMIC_CLOSE_BUBBLE -> {
                    mBubble.updateLocationUI(x, y)
                    val (mx, my) = mBubble.rawLocationOnScreen()
                    closeBubble?.followBubble(mx.toInt(), my.toInt(), mBubble)

                }

                CloseBubbleBehavior.FIXED_CLOSE_BUBBLE -> {
                    val isAttracted = closeBubble?.tryAttractBubble(mBubble, x, y) ?: false
                    if (isAttracted.not()) {
                        mBubble.updateLocationUI(x, y)
                    }
                }
            }
            if (isCloseBubbleEnabled && isCloseBubbleVisible.not()) {
                tryShowCloseBubbleAndBackground()
                isCloseBubbleVisible = true
            }
        }

        override fun onFingerUp(x: Float, y: Float) {

            isCloseBubbleVisible = false
            tryRemoveCloseBubbleAndBackground()

            var shouldDestroy =
                when (closeBehavior) {

                    CloseBubbleBehavior.FIXED_CLOSE_BUBBLE -> {
                        closeBubble?.isFingerInsideClosableArea(x, y) ?: false
                    }

                    CloseBubbleBehavior.DYNAMIC_CLOSE_BUBBLE -> {
                        closeBubble?.isBubbleInsideClosableArea(mBubble)
                            ?: false
                    }
                }
            if (closeBubble?.ableToInteract == false) {
                shouldDestroy = false
            }

            if (shouldDestroy) {
                serviceInteractor?.requestStop()
            } else {
                if (mAnimateToEdge) {
                    mBubble.animateIconToEdge()
                }
            }
        }
    }
    //endregion

    // override service

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        bubble?.remove()
        closeBubble?.remove()
        bottomBackground?.remove()
        expandedBubble?.remove()

        sez.refresh()
        createBubbles(this, configBubble(), configExpandedBubble())

        when (state) {
            1 -> minimize()
            2 -> expand()
        }


    }

    abstract fun configBubble(): BubbleBuilder?
    abstract fun configExpandedBubble(): ExpandedBubbleBuilder?

    override fun setup() {
        createBubbles(this, configBubble(), configExpandedBubble())

        this.serviceInteractor = object : ServiceInteractor {
            override fun requestStop() {
                stopSelf()
            }
        }
    }


}
