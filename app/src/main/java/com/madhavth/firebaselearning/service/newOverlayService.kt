package com.madhavth.firebaselearning.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.LayoutInflater
import android.view.WindowManager
import com.madhavth.firebaselearning.R

class NewOverlayService: Service() {

    override fun onCreate() {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        val myView= LayoutInflater.from(this).inflate(R.layout.activity_lock_screen,null)
        windowManager.addView(myView, layoutParams)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}