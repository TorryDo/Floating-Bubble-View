package com.torrydo.floatingbubbleview

import android.content.Intent
import android.os.IBinder


abstract class FloatingBubbleService : FloatingBubbleServiceConfig() {

    // service lifecycle ---------------------------------------------------------------------------
    override fun onCreate() {
        super.onCreate()
        d("floating bubble service created")
    }

    override fun onDestroy() {
        super.onDestroy()
        d("floating bubble service destroyed")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Const.IS_LOGGER_ENABLED = setLoggerEnabled()
        setupViewAppearance()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // lib func ------------------------------------------------------------------------------------

    open fun setLoggerEnabled(): Boolean = true


}