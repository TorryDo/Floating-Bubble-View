package com.torrydo.testfloatingbubble.java_sample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.torrydo.testfloatingbubble.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

//        ExpandableLayout layout = new ExpandableLayout();

        button.setOnClickListener(v ->
                {

//                    Intent intent = new Intent(this, MyService.class);

//                    intent.putExtra("key1", "word");

//                    if (FloatingBubbleService.isRunning()) {
//                        stopMyService(intent);
//                    } else {
//                        startMyService(intent);
//                    }
                }
        );
    }

    private void stopMyService(Intent intent) {
        stopService(intent);
        Toast.makeText(this, "stop service", Toast.LENGTH_SHORT).show();
    }

    private void startMyService(Intent intent) {
        finish();
        ContextCompat.startForegroundService(this, intent);
    }

}

