package com.torrydo.floatingbubbleview

import android.os.Handler
import android.os.Looper

object ThreadHelper {

    fun runOnMainThread(doWork: () -> Unit) {
        Handler(Looper.getMainLooper()).post {
            doWork()
        }
    }

}