package com.torrydo.floatingbubble.physics

interface FloatingViewTouchListener {

    fun onUp(x: Float, y: Float){}

    fun onDown(x: Float, y: Float){}

    fun onTap(){}

    fun onMove(x: Float, y: Float){}

    fun onRemove(){}

}