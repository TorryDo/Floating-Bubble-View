package com.torrydo.floatingbubbleview

import android.content.Intent
import android.os.IBinder


abstract class FloatingBubbleService : FloatingBubbleServiceConfig(), Logger by LoggerImpl() {

    // service lifecycle ---------------------------------------------------------------------------
    override fun onCreate() {
        super.onCreate()
        d("floatingBubble service is created")
    }

    override fun onDestroy() {
        super.onDestroy()
        d("floatingBubble service is destroyed")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Const.IS_LOGGER_ENABLED = setLoggerEnabled()

        if (isDrawOverlaysPermissionGranted()) {
            setupViewAppearance()
        } else {
            throw PermissionDeniedException()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // overridable func ------------------------------------------------------------------------------------

    @Deprecated("this function may not work properly", ReplaceWith("true"))
    open fun setLoggerEnabled(): Boolean = true

    // private -------------------------------------------------------------------------------------


}