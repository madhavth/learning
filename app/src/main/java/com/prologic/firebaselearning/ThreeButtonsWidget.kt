package com.prologic.firebaselearning

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startForegroundService
import com.madhavth.firebaselearning.MainActivity
import com.madhavth.firebaselearning.R
import com.madhavth.firebaselearning.ScrapingActivity
import com.madhavth.firebaselearning.Widgets.BUTTON1_INTENT
import com.madhavth.firebaselearning.Widgets.BUTTON2_INTENT
import com.madhavth.firebaselearning.Widgets.BUTTON3_INTENT
import com.madhavth.firebaselearning.service.OverlayService
import timber.log.Timber
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class ThreeButtonsWidget : AppWidgetProvider() {
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

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Timber.d("onReceived this ${intent?.action}")
        when(intent?.action)
        {
            BUTTON1_INTENT -> {
                Toast.makeText(context?.applicationContext
                , "button 1 clicked",
                Toast.LENGTH_SHORT).show()
            }

            BUTTON2_INTENT -> {
                Toast.makeText(context?.applicationContext
                    , "stopping overlay",
                    Toast.LENGTH_SHORT).show()
                val intent = Intent(context, OverlayService::class.java)
                context?.stopService(intent)
            }

            BUTTON3_INTENT -> {
                Toast.makeText(context?.applicationContext
                    , "starting draw window",
                    Toast.LENGTH_SHORT).show()
                val intent = Intent(context, OverlayService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(context!!, intent)
                }
                else
                    context?.startService(intent)
            }
        }
    }
}


internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.three_buttons_widget)

    val intent1 = Intent(context, ThreeButtonsWidget::class.java)
    intent1.action = BUTTON1_INTENT
    val pendingIntent1 = PendingIntent.getBroadcast(context, 0, intent1, 0)


    val pendingIntent2 = Intent(context, ThreeButtonsWidget::class.java).let {
        intent ->
            intent.action = BUTTON2_INTENT
            PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    val pendingIntent3 = Intent(context, ThreeButtonsWidget::class.java).let {
        intent ->
        intent.action = BUTTON3_INTENT
            PendingIntent.getBroadcast(context, 0, intent, 0)
    }


    views.setOnClickPendingIntent(R.id.btnTestWidget1, pendingIntent1)
    views.setOnClickPendingIntent(R.id.btnTestWidget2, pendingIntent2)
    views.setOnClickPendingIntent(R.id.btnTestWidget3, pendingIntent3)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}


