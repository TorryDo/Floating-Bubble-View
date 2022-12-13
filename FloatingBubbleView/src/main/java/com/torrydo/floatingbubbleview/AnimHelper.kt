package com.torrydo.floatingbubbleview

import android.view.View
import androidx.dynamicanimation.animation.*

internal class AnimHelper {

    companion object{
        /**
         * default minimum value of the property to be animated
         * */
        const val MIN_VALUE = 0f
        const val DEFAULT_FRICTION = 1.1f
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
                animationListener.onFinish()
            }

        }.start()
    }

    fun startFlingX(
        v: View,
        startVelocity: Float,
        position: Float,
        animationListener: Event
    ) {
        FlingAnimation(v, DynamicAnimation.SCROLL_X).apply {
            setStartVelocity(startVelocity)
            setMinValue(MIN_VALUE)
            setMaxValue(position)
            friction = DEFAULT_FRICTION

            addUpdateListener { animation, value, velocity ->
                animationListener.onUpdate(value)
            }

            start()
        }
    }

}