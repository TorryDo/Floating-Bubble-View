package com.torrydo.testfloatingbubble;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.torrydo.floatingbubbleview.FloatingBubbleService;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        if(FloatingBubbleService.isRunning()){
            // println()
        }

        button.setOnClickListener(v ->
                {
                    if (FloatingBubbleService.isRunning()) {
                        this.stopService(new Intent(this, MyService.class));
                    } else {
                        finish();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(new Intent(this, MyService.class));
                        } else {
                            startService(new Intent(this, MyService.class));
                        }
                    }
                }
        );

    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}

