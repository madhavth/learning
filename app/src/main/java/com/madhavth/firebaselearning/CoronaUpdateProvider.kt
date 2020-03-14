package com.madhavth.firebaselearning

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.RemoteViews
import com.madhavth.firebaselearning.service.retorift.TestApi
import kotlinx.coroutines.*

/**
 * Implementation of App Widget functionality.
 */
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


    suspend fun getCoronaUpdate(): ByteArray?
    {
        return withContext(Dispatchers.IO) {
            try{
                val coronaUpdate = TestApi.retrofitService.getUpdates().await()
                val coronaImage = coronaUpdate.bytes()
                return@withContext coronaImage
            }
            catch(e: Exception)
            {
                return@withContext null
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

        views.setViewVisibility(R.id.progressLoadingCoronaUpdate, View.VISIBLE)
        views.setImageViewResource(R.id.imgViewCoronaUpdate, R.drawable.images)
        appWidgetManager.updateAppWidget(appWidgetId,views)

        CoroutineScope(Dispatchers.Main).launch {
            val myImage = getCoronaUpdate() ?: return@launch

            views.setViewVisibility(R.id.progressLoadingCoronaUpdate, View.GONE )
            val bitmapOptions = BitmapFactory.Options()
            val bitmap = BitmapFactory.decodeByteArray(myImage,0, myImage.size, bitmapOptions)



            views.setImageViewBitmap(R.id.imgViewCoronaUpdate,bitmap)
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
