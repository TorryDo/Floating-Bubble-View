package com.torrydo.floatingbubbleview

import android.view.View
import androidx.dynamicanimation.animation.*

internal class AnimHelper {

    fun startSpringX(
        startValue: Float,
        position: Float,
        animationListener: AnimHelper.Event   // an interface
    ) {
        SpringAnimation(FloatValueHolder()).apply {
            spring = SpringForce().apply {
                setStartValue(startValue)
                stiffness = SpringForce.STIFFNESS_LOW
                finalPosition = position
                dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY

            }

            addUpdateListener { animation, value, velocity ->
                animationListener.onUpdate(value)
            }

            addEndListener { animation, canceled, value, velocity ->
                animationListener.onFinish()
            }

        }.start()
    }

    fun startFlingX(
        v: View,
        startVelocity: Float,
        position: Float,
        animationListener: AnimHelper.Event   // an interface
    ) {
        FlingAnimation(v, DynamicAnimation.SCROLL_X).apply {
            setStartVelocity(startVelocity)
            setMinValue(0f)
            setMaxValue(position)
            friction = 1.1f

            addUpdateListener { animation, value, velocity ->
                animationListener.onUpdate(value)
            }

            start()
        }
    }

    // event ---------------------------------------------------------------------------------------

    interface Event {

        fun onStart() {}

        fun onFinish() {}

        fun onFailure() {}

        fun onUpdate(float: Float) {}

    }

}