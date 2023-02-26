package com.torrydo.testfloatingbubble

import android.app.Notification
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.torrydo.floatingbubbleview.ExpandableView
import com.torrydo.floatingbubbleview.FloatingBubble
import com.torrydo.floatingbubbleview.FloatingBubbleService

class MyServiceKt : FloatingBubbleService() {


    // for android 8 and up
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
            // set bubble icon attributes, currently only drawable and bitmap are supported
            .bubble(R.drawable.ic_rounded_blue_diamond, 60, 60)
            // set style for bubble, by default bubble use fade animation
            .bubbleStyle(null)
            // set start location of bubble, (x=0, y=0) is top-left
            .startLocation(0, 0)
            // enable auto animate bubble to the left/right side when release, true by default
            .enableAnimateToEdge(true)
            // set close-bubble icon attributes, currently only drawable and bitmap are supported
            .closeBubble(R.drawable.ic_remove_icon, 60, 60)
            // set style for close-bubble, null by default
            .closeBubbleStyle(null)
            // show close-bubble, true by default
            .enableCloseBubble(true)
            // enable bottom background, false by default
            .bottomBackground(false)
            .addFloatingBubbleListener(object : FloatingBubble.Listener {
                override fun onDestroy() {}
                override fun onClick() {
                    action.navigateToExpandableView() // must override `setupExpandableView`, otherwise throw an exception
                }
                override fun onMove(x: Int, y: Int) {}
                override fun onUp(x: Int, y: Int) {}
                override fun onDown(x: Int, y: Int) {}
            })
            .opacity(1f)
    }

    override fun setupExpandableView(action: ExpandableView.Action): ExpandableView.Builder? {

        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.layout_view_test, null)

        layout.findViewById<View>(R.id.card_view).setOnClickListener { v: View? ->
            Toast.makeText(this, "hello from card view from kotlin", Toast.LENGTH_SHORT).show();
            action.popToBubble()
        }
        return ExpandableView.Builder(this)
            .expandableView(layout)
            .dimAmount(0.8f)
            .addExpandableViewListener(object : ExpandableView.Listener {
                override fun onOpenExpandableView() {}
                override fun onCloseExpandableView() {}
            })
            .expandableViewStyle(null)
    }

}