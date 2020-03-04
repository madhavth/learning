package com.madhavth.firebaselearning.Widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import com.madhavth.firebaselearning.R
import com.madhavth.firebaselearning.service.WeatherApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherWidgetProvider: AppWidgetProvider() {
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        if (appWidgetIds != null) {
            for(appWidgetId in appWidgetIds)
            {
                val remoteViews = RemoteViews(context!!.packageName, R.layout.weather_widget)


                val intent = Intent(context, WeatherWidgetProvider::class.java)
                intent.action = ACTION_UPDATE_WEATHER
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    appWidgetId,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT
                )

                remoteViews.setOnClickPendingIntent(R.id.btnUpdateWeather,pendingIntent)

                appWidgetManager?.updateAppWidget(appWidgetId, remoteViews)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val remoteViews = RemoteViews(context?.packageName, R.layout.weather_widget)

        when(intent?.action)
        {
            ACTION_UPDATE_WEATHER -> {
                val random = Math.random()* 100
                val random2 = Math.random() *50

                CoroutineScope(Dispatchers.Main).launch {
                    getWeather(context!!, appWidgetManager, remoteViews)
                }
            }
        }
    }

    suspend fun getWeather(context: Context, appWidgetManager: AppWidgetManager, remoteViews: RemoteViews) {

        var temperature = ""
        var city = ""
        var error = false
        var errorMsg =""

        try{
            val weather = WeatherApi.RETROFIT_SERVICE.getWeatherAsync().await()
            city = weather.location?.name.toString()
            temperature = weather.current?.temperature.toString()
        }
        catch (e: Exception)
        {
            error = true
            errorMsg = e.toString()
        }

        remoteViews.setTextViewText(R.id.tvWeather,"temperature: $temperature")
        remoteViews.setTextViewText(R.id.tvWeatherCity,"city: $city")

        val componentName = ComponentName(context!!, WeatherWidgetProvider::class.java)

        Toast.makeText(context
            , kotlin.run {
                if(error)
                    "error occured $errorMsg"
                    else
                    "Weather Updated"
            },
            Toast.LENGTH_SHORT).show()

        appWidgetManager.updateAppWidget(componentName,remoteViews)
    }
}