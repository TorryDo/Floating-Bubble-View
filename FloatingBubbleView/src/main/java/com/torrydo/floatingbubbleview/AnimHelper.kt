package com.torrydo.floatingbubbleview

import android.view.View
import androidx.dynamicanimation.animation.*

internal class AnimHelper {

    companion object{
        val MIN_VALUE = 0f          // I know this variable name is useless :(
        val DEFAULT_FRACTION = 1.1f
    }

    // event ---------------------------------------------------------------------------------------

    interface Event {

        fun onStart() {}

        fun onFinish() {}

        fun onFailure() {}

        fun onUpdate(float: Float) {}

    }

    // func ----------------------------------------------------------------------------------------

    fun startSpringX(
        startValue: Float,
        position: Float,
        animationListener: AnimHelper.Event
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
            setMinValue(MIN_VALUE)
            setMaxValue(position)
            friction = DEFAULT_FRACTION

            addUpdateListener { animation, value, velocity ->
                animationListener.onUpdate(value)
            }

            start()
        }
    }

}