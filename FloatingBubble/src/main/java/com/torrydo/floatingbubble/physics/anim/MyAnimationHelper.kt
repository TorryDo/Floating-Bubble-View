package com.torrydo.transe.utils.anim

import android.view.View
import androidx.dynamicanimation.animation.*

class MyAnimationHelper {

    fun startSpringX(
        startValue: Float,
        position: Float,
        animState: AnimState   // an interface
    ) {
        SpringAnimation(FloatValueHolder()).apply {
            spring = SpringForce().apply {
                setStartValue(startValue)
                stiffness = SpringForce.STIFFNESS_LOW
                finalPosition = position
                dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY

            }

            addUpdateListener { animation, value, velocity ->
                animState.onUpdate(value)
            }

            addEndListener { animation, canceled, value, velocity ->
                animState.onFinish()
            }

        }.start()
    }

    fun startFlingX(
        v: View,
        startVelocity: Float,
        position: Float,
        animState: AnimState   // an interface
    ) {
        FlingAnimation(v, DynamicAnimation.SCROLL_X).apply {
            setStartVelocity(startVelocity)
            setMinValue(0f)
            setMaxValue(position)
            friction = 1.1f

            addUpdateListener { animation, value, velocity ->
                animState.onUpdate(value)
            }

            start()
        }
    }

}