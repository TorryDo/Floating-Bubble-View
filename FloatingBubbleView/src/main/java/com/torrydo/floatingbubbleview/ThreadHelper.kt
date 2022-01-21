package com.torrydo.floatingbubbleview

import android.os.Handler
import android.os.Looper

internal object ThreadHelper {

    fun runOnMainThread(doWork: () -> Unit) {
        Handler(Looper.getMainLooper()).post {
            doWork()
        }
    }

}