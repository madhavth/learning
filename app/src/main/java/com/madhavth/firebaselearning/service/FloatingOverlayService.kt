package com.madhavth.firebaselearning.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.widget.Toast
import com.madhavth.firebaselearning.R
import kotlinx.android.synthetic.main.floating_layout.view.*
import timber.log.Timber

class FloatingOverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    var mx = 0f
    var my = 0f

    var my2 = 0f
    var mx2 = 0f


    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val displayMetrics = this.resources.displayMetrics
        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        val pxHeight = displayMetrics.heightPixels
        val pxWidth = displayMetrics.widthPixels


        floatingView = layoutInflater.inflate(R.layout.floating_layout, null)

        val windowParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        windowManager.addView(floatingView, windowParams)


        var updatedLayoutParams = windowParams

        floatingView.btnFloat.setOnClickListener {
            Toast.makeText(applicationContext
            , "Float baby float",
            Toast.LENGTH_SHORT).show()
        }

        floatingView.btnFloat.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mx = updatedLayoutParams.x.toFloat()
                    my = updatedLayoutParams.y.toFloat()

                    mx2 = event.rawX
                    my2 = event.rawY
                }

                MotionEvent.ACTION_UP -> {
                    if (updatedLayoutParams.x >= dpWidth)
                        updatedLayoutParams.x = dpWidth.toInt()


                    if (updatedLayoutParams.y >= dpHeight)
                        updatedLayoutParams.y = dpHeight.toInt()

                    windowManager.updateViewLayout(floatingView,updatedLayoutParams)

                    Timber.tag("heightWidthw")
                        .d("current x,y for view is ${updatedLayoutParams.x}, ${updatedLayoutParams.y}")
                    Timber.tag("heightWidthw").d("dpHeight, width are $dpHeight, $dpWidth")
                    Timber.tag("heightWidthw").d("pxHeight, width are $pxHeight, $pxWidth")
                    Timber.tag("heightWidthw")
                        .d("event x,y ${event.x}, ${event.y} and raw ${event.rawX}, ${event.rawY}")

                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = (mx2 - mx)
                    val newY = (my2 - my)

                    updatedLayoutParams.x = (mx + event.rawX - mx2).toInt()
                    updatedLayoutParams.y = (my + event.rawY - my2).toInt()

                    Timber.tag("paramsCheck")
                        .d("event x,y - ${event.x}, ${event.y}\nnewX,newY - $newX, $newY")
                    windowManager.updateViewLayout(floatingView, updatedLayoutParams)
                }
            }

            false
        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(floatingView)
    }
}