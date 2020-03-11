package com.madhavth.firebaselearning

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.IntentFilter
import com.madhavth.firebaselearning.Widgets.BUTTON1_INTENT
import com.madhavth.firebaselearning.Widgets.BUTTON2_INTENT
import com.madhavth.firebaselearning.Widgets.BUTTON3_INTENT
import com.prologic.firebaselearning.ThreeButtonsWidget
import timber.log.Timber

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        registerThreeWidget()
    }

//    <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
//    <action android:name="BUTTON_CLICK_1" />
//    <action android:name="BUTTON_CLICK_2" />
//    <action android:name="BUTTON_CLICK_3" />
private fun registerThreeWidget()
    {
    }

}