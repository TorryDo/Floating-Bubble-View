package com.torrydo.testfloatingbubble.java_sample;

import android.app.Notification;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.torrydo.floatingbubbleview.BubbleBehavior;
import com.torrydo.floatingbubbleview.ExpandableView;
import com.torrydo.floatingbubbleview.FloatingBubble;
import com.torrydo.floatingbubbleview.FloatingBubbleService;
import com.torrydo.floatingbubbleview.Route;
import com.torrydo.testfloatingbubble.R;

import kotlin.NotImplementedError;


public class MyService extends FloatingBubbleService {

    String data = null;

    /**
     * Sets up a notification for Bubble on Android 8 and up.
     *
     * @param channelId The ID of the notification channel.
     * @return The notification instance.
     */
    @NonNull
    @Override
    public Notification setupNotificationBuilder(@NonNull String channelId) {
        return new NotificationCompat.Builder(this, channelId)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_rounded_blue_diamond)
                .setContentTitle("bubble is running")
                .setContentText("click to do nothing")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
    }

    @NonNull
    @Override
    public String channelId() {
        return "you_channel_id";
    }

    @NonNull
    @Override
    public String channelName() {
        return "your channel name";
    }

    @Override
    public int notificationId() {
        return super.notificationId();
    }

    @NonNull
    @Override
    public Route initialRoute() {
        return Route.Empty;
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        assert intent != null;
        this.data = intent.getStringExtra("key1");

        if (this.data != null) {
            try {
//                this.showExpandableView();
                //TODO()

            } catch (NotImplementedError e) {
                throw new RuntimeException(e);
            }
            return START_STICKY;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @NonNull
    @Override
    public FloatingBubble.Builder setupBubble(@NonNull FloatingBubble.Action action) {

        Log.d("<>", "data = " + data);

        return new FloatingBubble.Builder(this)
                // set bubble icon attributes, currently only drawable and bitmap are supported
                .bubble(R.drawable.ic_rounded_blue_diamond, 60, 60)

                // set style for bubble, fade animation by default
                .bubbleStyle(null)

                // set start location of bubble, (x=0, y=0) is the top-left
                .startLocation(0, 0)

                // enable auto animate bubble to the left/right side when release, true by default
                .enableAnimateToEdge(true)

                // set close-bubble icon attributes, currently only drawable and bitmap are supported
                .closeBubble(R.drawable.ic_close_bubble, 60, 60)

                // set style for close-bubble, null by default
                .closeBubbleStyle(null)

                // show close-bubble, true by default
                .enableCloseBubble(true)

                // the more value (dp), the larger closeable-area
                .distanceToClose(100)

                // choose behavior of the bubbles
                // DYNAMIC_CLOSE_BUBBLE: close-bubble moving based on the bubble's location
                // FIXED_CLOSE_BUBBLE: bubble will automatically move to the close-bubble when it reaches the closable-area
                .behavior(BubbleBehavior.DYNAMIC_CLOSE_BUBBLE)

                // enable bottom background, false by default
                .bottomBackground(false)

                // add listener for the bubble
                .addFloatingBubbleListener(new FloatingBubble.Listener() {
                    @Override
                    public void onClick() {
                        action.navigateToExpandableView(); // must override `setupExpandableView`, otherwise throw an exception
                    }

                    @Override
                    public void onMove(float x, float y) {} // The location of the finger on the screen which triggers the movement of the bubble.

                    @Override
                    public void onUp(float x, float y) {} // ..., when finger release from bubble

                    @Override
                    public void onDown(float x, float y) {} // ..., when finger tap the bubble
                })

                // set bubble's opacity
                .opacity(1f);
    }

    @Nullable
    @Override
    public ExpandableView.Builder setupExpandableView(@NonNull ExpandableView.Action action) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_view_test, null);

        layout.findViewById(R.id.card_view).setOnClickListener(v -> {
            Toast.makeText(this, "hello from card view from java", Toast.LENGTH_SHORT).show();
            action.popToBubble();
        });

        return new ExpandableView.Builder(this)

                // set view to expandable-view. Jetpack Compose is not available in Java
                .view(layout)

                // set the amount of dimming below the view.
                .dimAmount(0.8f)

                // apply style for the expandable-view
                .expandableViewStyle(null)

                .addExpandableViewListener(new ExpandableView.Listener() {
                    @Override
                    public void onOpenExpandableView() {}

                    @Override
                    public void onCloseExpandableView() {}
                });
    }
}
