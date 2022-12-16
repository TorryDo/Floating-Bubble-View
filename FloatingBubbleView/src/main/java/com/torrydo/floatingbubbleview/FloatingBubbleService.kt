package com.torrydo.floatingbubbleview

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log.d
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.core.content.ContextCompat.getSystemService


abstract class FloatingBubbleService : FloatingBubbleServiceConfig(), Logger by LoggerImpl() {


    companion object {

        private const val REQUEST_CODE_STOP_PENDING_INTENT = 1

        private var isRunning = false

        /**
         * this function works as expected if only one bubble showed at a time, multiple bubbles may cause unexpected results
         * */
        @JvmStatic
        fun isRunning() = isRunning

    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        isRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.isLoggerEnabled = enableLogger()

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
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }


    open fun enableLogger(): Boolean = false

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
    private fun isHigherThanAndroid8() = Build.VERSION.SDK_INT >= AndroidVersions.`8`

}