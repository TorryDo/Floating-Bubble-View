package com.torrydo.transe.utils.anim

interface AnimState {

    fun onStart(){}

    fun onFinish(){}

    fun onFailure(){}

    fun onUpdate(float: Float){}

}