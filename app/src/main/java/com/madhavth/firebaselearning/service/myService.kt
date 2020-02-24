package com.madhavth.firebaselearning.service

import android.R
import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.madhavth.firebaselearning.MainActivity
import java.util.*


class MyService: Service()
{
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val message = intent?.getStringExtra("KEY1")
        Toast.makeText(this,"the message is $message",Toast.LENGTH_LONG).show()

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
            manager.createNotificationChannel(serviceChannel)


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
        }

        return START_NOT_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this
        , "i am destroyed!!!",
        Toast.LENGTH_LONG).show()
    }
}