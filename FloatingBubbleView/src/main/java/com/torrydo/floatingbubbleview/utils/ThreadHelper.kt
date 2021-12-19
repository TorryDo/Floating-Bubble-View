package com.torrydo.floatingbubbleview.utils

import android.os.Handler
import android.os.Looper

class ThreadHelper {

    fun runOnMainThread(doWork: () -> Unit) {
        Handler(Looper.getMainLooper()).post {
            doWork()
        }
    }

}