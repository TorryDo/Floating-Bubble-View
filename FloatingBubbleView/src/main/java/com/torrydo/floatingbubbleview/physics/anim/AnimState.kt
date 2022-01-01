package com.torrydo.floatingbubbleview.physics.anim

interface AnimState {

    fun onStart(){}

    fun onFinish(){}

    fun onFailure(){}

    fun onUpdate(float: Float){}

}