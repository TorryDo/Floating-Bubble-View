package com.torrydo.testfloatingbubble

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainKt: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, MyService::class.java)

        startService(intent)
//        startForegroundService(intent)

    }

}