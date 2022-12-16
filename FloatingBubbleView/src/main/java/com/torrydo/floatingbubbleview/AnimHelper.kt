package com.torrydo.floatingbubbleview

import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.FloatValueHolder
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce


internal class AnimHelper {

    companion object {
        /**
         * default minimum value of the property to be animated
         * */
        private const val MIN_VALUE = 0f
        private const val DEFAULT_FRICTION = 1.1f
    }

    // event ---------------------------------------------------------------------------------------

    interface Event {
        fun onStart() {}
        fun onEnd() {}
        fun onCancel() {}
        fun onUpdate(float: Float) {}
        fun onUpdatePoint(x: Float, y: Float) {}
    }

    // func ----------------------------------------------------------------------------------------

    fun startSpringX(
        startValue: Float,
        finalPosition: Float,
        animationListener: Event
    ) {
        SpringAnimation(FloatValueHolder()).apply {

            spring = SpringForce().apply {

                setStartValue(startValue)
                setFinalPosition(finalPosition)
                stiffness = SpringForce.STIFFNESS_LOW
                dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY

            }

            addUpdateListener { animation, value, velocity ->
                animationListener.onUpdate(value)
            }

            addEndListener { animation, canceled, value, velocity ->
                animationListener.onEnd()
            }

        }.start()
    }

    fun animateFlingPath(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        durationMillis: Long = 200,
        event: Event
    ) : ValueAnimator{

        val yRange = endY - startY
        // startY + (endY - startY)*percentX

        return ValueAnimator.ofFloat(startX, endX).apply {
            duration = durationMillis
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                val updatedX = it.animatedValue as Float
                val percentX = updatedX / endX
                event.onUpdatePoint(
                    x = it.animatedValue as Float,
                    y = startY + (yRange * percentX)
                )
            }
            addListener(
                onStart = {
                    event.onStart()
                },
                onCancel = {
                    event.onCancel()
                },
                onEnd = {
                    event.onEnd()
                },
            )
        }

    }

    fun startFlingX(
        startVelocity: Float = 50f,
        minValue: Float = MIN_VALUE,
        maxValue: Float,
        friction: Float = DEFAULT_FRICTION,
        animationListener: Event
    ) {
        FlingAnimation(FloatValueHolder()).apply {
            setStartVelocity(startVelocity)
            setMinValue(minValue)
            setMaxValue(maxValue)
            setFriction(friction)

            addUpdateListener { animation, value, velocity ->
                animationListener.onUpdate(value)
            }

            start()
        }
    }

}