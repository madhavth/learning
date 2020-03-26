package com.madhavth.firebaselearning.Widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.madhavth.firebaselearning.R
import com.madhavth.firebaselearning.TransparentActivity

/**
 * Implementation of App Widget functionality.
 */
class DeveloperWidgetProvider : AppWidgetProvider() {
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
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.developer_widget_provider)

        val pendingIntent = Intent(context, TransparentActivity::class.java).let{
            it.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            PendingIntent.getActivity(context,0, it, 0)
        }

        views.setOnClickPendingIntent(R.id.btnTransparentOpen,pendingIntent)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

}
