package com.madhavth.firebaselearning.Widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.InetAddresses
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.Formatter
import android.widget.RemoteViews
import android.widget.Toast
import com.madhavth.firebaselearning.AppPrefrence
import com.madhavth.firebaselearning.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.ref.WeakReference
import java.net.InetAddress
import java.text.Format


class IpAddressWidetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val widgetText = context.getString(R.string.appwidget_text)
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.ip_address_widet_provider)

        val intent = Intent(context, IpAddressWidetProvider::class.java)
        intent.action = "UPDATE_ADDRESS"
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)


        CoroutineScope(Dispatchers.IO).launch {
            views.setOnClickPendingIntent(R.id.tvIpAddress, pendingIntent)

            val wifiManager = context.applicationContext
                .getSystemService(WIFI_SERVICE) as WifiManager


            val iNetIp = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)

            views.setTextViewText(R.id.tvIpAddress, "IP: $iNetIp")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val views = RemoteViews(context?.packageName, R.layout.ip_address_widet_provider)
        val componentName = ComponentName(context!!, IpAddressWidetProvider::class.java)
        val appWidgetManger = AppWidgetManager.getInstance(context)

        if (intent?.action == "UPDATE_ADDRESS") {

            Timber.d("updating ip address")

            CoroutineScope(Dispatchers.IO).launch {

                val wifiManager = context.applicationContext
                    .getSystemService(WIFI_SERVICE) as WifiManager

                val iNetIp = Formatter
                    .formatIpAddress(wifiManager.connectionInfo.ipAddress).toString()

                views.setTextViewText(R.id.tvIpAddress, "IP: $iNetIp")
                appWidgetManger.updateAppWidget(componentName, views)

                val appPrefs = AppPrefrence(context)

                if(appPrefs.lastIpAddress != iNetIp)
                withContext(Dispatchers.Main)
                {
                    appPrefs.lastIpAddress = iNetIp
                    Toast.makeText(WeakReference(context).get(),
                        "Fetched IP Successfully",
                        Toast.LENGTH_SHORT).show()
                }
//                val sendIntent = Intent()
//
//                sendIntent.apply {
//                    action = Intent.ACTION_PROCESS_TEXT
//                    putExtra(Intent.EXTRA_PROCESS_TEXT, iNetIp)
//                    addCategory("android.intent.category.DEFAULT")
//                    type = "text/plain"
//                }
//
//                val shareIntent = Intent.createChooser(sendIntent, null)
//                shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                context.startActivity(shareIntent)
            }
        }
    }
}
