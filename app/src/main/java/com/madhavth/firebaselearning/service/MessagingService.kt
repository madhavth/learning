package com.madhavth.firebaselearning.service

import android.app.PendingIntent
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class MessagingService: FirebaseMessagingService() {

    val FIREBASEMESSAGE1 = "firebasemessage1"
    val FIREBASEMESSAGE2 = "firebasemessage2"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val data1 = remoteMessage.data.getValue("custom_key1")
        val data2 = remoteMessage.data.getValue("custom_key2")

        Timber.d("handling notifications from here onMessageReceived, $data1, $data2")
        //lets open foreground service on message received


        val intent = Intent(this, OverlayService::class.java)
        intent.putExtra(FIREBASEMESSAGE1, data1)
        intent.putExtra(FIREBASEMESSAGE2, data2)

        ContextCompat.startForegroundService(this, intent)
    }

    override fun onNewToken(p0: String) {
        Timber.d("new Token is $p0")
    }
}