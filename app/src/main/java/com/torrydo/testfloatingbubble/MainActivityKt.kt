package com.torrydo.testfloatingbubble

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.torrydo.floatingbubbleview.FloatingBubbleService
import com.torrydo.floatingbubbleview.Route

class MainActivityKt : AppCompatActivity() {

    companion object{
        var isVisible = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {

            if (FloatingBubbleService.isRunning()) {
                stopMyService()
            } else {
                val intent = Intent(this, MyServiceKt::class.java)
                intent.putExtra("size", 60)
                ContextCompat.startForegroundService(this, intent)
                isVisible = false
            }

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