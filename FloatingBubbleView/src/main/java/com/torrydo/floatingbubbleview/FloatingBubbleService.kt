package com.torrydo.floatingbubbleview

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.torrydo.floatingbubbleview.main.FloatingBubble
import com.torrydo.floatingbubbleview.main.FloatingBubbleBuilder
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableView
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableViewBuilder
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableViewListener
import com.torrydo.floatingbubbleview.utils.ThreadHelper
import com.torrydo.floatingbubbleview.utils.logger.ILogger
import com.torrydo.floatingbubbleview.utils.logger.Logger
import com.torrydo.floatingbubbleview.utils.toTag

abstract class FloatingBubbleService : Service() {

    private lateinit var logger: ILogger

    private var floatingBubble: FloatingBubble? = null

    override fun onCreate() {
        super.onCreate()
        logger = Logger().setTag(javaClass.simpleName.toTag()).setDebugEnabled(true)
        logger.log("floating bubble service created")
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.log("service destroyed")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        floatingBubble = setupBubble()
            .build()

        ThreadHelper().runOnMainThread {
            try {
                floatingBubble!!.show()
            } catch (e: Exception) {
                logger.error(e.message.toString())
            }
        }

        return START_NOT_STICKY
    }

    abstract fun setupBubble(): FloatingBubbleBuilder

    abstract fun setupExpandableView(listener: ExpandableViewListener): ExpandableViewBuilder

    override fun onBind(intent: Intent?): IBinder? = null

    // private -------------------------------------------------------------------------------------

}