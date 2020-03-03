package com.madhavth.firebaselearning

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.madhavth.firebaselearning.Widgets.BUTTON_ACTION

class MyBroadReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context
        , "Intent detected...",
        Toast.LENGTH_SHORT).show()

        if(intent?.action == BUTTON_ACTION )
        {
            Toast.makeText(context
            , "This is from the broadcast receiver",
            Toast.LENGTH_SHORT).show()
        }
    }
}