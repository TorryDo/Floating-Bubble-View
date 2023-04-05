package com.torrydo.floatingbubbleview

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.Discouraged
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.core.app.NotificationManagerCompat


abstract class FloatingBubbleService : Service() {

    private var floatingBubble: FloatingBubble? = null
    private var expandableView: ExpandableView? = null

    private var isNotificationInitialized = false

    private var currentRoute = Route.Empty

    companion object {

        private var isRunning = false

        /**
         * this function works as expected if only one bubble service running at a time, multiple bubbles may cause unexpected results
         * */
        @Discouraged("may cause race conditions and is likely to be changed or replaced in the future.")
        @JvmStatic
        fun isRunning() = isRunning

    }

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onCreate() {
        super.onCreate()
        isRunning = true

        currentRoute = initialRoute()

        when (currentRoute) {
            Route.Empty -> {
                initViewsAndNotification()
            }
            Route.Bubble -> {
                initViewsAndNotification()
                showBubbles()
            }
            Route.ExpandableView -> {
                initViewsAndNotification()
                showExpandableView()
            }
        }
    }

    override fun onDestroy() {
        removeAllViews()
        super.onDestroy()
        isRunning = false
    }

    // region Public Methods -----------------------------------------------------------------------

    fun currentRoute() = currentRoute

    fun showBubbles() {
        floatingBubble!!.showIcon()
    }

    /**
     * remove bubble and background if not null
     * */
    fun removeBubbles() {
        floatingBubble?.removeIcon()
        floatingBubble?.tryRemoveCloseBubbleAndBackground()
    }

    /**
     * @return true means expandable-view showing successfully, false otherwise
     * */
    @Throws(NotImplementedError::class)
    fun showExpandableView(): Boolean {
        if (expandableView == null) {
            throw NotImplementedError("you DID NOT override expandable view")
        }
        try {
            expandableView!!.show()
        } catch (e: Exception) {
            Log.e("<>", "showExpandableView: ", e)
            return false
        }

        return true
    }

    fun removeExpandableView() {
        expandableView?.remove()
    }

    /**
     * remove all views or init notification if first-time call
     * */
    fun removeAllViews() {

        if (isNotificationInitialized.not()) {
            initViewsAndNotification()
            isNotificationInitialized = true
            return
        }

        removeExpandableView()
        removeBubbles()
    }

    //endregion

    /**
     * init view instances and notification
     * */
    @Throws(PermissionDeniedException::class)
    private fun initViewsAndNotification() {

        if (!isDrawOverlaysPermissionGranted()) {
            throw PermissionDeniedException()
        }

        initViewInstances()

        if (isHigherThanAndroid8()) {
            showForegroundNotification()
        }

    }

    private fun initViewInstances() {
        floatingBubble = setupBubble(customFloatingBubbleAction)
            .addFloatingBubbleListener(customFloatingBubbleListener)
            .build()

        expandableView = setupExpandableView(customExpandableViewListener)
            ?.build()
    }


    //region Interface ----------------------------------------------------------------------------

    private val customExpandableViewListener = object : ExpandableView.Action {
        override fun popToBubble() {
            removeExpandableView()
            showBubbles()
        }
    }

    private val customFloatingBubbleListener = object : FloatingBubble.Listener {
        override fun onDestroy() {
            removeAllViews()
            stopSelf()
        }
    }

    private val customFloatingBubbleAction = object : FloatingBubble.Action {
        override fun navigateToExpandableView() {
            if (showExpandableView()) {
                removeBubbles()
            }
        }
    }
    //endregion

    //region Notification --------------------------------------------------------------------------

    open fun channelId() = "bubble_service"
    open fun channelName() = "floating bubble"

    open fun notificationId() = 101

    /**
     * create a new instance by calling `setupNotificationBuilder`, then update it to the existing notification
     * */
    fun updateNotification() {
        val notification = setupNotificationBuilder(channelId())
        NotificationManagerCompat.from(this).notify(notificationId(), notification)
    }

    private fun showForegroundNotification() {

        val channelId = if (isHigherThanAndroid8()) {
            createNotificationChannel(channelId(), channelName())
        } else {
            // In earlier version, channel ID is not required
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
            ""
        }
        val notification = setupNotificationBuilder(channelId)

        startForeground(notificationId(), notification)

        isNotificationInitialized = true
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        channelName: String
    ): String {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT // IMPORTANCE_NONE recreate the notification
        )
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.createNotificationChannel(channel)
        return channelId
    }

    //endregion

    //region View builders ---------------------------------------------------------------------

    open fun initialRoute(): Route = Route.Bubble

    abstract fun setupBubble(action: FloatingBubble.Action): FloatingBubble.Builder

    open fun setupExpandableView(action: ExpandableView.Action): ExpandableView.Builder? = null

    //endregion

    //region Helper Method -------------------------------------------------------------------------
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    private fun isHigherThanAndroid8() = Build.VERSION.SDK_INT >= AndroidVersions.`8`
    //endregion

}