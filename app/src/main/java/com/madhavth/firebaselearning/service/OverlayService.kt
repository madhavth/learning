package com.madhavth.firebaselearning.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.madhavth.firebaselearning.R
import com.madhavth.firebaselearning.Widgets.ACTION_STOP_VIEW
import kotlinx.android.synthetic.main.activity_canvas.view.*
import timber.log.Timber
import kotlin.math.abs


class OverlayService: Service() {
    var minimized: Boolean = false
    var mx: Float = 0f
    var my: Float = 0f

    var my2: Float = 0f
    var mx2: Float = 0f

    var minX: Float = 0f
    var minY: Float = 0f


    var windowX: Float = 0f
    var windowY: Float = 0f

    var winRX: Float = 0f
    var winRY: Float = 0f

    var btnWidth = 0
    var btnHeight = 0

    override fun onCreate() {
        super.onCreate()

        val displayMetrics = this.resources.displayMetrics
        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        val pxHeight = displayMetrics.heightPixels
        val pxWidth = displayMetrics.widthPixels

        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val overlay = layoutInflater.inflate(R.layout.activity_canvas2,null)


        overlay.txtView.textSize = 50f

        overlay.btnMoveMe.layoutParams.width = 0
        overlay.alpha = 0.8f

        overlay.imgView.layoutParams.width = resources.displayMetrics.widthPixels
        overlay.imgView.layoutParams.height = resources.displayMetrics.heightPixels/5

        overlay.btnClear.setOnClickListener {
            overlay.myCanvas.clearBitmap()
        }

        overlay.btnClose.text = "close"
        overlay.btnClose.setOnClickListener {
            windowManager.removeView(overlay)
            stopSelf()
        }


        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.x = 0
        params.y = 0

        params.width = (pxWidth/1.5).toInt()
        params.height = pxHeight/2

        overlay.btnMinimize.text = "Minimize"
        overlay.btnMinimize.setOnClickListener {
            Timber.d("btnSave onCLick")
            if(!minimized)
            {
                overlay.txtView.textSize = 15f
                params.height = pxHeight/11
                params.width  = pxWidth * 2/7

                if(params.x >=0)
                    params.x  = pxWidth/2
                else
                    params.x = -pxWidth/2

                overlay.btnFullScreen.layoutParams.height = 0
                overlay.btnFullScreen.layoutParams.width = 0

                windowManager.updateViewLayout(overlay, params)
                overlay.btnMinimize.text= "Maximize"
            }
            else
            {
                overlay.txtView.textSize = 50f
                overlay.btnFullScreen.layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
                overlay.btnFullScreen.layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT

                params.width = (pxWidth/1.5).toInt()
                params.height = pxHeight/2
                params.x = 0
                windowManager.updateViewLayout(overlay, params)
                overlay.btnMinimize.text ="minimize"
            }
            minimized = !minimized
        }


        overlay.btnMoveMe.setOnTouchListener {
         v,event ->
            Timber.tag("ConstantLog").d("ACTION ${MotionEvent.actionToString(event.action)}")
            if(event.action == MotionEvent.ACTION_MOVE)
            {
                val dx = abs(mx - event.x)
                val dy = abs(my - event.y)

                if(dx >= 8f || dy >= 8f)
                {
                    Timber.tag("ConstantLog").d("moved to ${event.x}, ${event.y}")
                    overlay.btnMoveMe.x = mx2 + event.rawX -mx
                    overlay.btnMoveMe.y = my2 + event.rawY -my
                    Timber.tag("ConstantLog").d("btnMoveMe to ${overlay.btnMoveMe.x}, ${overlay.btnMoveMe.y}")
                }

            }

            if(event.action == MotionEvent.ACTION_DOWN)
            {
                mx = event.rawX
                my = event.rawY

                mx2 = overlay.btnMoveMe.x
                my2 = overlay.btnMoveMe.y
            }

            if(event.action == MotionEvent.ACTION_UP)
            {
                overlay.btnMoveMe.apply {
                    if(x >= overlay.width)
                        x = overlay.width.toFloat() - overlay.btnMoveMe.width
                    else if (x <= 0)
                         x = overlay.btnMoveMe.width.toFloat()

                    if(y >= overlay.height)
                        y = overlay.height.toFloat()  - overlay.btnMoveMe.height
                    else if (y<=0)
                        y =  overlay.btnMoveMe.height.toFloat()

                    Timber.d("action up ${overlay.width} and ${overlay.height} and event x,y are ${overlay.btnMoveMe.x}, ${overlay.btnMoveMe.y}")
                }
            }

            false
        }

        Timber.tag("DisplayMetrics").d("dpHeight, width are $dpHeight, $dpWidth and \n px height, width are ${displayMetrics.heightPixels}, ${displayMetrics.widthPixels}")


        overlay.imgView.setOnTouchListener{
            v,event ->
            when(event.action)
            {
                MotionEvent.ACTION_UP -> {
                    Timber.d("imgView Up")
                    Timber.d("rawX, rawY are ${event.rawY}, ${event.rawY}")
                    Timber.d("params X and Y are ${params.x}, ${params.y}")

                    if(params.x >= pxWidth/2)
                        params.x = pxWidth/2

                    else if(params.x <= -pxWidth/2)
                        params.x = -pxWidth/2


                    if(params.y >= pxHeight/2)
                        params.y =pxHeight/2

                    else if(params.y <= -pxHeight/2)
                        params.y = -pxHeight/2

                    windowManager.updateViewLayout(overlay, params)
                }

                MotionEvent.ACTION_DOWN -> {
                    Timber.d("imgView Down")
                    windowX = params.x.toFloat()
                    windowY = params.y.toFloat()

                    winRX = event.rawX
                    winRY = event.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    Timber.d("imgView Move")

                    params.x = (windowX + event.rawX - winRX).toInt()
                    params.y = (windowY + event.rawY - winRY).toInt()

                    windowManager.updateViewLayout(overlay, params)
                }
            }
            return@setOnTouchListener true
        }

        overlay.btnFullScreen.setOnClickListener {
            params.width = pxWidth
            params.height = pxHeight * 9/10
            params.y = -pxHeight/2
            windowManager.updateViewLayout(overlay, params)
        }

        windowManager.addView(overlay, params)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        try{
            val data1 = intent?.extras?.getString("firebasemessage1")
            val data2 = intent?.extras?.getString("firebasemessage2")

            Toast.makeText(this
            , "data1, data2 are $data1, $data2",
            Toast.LENGTH_LONG).show()
        }
        finally {

        }

        when(intent?.action)
        {
            ACTION_STOP_VIEW -> {
                Toast.makeText(this
                    , "stopping service click close to finish",
                    Toast.LENGTH_LONG).show()

                stopService(intent)
            }
            else ->
            {
                startForeground(100, getNotification(this))
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun getNotification(context: Context): Notification
    {
        val CHANNEL_ID = "100"
        //create Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(serviceChannel)
        }

        val notificationIntent = Intent(context, OverlayService::class.java)
        notificationIntent.action = ACTION_STOP_VIEW

        val pendingIntent = PendingIntent.getService(
            context,
            0, notificationIntent, 0
        )

        val notification =  NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("drawing Service")
            .setContentText("click to stop")
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setContentIntent(pendingIntent)

        return notification.build()
    }



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}