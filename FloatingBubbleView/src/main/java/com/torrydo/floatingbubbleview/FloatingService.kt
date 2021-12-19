package com.torrydo.floatingbubbleview

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.torrydo.floatingbubbleview.utils.ThreadHelper
import com.torrydo.floatingbubbleview.utils.logger.ILogger
import com.torrydo.floatingbubbleview.utils.logger.Logger
import com.torrydo.floatingbubbleview.utils.toTag

open class FloatingService : Service() {

    private lateinit var logger: ILogger

    private var floatingBubble: FloatingBubble? = null

    override fun onCreate() {
        super.onCreate()
        logger = Logger().setTag(javaClass.simpleName.toTag()).setDebugEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.log("service destroyed")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logger.log("start service")
        floatingBubble = setupBubbleBuilder().build()
        ThreadHelper().runOnMainThread {
            try {
                floatingBubble!!.show()
            } catch (e: Exception) {
                logger.error(e.message.toString())
            }
        }

        return START_NOT_STICKY
    }

    open fun setupBubbleBuilder(): FloatingBubbleBuilder = FloatingBubbleBuilder().with(this)

    override fun onBind(intent: Intent?): IBinder? = null

}