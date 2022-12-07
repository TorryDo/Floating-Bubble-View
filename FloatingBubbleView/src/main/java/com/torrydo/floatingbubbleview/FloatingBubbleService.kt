package com.torrydo.floatingbubbleview

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN


abstract class FloatingBubbleService : FloatingBubbleServiceConfig(), Logger by LoggerImpl() {

    companion object {
        /**
         * this bool works as expected if only one bubble showed at a time, multiple bubbles may cause unexpected results
         * */
        @JvmStatic
        var isRunning = false
            private set

//        /**
//         * force the bubble to stop
//         * */
//        fun <T: FloatingBubbleService> stop(context: Context){
//            context.stopService(Intent(context, T::class.java))
//        }

    }


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        isRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Const.IS_LOGGER_ENABLED = setLoggerEnabled()

        if (isDrawOverlaysPermissionGranted()) {

            setupViewAppearance()

            if (isHigherThanAndroid8()) {
                startBubbleForeground()
            }

        } else throw PermissionDeniedException()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }


    // overridable func ----------------------------------------------------------------------------

    open fun channelId() = "bubble_service"
    open fun channelName() = "floating bubble"

    open fun notificationId() = 101

    open fun startBubbleForeground() {

        val channelId = if (isHigherThanAndroid8()) {
            createNotificationChannel(channelId(), channelName())
        } else {
            // In earlier version, channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
            ""
        }
        val notification = setupNotificationBuilder(channelId)

        startForeground(notificationId(), notification)
    }

    open fun setupNotificationBuilder(channelId: String): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_rounded_blue_diamond)
            .setContentTitle("bubble is running")
//            .setContentText("click to do nothing")
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }


    @Deprecated("this function may not work properly", ReplaceWith("true"))
    open fun setLoggerEnabled(): Boolean = true

    // helper --------------------------------------------------------------------------------------

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        channelName: String
    ): String {
        val channel = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    private fun isHigherThanAndroid8() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

}