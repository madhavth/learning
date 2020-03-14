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
import timber.log.Timber

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

    suspend fun getNepalCases(): String
    {
        return withContext(Dispatchers.IO)
        {
            try {
                val allCases = com.madhavth.firebaselearning
                    .service.retorift.TestApi.retrofitService.getCasesByCountry().await().string()

                val nepalIndex = allCases.indexOf("\"country_name\":\"Nepal\"")

                val nepalCases = allCases.substring(nepalIndex, nepalIndex+200)
                val nepalSubString = nepalCases.split("\"").filter{

                    when(it)
                    {
                        "," -> return@filter false
                        ":" -> return@filter false
                        else -> return@filter true
                    }
                }

                //Timber.d("nepal cases are $nepalCases")
                Timber.d("nepal subStrings are $nepalSubString")

                nepalSubString.forEach{ sub ->
                    Timber.d("string is $sub")
                }

                val cases4 = nepalSubString[4]
                val death6 = nepalSubString[6]
                val totalRecovered10= nepalSubString[10]
                val newDeaths12 = nepalSubString[12]
                val newCases14 = nepalSubString[14]
                val seriousCritical16 = nepalSubString[16]

                Timber.d("Nepal cases - $cases4, deaths - $death6, total recovered: $totalRecovered10\n" +
                        "new deaths - $newDeaths12, new cases - $newCases14, serious critcal - $seriousCritical16")

                val cases = "Nepal cases - $cases4, deaths - $death6, total recovered: $totalRecovered10\n" +
                        "new deaths - $newDeaths12, new cases - $newCases14, serious critcal - $seriousCritical16"
                return@withContext cases
            }
            catch (e: Exception) {
                Timber.d("cases error due to ${e.message}")
                return@withContext "error retreiving cases"
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
        views.setViewVisibility(R.id.progressLoadingCoronaUpdate, View.VISIBLE)
        views.setImageViewResource(R.id.imgViewCoronaUpdate, R.drawable.images)
        appWidgetManager.updateAppWidget(appWidgetId,views)

        CoroutineScope(Dispatchers.Main).launch {
            val txtNepalUpdate = getNepalCases()
            val myImage = getCoronaUpdate() ?: return@launch

            views.setViewVisibility(R.id.progressLoadingCoronaUpdate, View.GONE )
            val bitmapOptions = BitmapFactory.Options()
            val bitmap = BitmapFactory.decodeByteArray(myImage,0, myImage.size, bitmapOptions)


            views.setTextViewText(R.id.tvNepalUpdates, txtNepalUpdate)
            views.setImageViewBitmap(R.id.imgViewCoronaUpdate,bitmap)
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
