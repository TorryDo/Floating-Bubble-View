package com.torrydo.testfloatingbubble.java_sample;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import com.torrydo.floatingbubbleview.CloseBubbleBehavior;
import com.torrydo.floatingbubbleview.FloatingBubbleListener;
import com.torrydo.floatingbubbleview.helper.ViewHelper;
import com.torrydo.floatingbubbleview.service.expandable.BubbleBuilder;
import com.torrydo.floatingbubbleview.service.expandable.ExpandableBubbleService;
import com.torrydo.floatingbubbleview.service.expandable.ExpandedBubbleBuilder;
import com.torrydo.testfloatingbubble.R;

public class MyServiceJava extends ExpandableBubbleService {

//    @Override
//    public void startNotificationForeground() {
//        startForeground();
//    }

    @Override
    public void onCreate() {
        super.onCreate();

        minimize();
    }

    @Nullable
    @Override
    public BubbleBuilder configBubble() {
        View imgView = ViewHelper.fromDrawable(this, R.drawable.ic_rounded_blue_diamond, 60, 60);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expand();
            }
        });
        return new BubbleBuilder(this)
                .bubbleView(imgView)
                .bubbleStyle(R.style.default_bubble_style)
                .bubbleDraggable(true)
                .forceDragging(true)
                .closeBubbleView(ViewHelper.fromDrawable(this, R.drawable.ic_close_bubble))
                .closeBubbleStyle(R.style.default_close_bubble_style)
                .distanceToClose(100)
                .closeBehavior(CloseBubbleBehavior.FIXED_CLOSE_BUBBLE)
                .startLocation(100, 100)
                .enableAnimateToEdge(true)
                .bottomBackground(false)
                .addFloatingBubbleListener(new FloatingBubbleListener() {
                    @Override
                    public void onFingerDown(float x, float y) {}
                    @Override
                    public void onFingerUp(float x, float y) {}
                    @Override
                    public void onFingerMove(float x, float y) {}
                })
                ;

    }

    @Nullable
    @Override
    public ExpandedBubbleBuilder configExpandedBubble() {
        View expandedView = LayoutInflater.from(this).inflate(R.layout.layout_view_test, null);

        expandedView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minimize();
            }
        });
        return new ExpandedBubbleBuilder(this)
                .expandedView(expandedView)
                .startLocation(0, 0)
                .draggable(true)
                .style(R.style.default_bubble_style)
                .fillMaxWidth(true)
                .enableAnimateToEdge(true)
                .dimAmount(0.5f);
    }
}
