package com.torrydo.floatingbubbleview

interface AnimationListener {

    fun onStart(){}

    fun onFinish(){}

    fun onFailure(){}

    fun onUpdate(float: Float){}

}