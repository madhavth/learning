package com.madhavth.firebaselearning.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import com.madhavth.firebaselearning.MainActivity
import com.madhavth.firebaselearning.R
import com.madhavth.firebaselearning.ScrapingActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MyJobIntentService: JobIntentService() {

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun enqueueWork(context: Context, jobId: Int, intent: Intent)
    {
        enqueueWork(context, MyJobIntentService::class.java,jobId, intent)
    }

    override fun onHandleWork(intent: Intent) {

        coroutineScope.launch {
            Timber.tag("ConstantLog")
                .d("logggggg constant irritating ${Calendar.getInstance().time}")
            delay(10000)
            Timber.tag("ConstantLog").d("is this still here??????")
        }
        val download_url  =intent.extras?.getString("download_url")
        createNotification(download_url)
    }

    fun createNotification(message:String?)
    {
        //create Notification Channel
        val CHANNEL_ID =  "1111" //Calendar.getInstance().time.toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager =
                getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }

        val notificationIntent = Intent(this, ScrapingActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText(message)
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1111, notification)
    }

}