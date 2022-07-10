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
            isHigherThanAndroid8 {
                setupViewAppearance()
                startForeground()
                return START_STICKY
            }
            setupViewAppearance()

        } else {
            throw PermissionDeniedException()
        }

        return START_STICKY
    }

    private val CHANNEL_DEFAULT_IMPORTANCE = "bubbles"
    private val ONGOING_NOTIFICATION_ID = 101

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(
                    "bubble_service",
                    "floating bubble"
                )
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_rounded_blue_diamond)
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(ONGOING_NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        channelName: String
    ): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O, lambda = 0)
    private inline fun isHigherThanAndroid8(job: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            job()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // overridable func ------------------------------------------------------------------------------------

    @Deprecated("this function may not work properly", ReplaceWith("true"))
    open fun setLoggerEnabled(): Boolean = true

    // private -------------------------------------------------------------------------------------


}