package com.torrydo.floatingbubbleview.physics.anim

interface AnimationListener {

    fun onStart(){}

    fun onFinish(){}

    fun onFailure(){}

    fun onUpdate(float: Float){}

}