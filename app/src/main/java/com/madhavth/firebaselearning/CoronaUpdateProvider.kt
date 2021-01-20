package com.madhavth.firebaselearning

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.ByteArrayOutputStream


const val TRIGGER_UPDATE_CORONA_NEPAL = "TRIGGER_UPDATE_CORONA_NEPAL"
const val TRIGGER_WORLD_STATS_UPDATE = "TRIGGER_WORLD_STATS_UPDATE"
const val UPDATE_MASK_TIPS = "UPDATE_MASK_TIPS"

class CoronaUpdateProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    private lateinit var appPrefrence: AppPrefrence

    override fun onReceive(context: Context?, intent: Intent?) {
        appPrefrence = AppPrefrence(context!!)
        super.onReceive(context, intent)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val views = RemoteViews(context?.packageName,R.layout.corona_update_provider)
        val componentName = ComponentName(context!!, CoronaUpdateProvider::class.java)

        if(intent?.action == TRIGGER_UPDATE_CORONA_NEPAL)
        {
            try{
                CoroutineScope(Dispatchers.IO).launch {
                    views.setTextViewText(R.id.tvNepalUpdates, "fetching..")
                    appWidgetManager.updateAppWidget(componentName, views)

                    appWidgetManager.updateAppWidget(componentName,views)
                }
            }
            catch(e: Exception)
            {

            }
        }

        else if(intent?.action == TRIGGER_WORLD_STATS_UPDATE)
        {
            CoroutineScope(Dispatchers.IO).launch {
                views.setTextViewText(R.id.tvWorldTotalCases, "updating world stats..")
                appWidgetManager.updateAppWidget(componentName, views)

                appWidgetManager.updateAppWidget(componentName, views)
            }
        }

    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val widgetText = context.getString(R.string.appwidget_text)
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.corona_update_provider)

        views.setTextViewText(R.id.tvNepalUpdates, "fetching...")
        views.setTextViewText(R.id.tvWorldTotalCases,"fetching...")
        views.setViewVisibility(R.id.progressLoadingCoronaUpdate, View.VISIBLE)
        views.setImageViewResource(R.id.imgViewCoronaUpdate, R.drawable.images)
        appWidgetManager.updateAppWidget(appWidgetId,views)

        val intent = Intent(context, CoronaUpdateProvider::class.java)
        intent.action = "TRIGGER_UPDATE_CORONA_NEPAL"
        val pendingIntent = PendingIntent.getBroadcast(context,0
            , intent, 0)

        val intent2 = Intent(context, CoronaUpdateProvider::class.java)
        intent2.action = "TRIGGER_WORLD_STATS_UPDATE"
        val pendingIntent2 = PendingIntent.getBroadcast(context, 1, intent2, 0)


        val intentImageClick = Intent(context, CoronaUpdateProvider::class.java)
        intentImageClick.action = UPDATE_MASK_TIPS
        val pendingIntent3 = PendingIntent.getBroadcast(context, 2, intentImageClick,0)


        views.setOnClickPendingIntent(R.id.tvNepalUpdates,pendingIntent)
        views.setOnClickPendingIntent(R.id.tvWorldTotalCases, pendingIntent2)
        views.setOnClickPendingIntent(R.id.imgViewCoronaUpdate, pendingIntent3)
    }
}
