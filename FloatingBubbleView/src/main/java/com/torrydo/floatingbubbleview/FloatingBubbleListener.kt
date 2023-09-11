package com.torrydo.floatingbubbleview

interface FloatingBubbleListener {

    /**
     * the location of the finger on the screen
     * */
    fun onFingerDown(x: Float, y: Float) {}

    /**
     * the location of the finger on the screen
     * */
    fun onFingerUp(x: Float, y: Float) {}

    /**
     * the location of the finger on the screen
     * */
    fun onFingerMove(x: Float, y: Float) {}

}