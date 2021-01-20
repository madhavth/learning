package com.madhavth.firebaselearning.Widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.net.wifi.WifiManager
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
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL


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

        val intent2= Intent(context, IpAddressWidetProvider::class.java)
        intent2.action = "TOGGLE_IP"
        val pendingIntent2 = PendingIntent.getBroadcast(context, 1, intent2, PendingIntent.FLAG_CANCEL_CURRENT)

        CoroutineScope(Dispatchers.IO).launch {
            views.setOnClickPendingIntent(R.id.tvIpAddress, pendingIntent)
            views.setOnClickPendingIntent(R.id.btnSwap, pendingIntent2)

            val wifiManager = context.applicationContext
                .getSystemService(WIFI_SERVICE) as WifiManager

            val iNetIp = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)

            views.setTextViewText(R.id.tvIpAddress, "IP: $iNetIp")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

    }



    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        when (intent?.action) {
            "UPDATE_ADDRESS" -> {
                context?.let { updateAddress(it) }
            }
            "TOGGLE_IP" -> {
                context?.let {
                    val appPrefs = AppPrefrence(context)
                    appPrefs.toggleIpMode()
                    updateAddress(it)
                }
            }
        }
    }


    private fun getPublicIp(): String?
    {
        val url = URL("https://ifconfig.me/ip")
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val br = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val sb = StringBuilder()
            var output: String?
            while (br.readLine().also { output = it } != null) {
                sb.append(output)
            }
            return sb.toString()
        }
        finally {
            urlConnection.disconnect()
        }
    }



    private fun updateAddress(context:Context)
    {
        val views = RemoteViews(context?.packageName, R.layout.ip_address_widet_provider)
        val componentName = ComponentName(context!!, IpAddressWidetProvider::class.java)
        val appWidgetManger = AppWidgetManager.getInstance(context)

        Timber.d("updating ip address")

        CoroutineScope(Dispatchers.IO).launch {

            val appPrefs = AppPrefrence(context)

            val wifiManager = context.applicationContext
                .getSystemService(WIFI_SERVICE) as WifiManager

            val iNetIp: String

            iNetIp = if(appPrefs.ipMode == LOCAL_IP) {
                Formatter
                    .formatIpAddress(wifiManager.connectionInfo.ipAddress).toString()
            } else {
                val publicIp = getPublicIp()
                publicIp.toString()
            }

            views.setTextViewText(R.id.tvIpAddress, "IP: $iNetIp")
            appWidgetManger.updateAppWidget(componentName, views)


            if(appPrefs.lastIpAddress != iNetIp)
                withContext(Dispatchers.Main)
                {
                    appPrefs.lastIpAddress = iNetIp
                }
        }
    }
}
