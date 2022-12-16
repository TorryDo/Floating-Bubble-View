package com.torrydo.testfloatingbubble.java_sample;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.torrydo.floatingbubbleview.ExpandableView;
import com.torrydo.floatingbubbleview.FloatingBubble;
import com.torrydo.floatingbubbleview.FloatingBubbleService;
import com.torrydo.testfloatingbubble.R;


public class MyService extends FloatingBubbleService {

    @Override
    public boolean enableLogger() {
        return true;
    }

    // for android 8 and above
//    @NonNull
//    @Override
//    public Notification setupNotificationBuilder(@NonNull String channelId) {
//        return new NotificationCompat.Builder(this, channelId)
//                .setOngoing(true)
//                .setSmallIcon(R.drawable.ic_rounded_blue_diamond)
//                .setContentTitle("bubble is running")
//                .setContentText("click to do nothing")
//                .setPriority(NotificationCompat.PRIORITY_MIN)
//                .setCategory(Notification.CATEGORY_SERVICE)
//                .build();
//    }


    @NonNull
    @Override
    public FloatingBubble.Builder setupBubble(@NonNull FloatingBubble.Action action) {

        return new FloatingBubble.Builder(this)
                .setBubble(R.drawable.ic_rounded_blue_diamond)
                .setCloseBubble(R.drawable.ic_remove_icon)
                .setBubbleSizeDp(60, 60)
//                .setBubbleStyle(null)
//                .setCloseBubbleStyle(R.style.default_close_bubble_style)
                .setCloseBubbleSizeDp(80,80)
                .addFloatingBubbleTouchListener(new FloatingBubble.TouchEvent() {
                    @Override
                    public void onDestroy() {
                        System.out.println("on Destroy");
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
                        System.out.println("onUp");
                    }

                    @Override
                    public void onDown(int x, int y) {
                        System.out.println("onDown");
                    }
                })

                .setStartPoint(0, 0) // half-screen-width, half-screen-height
                .setAlpha(1f)
//                .enableAnimateToEdge(false)
//                .enableCloseIcon(false)
                ;
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
                .setExpandableView(layout)
                .setDimAmount(0.8f)
//                .setExpandableViewStyle(null)
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
