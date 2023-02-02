package com.torrydo.testfloatingbubble.java_sample;

import android.app.Notification;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.torrydo.floatingbubbleview.ExpandableView;
import com.torrydo.floatingbubbleview.FloatingBubble;
import com.torrydo.floatingbubbleview.FloatingBubbleService;
import com.torrydo.testfloatingbubble.R;


public class MyService extends FloatingBubbleService {

    @Override
    public boolean enableLogger() {
        return true;
    }

    // for android 8 and up
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
    public FloatingBubble.Builder setupBubble(@NonNull FloatingBubble.Action action) {

        return new FloatingBubble.Builder(this)
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

                .addFloatingBubbleListener(new FloatingBubble.Listener() {
                    @Override
                    public void onDestroy() {}

                    @Override
                    public void onClick() {
                        action.navigateToExpandableView(); // must override `setupExpandableView`, otherwise throw an exception
                    }

                    @Override
                    public void onMove(int x, int y) {}

                    @Override
                    public void onUp(int x, int y) {}

                    @Override
                    public void onDown(int x, int y) {}
                })
                .opacity(1f);
    }

    @Nullable
    @Override
    public ExpandableView.Builder setupExpandableView(@NonNull ExpandableView.Action action) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_view_test, null);


        layout.findViewById(R.id.card_view).setOnClickListener(v -> {
//            Toast.makeText(this, "hello from card view from java", Toast.LENGTH_SHORT).show();
            action.popToBubble();
        });

        return new ExpandableView.Builder(this)
                .expandableView(layout)
                .dimAmount(0.8f)
                .addExpandableViewListener(new ExpandableView.Listener() {
                    @Override
                    public void onOpenExpandableView() {
                        Log.d("<>", "onOpenFloatingView: ");
                    }

                    @Override
                    public void onCloseExpandableView() {
                        Log.d("<>", "onCloseFloatingView: ");
                    }
                }).expandableViewStyle(null)
                ;
    }
}
