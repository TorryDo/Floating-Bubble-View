package com.torrydo.testfloatingbubble

import android.app.Notification
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.torrydo.floatingbubbleview.ExpandableView
import com.torrydo.floatingbubbleview.FloatingBubble
import com.torrydo.floatingbubbleview.FloatingBubbleService

class MyServiceKt : FloatingBubbleService() {

    override fun setupNotificationBuilder(channelId: String): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_rounded_blue_diamond)
            .setContentTitle("bubble is running")
            .setContentText("click to do nothing")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }

    override fun setupBubble(action: FloatingBubble.Action): FloatingBubble.Builder {
        return FloatingBubble.Builder(this)
            .setBubble(R.drawable.ic_rounded_blue_diamond)
            .setBubbleSizeDp(60, 60)
            .setCloseBubble(R.drawable.ic_remove_icon)
            .addFloatingBubbleTouchListener(object : FloatingBubble.TouchEvent {
                override fun onDestroy() {
                    println("on Destroy")
                }

                override fun onClick() {
                    action.navigateToExpandableView();
                }

                override fun onMove(x: Int, y: Int) {
//                    println("onMove")
                }

                override fun onUp(x: Int, y: Int) {
                    println("onUp")
                }

                override fun onDown(x: Int, y: Int) {
                    println("onDown")
                }
            })

            .setStartPoint(-200, 0)
            .setAlpha(1f)
            .enableCloseIcon(false)

    }

    override fun setupExpandableView(action: ExpandableView.Action): ExpandableView.Builder {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val layout = inflater.inflate(R.layout.layout_view_test, null)

        layout.findViewById<View>(R.id.card_view).setOnClickListener { view ->
            Toast.makeText(this, "hello from card view from kotlin", Toast.LENGTH_SHORT).show();
            action.popToBubble()
        }
        return ExpandableView.Builder(this)
            .setExpandableView(layout)
            .setDimAmount(0.8f)
            .addExpandableViewListener(object : ExpandableView.Action {
                override fun popToBubble() {
                    action.popToBubble()
                }

                override fun onOpenExpandableView() {
                    super.onOpenExpandableView()
                }

                override fun onCloseExpandableView() {
                    super.onCloseExpandableView()
                }
            })
    }

}