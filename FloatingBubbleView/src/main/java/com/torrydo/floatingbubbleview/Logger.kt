package com.torrydo.floatingbubbleview

import android.util.Log

private fun String?.addTagPrefix() = "<> $this"

internal interface Logger {

    companion object {
        internal var isLoggerEnabled = false
    }

    fun enableLogger(enabled: Boolean) {
        isLoggerEnabled = enabled
    }

    /**
     * The reason I add 'tag' parameter below because
     * I CAN NOT get class name in runtime using interface delegation
     *
     * - interface delegation is compiled before it's subclass compiled.
     */

    fun d(message: String, tag: String? = javaClass.simpleName.toString())

    fun e(message: String, tag: String? = javaClass.simpleName.toString())

    fun i(message: String, tag: String? = javaClass.simpleName.toString())
}

internal open class LoggerImpl : Logger {

    override fun d(message: String, tag: String?) {
        if (Logger.isLoggerEnabled.not()) return
        Log.d(tag.addTagPrefix(), message)
    }

    override fun e(message: String, tag: String?) {
        if (Logger.isLoggerEnabled.not()) return
        Log.e(tag.addTagPrefix(), message)
    }

    override fun i(message: String, tag: String?) {
        if (Logger.isLoggerEnabled.not()) return
        Log.i(tag.addTagPrefix(), message)
    }


}