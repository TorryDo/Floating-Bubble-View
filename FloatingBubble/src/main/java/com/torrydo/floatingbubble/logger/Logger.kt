package com.torrydo.floatingbubble.logger

import android.util.Log
import com.torrydo.floatingbubble.utils.toTag

class Logger : ILogger {

    private var TAG = javaClass.simpleName.toTag()
    private var isEnabled = false         // is Debug Enabled

    override fun setTag(tag: String): ILogger {
        TAG = tag
        return this
    }

    override fun setDebugEnabled(isEnabled: Boolean): ILogger {
        this.isEnabled = isEnabled
        return this
    }

    override fun log(message: String) {
        if (isEnabled) {
            Log.d(TAG, message)
        }
    }

    override fun log(message: String, throwable: Throwable) {
        if (isEnabled) {
            Log.e(TAG, message, throwable)
        }
    }

    override fun error(message: String) {
        if (isEnabled) {
            Log.e(TAG, message)
        }
    }

}