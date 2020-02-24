package com.madhavth.firebaselearning.service

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.madhavth.firebaselearning.MainActivity
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import java.util.*


class MyService: Service()
{
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val message = intent?.getStringExtra("KEY1")

        //create Notification Channel
        val CHANNEL_ID = Calendar.getInstance().time.toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager =
                getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText(message)
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        doBackGroundTask()

        return START_NOT_STICKY
    }


    val job = Job()
    val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    private fun doBackGroundTask() {

        coroutineScope.launch {
            for(i in 1..9)
            {
                delay(3000)
                notification("i am work $i")
            }
        }
    }



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this
        , "i am destroyed!!!",
        Toast.LENGTH_LONG).show()
        notification("destroyed service 1")
        job.cancel()
        coroutineScope.cancel()
    }

    fun notification(message:String)
    {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val notificationManager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification: NotificationCompat.Builder = NotificationCompat
            .Builder(applicationContext, "default")
            .setContentTitle("working periodic background task")
            .setContentText(message)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.sym_def_app_icon)

        //getting unique id -- just getting the current time for now
        val id = Date().time.toInt()


        notificationManager.notify(id, notification.build())

    }
}