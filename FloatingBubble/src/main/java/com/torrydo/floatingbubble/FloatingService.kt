package com.torrydo.floatingbubble

import android.app.Service
import android.content.Intent
import android.os.IBinder

class FloatingService : Service() {

    override fun onCreate() {
        super.onCreate()
    }


    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}