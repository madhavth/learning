package com.madhavth.firebaselearning.CustomViewGroups

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

const val TRIGGER_NOTIFICATION = "TRIGGER_NOTIFICATION"

class MyBroadCastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        Toast.makeText(context
        , "Intent Received...",
        Toast.LENGTH_SHORT).show()

        if(intent?.action == TRIGGER_NOTIFICATION)
            Toast.makeText(context
            , "toast from broadcast receiever",
            Toast.LENGTH_SHORT).show()
    }
}