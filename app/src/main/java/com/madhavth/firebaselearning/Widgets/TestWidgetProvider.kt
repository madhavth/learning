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
import dagger.Component

class TestWidgetProvider: AppWidgetProvider()
{

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        if (appWidgetIds != null) {
            for (appWidgetId in appWidgetIds) {
                val remoteViews = RemoteViews(
                    context!!.packageName,
                    R.layout.my_new_test_widget
                )
                val random = Math.random() * 100
                remoteViews.setTextViewText(R.id.tvNewWidget, "$random")

                val intent = Intent(context, TestWidgetProvider::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    0
                )


                val buttonIntent = Intent(context, TestWidgetProvider::class.java)
                buttonIntent.action = BUTTON_ACTION

                val pendingIntent2 = PendingIntent.getBroadcast(
                    context,
                    0,
                    buttonIntent,
                    0
                )


                remoteViews.setOnClickPendingIntent(R.id.btnChangingTestNew, pendingIntent2)
//                remoteViews.setOnClickPendingIntent(R.id.btnChangingTestNew, pendingIntent)

                appWidgetManager?.updateAppWidget(appWidgetId, remoteViews)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if(intent?.action == BUTTON_ACTION)
        {
            Toast.makeText(context
                , "clicked change button",
                Toast.LENGTH_SHORT).show()

            val componentName = ComponentName(context!!, TestWidgetProvider::class.java)
            val remoteViews = RemoteViews(context.packageName,R.layout.my_new_test_widget)
            val random = Math.random()* 100
            remoteViews.setTextViewText(R.id.tvNewWidget, "new $random")

            val appWidgetManager = AppWidgetManager.getInstance(context)
            appWidgetManager.updateAppWidget(componentName, remoteViews)
        }

        else if(intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        {
//            Toast.makeText(context
//            , "Updated..",
//            Toast.LENGTH_SHORT).show()
        }

    }
}