package com.torrydo.floatingbubbleview

interface FloatingBubbleTouchListener {

    fun onDown(x: Int, y: Int){}

    fun onUp(x: Int, y: Int){}

    fun onMove(x: Int, y: Int){}

    fun onClick(){}

    fun onDestroy(){}

}