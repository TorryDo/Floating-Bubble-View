package com.torrydo.testfloatingbubble;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.torrydo.floatingbubbleview.FloatingBubbleService;
import com.torrydo.floatingbubbleview.main.FloatingBubbleBuilder;
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableViewBuilder;
import com.torrydo.floatingbubbleview.main.layout_view.ExpandableViewEvent;
import com.torrydo.floatingbubbleview.physics.FloatingBubbleTouchListener;

public class MyService extends FloatingBubbleService {


    @NonNull
    @Override
    public FloatingBubbleBuilder setupBubble() {


        return new FloatingBubbleBuilder()
                .with(this)
                .setIcon(R.drawable.ic_star)
                .setRemoveIcon(R.mipmap.ic_launcher_round)
                .addFloatingBubbleTouchListener(new FloatingBubbleTouchListener(){
                    @Override
                    public void onDestroy() {
                        System.out.println("<><> on destroy on java");
                    }

                    @Override
                    public void onClick() {
                        System.out.println("<><> on click on java");
                    }

                    @Override
                    public void onMove(int x, int y) {
                        System.out.println("<><> on move from java");
                    }

                    @Override
                    public void onUp(int x, int y) {
                        System.out.println("<><> on up from java");
                    }

                    @Override
                    public void onDown(int x, int y) {
                        System.out.println("<><> on down from java");
                    }
                })
                .setBubbleSizeDp(60)
                .setStartPoint(-200, 0)
                .setAlpha(1f);
    }


    @NonNull
    @Override
    public ExpandableViewBuilder setupExpandableView(ExpandableViewEvent event) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_view_test, null);


        layout.findViewById(R.id.card_view).setOnClickListener(v -> {
            Toast.makeText(this, "hello from card view from java", Toast.LENGTH_SHORT).show();
            event.popToBubble();
        });

        return new ExpandableViewBuilder()
                .with(this)
                .setExpandableView(layout);
    }
}
