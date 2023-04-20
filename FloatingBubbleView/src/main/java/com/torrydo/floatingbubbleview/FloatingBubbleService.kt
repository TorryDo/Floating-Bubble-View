package com.torrydo.floatingbubbleview

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
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

        @Volatile
        private var isRunning = false

        /**
         * This function works as expected if only one bubble service running at a time,
         * multiple bubbles may cause unexpected results
         *
         * Please note that the onDestroy() method in a Service is not guaranteed to be called in all situations,
         * such as when the system kills the Service to free up resources.
         * This means that the isRunning() method in this library may not always return the correct value and should be used with caution.
         * Consider using an alternative approach to checking the running state of the Service.
         * */
        @Discouraged("May not work properly and is likely to be changed or replaced in the future. Use with caution and consider alternative approaches.")
        @JvmStatic
        fun isRunning() = isRunning

    }

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onCreate() {
        super.onCreate()

        if (!isDrawOverlaysPermissionGranted()) {
            throw PermissionDeniedException()
        }

        isRunning = true

        currentRoute = initialRoute()

        initialNotification()?.let {
            notify(it)
        }

        when (currentRoute) {
            Route.Empty -> {}
            Route.Bubble -> showBubbles()
            Route.ExpandableView -> showExpandableView()
        }
    }

    override fun onDestroy() {
        removeAllViews()
        super.onDestroy()
        isRunning = false
    }

    // region Show/Hide methods -----------------------------------------------------------------------

    /**
     * get current route
     * */
    fun currentRoute() = currentRoute

    fun showBubbles() {
        if (floatingBubble == null) {
            floatingBubble = setupBubble(customFloatingBubbleAction)
                .addServiceInteractor(customFloatingBubbleServiceInteractor)
                .build()
        }
        floatingBubble!!.showIcon()

        currentRoute = Route.Bubble

    }

    /**
     * remove bubble and background
     * */
    fun removeBubbles() {
        floatingBubble?.removeIcon()
        floatingBubble?.tryRemoveCloseBubbleAndBackground()

        if (currentRoute == Route.Bubble) {
            currentRoute = Route.Empty
        }
    }

    /**
     * @return True if the expandable view is showing successfully, False otherwise.
     * */
    @Throws(NotImplementedError::class)
    fun showExpandableView(): Boolean {
        if (expandableView == null) {

            expandableView = setupExpandableView(customExpandableViewListener)
                ?.build()

            if (expandableView == null) {
                throw NotImplementedError("you DID NOT override expandable view")
            }
        }
        try {
            expandableView!!.show()

            currentRoute = Route.ExpandableView

        } catch (e: Exception) {
//            Log.e("<>", "showExpandableView: ", e)
            return false
        }

        return true
    }

    fun removeExpandableView() {
        expandableView?.remove()

        if (currentRoute == Route.ExpandableView) {
            currentRoute = Route.Empty
        }
    }

    /**
     * remove all views
     * */
    fun removeAllViews() {

        removeExpandableView()
        removeBubbles()

        currentRoute = Route.Empty

    }

    //endregion

    //region Notification --------------------------------------------------------------------------

    open fun channelId() = "bubble_service"
    open fun channelName() = "floating bubble"

    open fun notificationId() = 101

    /**
     * show the notification or update if already exists
     * */
    fun notify(notification: Notification) {

        if (isNotificationInitialized) {
            NotificationManagerCompat.from(this).notify(notificationId(), notification)
            return
        }

        if (isAndroid8OrHigher()) {
            createNotificationChannel(channelId(), channelName())
        }

        startForeground(notificationId(), notification)

        isNotificationInitialized = true

    }

    open fun initialNotification(): Notification? {
        return NotificationCompat.Builder(this, channelId())
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_rounded_blue_diamond)
            .setContentTitle("bubble is running")
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setSilent(true)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    open fun createNotificationChannel(
        channelId: String,
        channelName: String
    ) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT // IMPORTANCE_NONE recreate the notification if update
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.createNotificationChannel(channel)
    }

    //endregion

    //region Interface implementation --------------------------------------------------------------

    private val customExpandableViewListener = object : ExpandableView.Action {
        override fun popToBubble() {
            removeExpandableView()
            showBubbles()
        }
    }

    private val customFloatingBubbleServiceInteractor = object : FloatingBubble.ServiceInteractor {
        override fun requestStop() {
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

    //region Builder ---------------------------------------------------------------------

    open fun initialRoute(): Route = Route.Bubble

    abstract fun setupBubble(action: FloatingBubble.Action): FloatingBubble.Builder

    open fun setupExpandableView(action: ExpandableView.Action): ExpandableView.Builder? = null

    //endregion


    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    private fun isAndroid8OrHigher() = Build.VERSION.SDK_INT >= AndroidVersions.`8`

}