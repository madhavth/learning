package com.madhavth.firebaselearning.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.RemoteViews
import android.widget.Toast
import com.madhavth.firebaselearning.CanvasActivity
import com.madhavth.firebaselearning.R
import com.madhavth.firebaselearning.Widgets.ACTION_STOP_VIEW
import kotlinx.android.synthetic.main.activity_canvas.view.*
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.min


class OverlayService: Service() {
    var minimized: Boolean = false
    var mx: Float = 0f
    var my: Float = 0f
    var mx2: Float = 0f
    var my2: Float = 0f


    override fun onCreate() {
        super.onCreate()

        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val overlay = layoutInflater.inflate(R.layout.activity_canvas,null)

        overlay.alpha = 0.8f

        overlay.btnClear.setOnClickListener {
            overlay.myCanvas.clearBitmap()
        }

        overlay.btnSearch.text = "close"
        overlay.btnSearch.setOnClickListener {
            windowManager.removeView(overlay)
            stopSelf()
        }


        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            0,
            PixelFormat.TRANSLUCENT
        )

        overlay.btnSave.text = "minimize"
        overlay.btnSave.setOnClickListener {
            if(!minimized)
            {
                params.height = 200
                params.width  = 200
                windowManager.updateViewLayout(overlay, params)
                overlay.btnSave.text= "maximize"
            }
            else
            {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
                params.width = WindowManager.LayoutParams.WRAP_CONTENT
                windowManager.updateViewLayout(overlay, params)
                overlay.btnSave.text ="minimize"
            }
            minimized = !minimized
        }

        overlay.btnMoveMe.setOnTouchListener(View.OnTouchListener{
         v,event ->
            Timber.tag("ConstantLog").d("ACTION ${event.action}")
            if(event.action == MotionEvent.ACTION_MOVE)
            {
                val dx = abs(mx - event.x)
                val dy = abs(my - event.y)

                if(dx >= 8f || dy >= 8f)
                {
                    Timber.tag("ConstantLog").d("moved to ${event.x}, ${event.y}")
                    overlay.btnMoveMe.x += event.x
                    overlay.btnMoveMe.y += event.y
                    Timber.tag("ConstantLog").d("btnMoveMe to ${overlay.btnMoveMe.x}, ${overlay.btnMoveMe.y}")
                }

                return@OnTouchListener true
            }

            if(event.action == MotionEvent.ACTION_DOWN)
            {
                mx = event.x
                my = event.y
            }

            if(event.action == MotionEvent.ACTION_UP)
            {
             val sth =4
            }

            false
        })


        windowManager.addView(overlay, params)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action)
        {
            ACTION_STOP_VIEW -> {
                Toast.makeText(this
                    , "removing view",
                    Toast.LENGTH_SHORT).show()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}