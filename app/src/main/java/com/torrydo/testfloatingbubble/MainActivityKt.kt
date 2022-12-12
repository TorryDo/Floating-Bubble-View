package com.torrydo.testfloatingbubble

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.torrydo.floatingbubbleview.FloatingBubbleService

class MainActivityKt : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {

            val intent = Intent(this, MyServiceKt::class.java)

            if (FloatingBubbleService.isRunning()) {
                stopMyService(intent)
            } else {
                startMyService(intent)
            }

        }
    }

    private fun stopMyService(intent: Intent) {
        stopService(intent)
        Toast.makeText(this, "stop service", Toast.LENGTH_SHORT).show()
    }

    private fun startMyService(intent: Intent) {
        finish()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

}