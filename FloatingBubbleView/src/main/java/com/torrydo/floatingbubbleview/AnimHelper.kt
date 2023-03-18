package com.torrydo.floatingbubbleview

import androidx.annotation.Discouraged
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.FloatValueHolder
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce


internal object AnimHelper {

    /**
     * default minimum value of the property to be animated
     * */
    private const val MIN_VALUE = 0f
    private const val DEFAULT_FRICTION = 1.1f

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
        event: Event,
        stiffness: Float = SpringForce.STIFFNESS_LOW,
        dampingRatio: Float = SpringForce.DAMPING_RATIO_LOW_BOUNCY,
    ) {
        val springAnim = SpringAnimation(FloatValueHolder())

        springAnim.spring = SpringForce().apply {

            springAnim.setStartValue(startValue)
            setFinalPosition(finalPosition)
            this.stiffness = stiffness
            this.dampingRatio = dampingRatio

        }
        springAnim.addUpdateListener { animation, value, velocity ->
            event.onUpdate(value)
        }
        springAnim.addEndListener { animation, canceled, value, velocity ->
            event.onEnd()
        }

        event.onStart()
        springAnim.start()
    }


    fun animateSpringPath(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        event: Event,
        stiffness: Float = SpringForce.STIFFNESS_MEDIUM,
        dampingRatio: Float = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
    ) {
        val xDistance = endX - startX
        val yDistance = endY - startY

        val springAnim = SpringAnimation(FloatValueHolder())

        val springForce = SpringForce().apply {
            this.stiffness = stiffness
            this.dampingRatio = dampingRatio
        }

        if(yDistance>xDistance) {
            springAnim.setStartValue(startY)
            springForce.finalPosition = endY

            springAnim.addUpdateListener { animation, value, velocity ->
                val ratio = 1 - (endY - value) / yDistance
                event.onUpdatePoint(
                    x = startX + xDistance * ratio,
                    y = value
                )
            }
        }else{
            springAnim.setStartValue(startX)
            springForce.finalPosition = endX

            springAnim.addUpdateListener { animation, value, velocity ->
                val ratio = (value-startX) / xDistance
                event.onUpdatePoint(
                    x = value,
                    y = startY + yDistance * ratio
                )
            }
        }

        springAnim.spring = springForce
        springAnim.addEndListener { animation, canceled, value, velocity ->
            event.onEnd()
        }

        event.onStart()
        springAnim.start()
    }

    @Discouraged("not finished yet")
    fun startFlingX(
        startVelocity: Float = 50f,
        startValue: Float,
        minValue: Float,
        maxValue: Float,
        friction: Float = DEFAULT_FRICTION,
        animationListener: Event
    ) {
        FlingAnimation(FloatValueHolder()).apply {
            setStartValue(startValue)
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