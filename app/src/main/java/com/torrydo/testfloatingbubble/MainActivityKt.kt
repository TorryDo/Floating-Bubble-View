package com.torrydo.testfloatingbubble

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivityKt : AppCompatActivity() {

    companion object{
        var isVisible = false
        var isBubbleRunning = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {

            if (isBubbleRunning) {
                stopMyService()
            } else {
                // Check if the application has draw over other apps permission, and request it if not
                if (!Settings.canDrawOverlays(this)) {
                    startActivity(
                        Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:$packageName")
                        )
                    )
                } else {
                    val intent = Intent(this, MyServiceKt::class.java)
                    intent.putExtra("size", 60)
                    intent.putExtra("noti_message", "HELLO FROM MAIN ACT")
                    ContextCompat.startForegroundService(this, intent)
                    isVisible = false
                }
            }

            isBubbleRunning = !isBubbleRunning
        }
    }

//    override fun onStop() {
//        super.onStop()
//        if(FloatingBubbleService.isRunning() && FloatingBubbleReceiver.isEnabled && isVisible.not()){
//            val intent = Intent(this, MyServiceKt::class.java)
//            intent.putExtra("route", Route.ExpandableView.name)
//            ContextCompat.startForegroundService(this, intent)
//        }
//
//    }

//    override fun onStart() {
//        super.onStart()
//        if(FloatingBubbleService.isRunning()){
//            val intent = Intent(this, MyServiceKt::class.java)
//            intent.putExtra("route", Route.Empty.name)
//            ContextCompat.startForegroundService(this, intent)
//
//            isVisible = false
//        }
//
//    }


    private fun stopMyService() {
        val intent = Intent(this, MyServiceKt::class.java)
        stopService(intent)
        Toast.makeText(this, "stop service", Toast.LENGTH_SHORT).show()
    }

}