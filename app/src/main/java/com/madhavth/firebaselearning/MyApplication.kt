package com.madhavth.firebaselearning

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import com.madhavth.firebaselearning.CustomViewGroups.MyBroadCastReceiver
import com.madhavth.firebaselearning.CustomViewGroups.TRIGGER_NOTIFICATION
import timber.log.Timber

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

//    <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
//    <action android:name="BUTTON_CLICK_1" />
//    <action android:name="BUTTON_CLICK_2" />
//    <action android:name="BUTTON_CLICK_3" />

}