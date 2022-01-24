package com.torrydo.floatingbubbleview

import android.os.Handler
import android.os.Looper

internal object ThreadHelper {

    inline fun runOnMainThread(crossinline doWork: () -> Unit) {
        Handler(Looper.getMainLooper()).post {
            doWork()
        }
    }

}