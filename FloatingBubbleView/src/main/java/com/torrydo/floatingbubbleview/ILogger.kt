package com.torrydo.floatingbubbleview

interface ILogger {

    fun setTag(tag: String): ILogger

    fun setDebugEnabled(isEnabled: Boolean): ILogger

    fun log(message: String){}

    fun log(message: String, throwable: Throwable){}

    fun error(message: String){}

}