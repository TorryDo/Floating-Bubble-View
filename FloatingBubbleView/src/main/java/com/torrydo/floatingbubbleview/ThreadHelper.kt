package com.torrydo.floatingbubbleview

import android.os.Handler
import android.os.Looper

//internal object MyLogger{
//    private var _logger: Logger? = null
//    val logger: Logger get(){
//        if(_logger == null) _logger = LoggerImpl()
//        return _logger!!
//    }
//}

internal inline fun onMainThread(crossinline doWork: () -> Unit) {
    Handler(Looper.getMainLooper()).post {
        doWork()
    }
}

//internal inline fun onBackgroundThread(crossinline doWork: () -> Unit): Thread {
//    val thread = Thread{
//        doWork()
//    }
//
//    thread.run()
//
//    return thread
//}