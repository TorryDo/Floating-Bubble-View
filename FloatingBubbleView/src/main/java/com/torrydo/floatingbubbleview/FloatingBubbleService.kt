package com.torrydo.floatingbubbleview

import android.content.Intent
import android.os.IBinder


abstract class FloatingBubbleService : FloatingBubbleServiceConfig() {


    override fun onCreate() {
        super.onCreate()
        d("floating bubble service created")
    }

    override fun onDestroy() {
        super.onDestroy()
        d("service destroyed")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Const.IS_LOGGER_ENABLED = setDebugEnabled()
        setup()

        return START_NOT_STICKY
    }

    open fun setDebugEnabled(): Boolean = true

    override fun onBind(intent: Intent?): IBinder? = null


}