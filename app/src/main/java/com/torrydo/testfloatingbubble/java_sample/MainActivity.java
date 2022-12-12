package com.torrydo.testfloatingbubble.java_sample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.torrydo.floatingbubbleview.FloatingBubbleService;
import com.torrydo.testfloatingbubble.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(v ->
                {
                    Intent intent = new Intent(this, MyService.class);

                    if (FloatingBubbleService.isRunning()) {
                        stopMyService(intent);
                    } else {
                        startMyService(intent);
                    }
                }
        );
    }

    private void stopMyService(Intent intent){
        stopService(intent);
        Toast.makeText(this, "stop service", Toast.LENGTH_SHORT).show();
    }

    private void startMyService(Intent intent){
        finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

}

