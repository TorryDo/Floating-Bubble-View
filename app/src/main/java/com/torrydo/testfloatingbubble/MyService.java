package com.torrydo.testfloatingbubble;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.torrydo.floatingbubbleview.ExpandableView;
import com.torrydo.floatingbubbleview.ExpandableViewBuilder;
import com.torrydo.floatingbubbleview.FloatingBubbleBuilder;
import com.torrydo.floatingbubbleview.FloatingBubbleService;


public class MyService extends FloatingBubbleService {

    @NonNull
    @Override
    public FloatingBubbleBuilder setupBubble() {

        return new FloatingBubbleBuilder()
                .with(this)
                .setIcon(R.drawable.ic_star)
                .setRemoveIcon(R.mipmap.ic_launcher_round)

                .setBubbleSizeDp(60)
                .setStartPoint(-200, 0)
                .setAlpha(1f);
    }

    @NonNull
    @Override
    public ExpandableViewBuilder setupExpandableView(@NonNull ExpandableView.Event event) {
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
