package com.torrydo.floatingbubbleview

import android.util.Log

//class Logger{
//
//    companion object{
//        fun d(message String){
//
//        }
//    }
//
//}


internal interface Logger {

    fun getTagName(): String

    fun setTagName(tagName: String)

    fun enableLogger(enabled: Boolean)

    fun d(message: String)

    fun e(message: String)
}

internal open class LoggerImpl : Logger {

    private var tag: String? = null
    private var isLoggerEnabled: Boolean = Const.IS_DEBUG_ENABLED

    override fun getTagName(): String {
        if (tag == null) tag = getClassName()
        return tag!!
    }

    override fun setTagName(tagName: String) {
        tag = tagName.toTag()
    }

    override fun enableLogger(enabled: Boolean) {
        isLoggerEnabled = enabled
    }

    override fun d(message: String) {
        if (isLoggerEnabled) {
            Log.d(getTagName(), message)
        }
    }

    override fun e(message: String) {
        if (isLoggerEnabled) {
            Log.e(getTagName(), message)
        }
    }

    // private -------------------------------------------------------------------------------------

    open fun getClassName() = javaClass.simpleName.toTag()

}

internal fun String.toTag() = "<> $this"