package com.madhavth.firebaselearning.Widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import com.madhavth.firebaselearning.R
import com.madhavth.firebaselearning.SecondActivity

class MyWidgetProvider: AppWidgetProvider()
{
    private val PREFS_NAME = "Button_Widget"

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        if (appWidgetIds != null) {
            for(appWidgetId in appWidgetIds)
            {
                val intent = Intent(context, MyWidgetProvider::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    appWidgetId,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
                val remoteViews = RemoteViews(context!!.packageName, R.layout.mywidget_example)
                remoteViews.setOnClickPendingIntent(R.id.btnMyExampleWidgetHello, pendingIntent)



                appWidgetManager?.updateAppWidget(appWidgetId,remoteViews)
            }
        }

    }


    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val remoteViews = RemoteViews(context!!.packageName, R.layout.mywidget_example)

        if(intent?.action == BUTTON_OLD_ACTION)
        {
            Toast.makeText(context
            , "Old Button also WOrks",
            Toast.LENGTH_SHORT).show()

            val random = Math.random() * 100
            remoteViews.setTextViewText(R.id.btnMyExampleWidgetHello, "new $random")
        }
    }

    private fun setRandom(context: Context)
    {
        val sharedPref = context.getSharedPreferences("MyPref", 0)
        val editor =sharedPref.edit()
        val random = Math.random() * 100
        editor.putString(PREFS_NAME, "$random")
    }

    private fun getPref(context: Context):String?
    {
        val sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val random = Math.random() * 100
        return sharedPreferences.getString(PREFS_NAME,"$random")
    }
}