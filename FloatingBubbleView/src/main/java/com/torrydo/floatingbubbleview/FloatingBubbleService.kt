package com.torrydo.floatingbubbleview

import android.content.Intent
import android.os.IBinder


abstract class FloatingBubbleService : FloatingBubbleServiceConfig() {

    private lateinit var logger: ILogger

    override fun onCreate() {
        super.onCreate()
        logger = Logger().setTag(javaClass.simpleName.toTag())
            .setDebugEnabled(Constants.IS_DEBUG_ENABLED)
        logger.log("floating bubble service created")
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.log("service destroyed")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Constants.IS_DEBUG_ENABLED = setDebugEnabled()
        setup()

        return START_NOT_STICKY
    }

    open fun setDebugEnabled(): Boolean = false

    override fun onBind(intent: Intent?): IBinder? = null


}