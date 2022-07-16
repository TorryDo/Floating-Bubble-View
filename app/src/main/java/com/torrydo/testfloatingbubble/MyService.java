package com.torrydo.testfloatingbubble;

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


public class MyService extends FloatingBubbleService {

    // for android 8 and above
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

        return new FloatingBubble.Builder()
                .with(this)
                .setIcon(R.drawable.ic_rounded_blue_diamond)
                .setRemoveIcon(R.drawable.ic_remove_icon)
                .addFloatingBubbleTouchListener(new FloatingBubble.TouchEvent() {
                    @Override
                    public void onDestroy() {
//                        System.out.println("on Destroy");
                    }

                    @Override
                    public void onClick() {
                        action.navigateToExpandableView();
                    }

                    @Override
                    public void onMove(int x, int y) {
//                        System.out.println("onMove");
                    }

                    @Override
                    public void onUp(int x, int y) {
//                        System.out.println("onUp");
                    }

                    @Override
                    public void onDown(int x, int y) {
//                        System.out.println("onDown");
                    }
                })
                .setBubbleSizeDp(60)
                .setStartPoint(-200, 0)
                .setAlpha(1f);
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


        return new ExpandableView.Builder()
                .with(this)
                .setExpandableView(layout)
                .setDimAmount(0.8f)
                .addExpandableViewListener(new ExpandableView.Action() {
                    @Override
                    public void popToBubble() {
                        this.popToBubble();
                    }

                    @Override
                    public void onOpenExpandableView() {
                        Log.d("<>", "onOpenFloatingView: ");
                    }

                    @Override
                    public void onCloseExpandableView() {
                        Log.d("<>", "onCloseFloatingView: ");
                    }
                });
    }
}
