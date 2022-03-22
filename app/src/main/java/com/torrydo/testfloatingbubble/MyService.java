package com.torrydo.testfloatingbubble;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.torrydo.floatingbubbleview.ExpandableView;
import com.torrydo.floatingbubbleview.FloatingBubble;
import com.torrydo.floatingbubbleview.FloatingBubbleService;


public class MyService extends FloatingBubbleService {

    @NonNull
    @Override
    public FloatingBubble.Builder setupBubble() {


        return new FloatingBubble.Builder()
                .with(this)
                .setIcon(R.drawable.ic_rounded_blue_diamond)
                .setRemoveIcon(R.drawable.ic_remove_icon)
//                .addFloatingBubbleTouchListener(new FloatingBubble.TouchEvent() {
//                    @Override
//                    public void onDestroy() { System.out.println("on Destroy"); }
//
//                    @Override
//                    public void onClick() { System.out.println("onClick"); }
//
//                    @Override
//                    public void onMove(int x, int y) { System.out.println("onMove"); }
//
//                    @Override
//                    public void onUp(int x, int y) { System.out.println("onUp"); }
//
//                    @Override
//                    public void onDown(int x, int y) { System.out.println("onDown"); }
//                })
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
            Toast.makeText(this, "hello from card view from java", Toast.LENGTH_SHORT).show();
            action.popToBubble();
        });

        return new ExpandableView.Builder()
                .with(this)
                .setExpandableView(layout)
                .setDimAmount(0.8f);
    }


}
