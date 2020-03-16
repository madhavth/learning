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
import com.madhavth.firebaselearning.service.retorift.TestApi
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

                appPrefrence.nepalStats = cases

                return@withContext cases
            }
            catch (e: Exception) {
                Timber.d("cases error due to ${e.message}")
                return@withContext appPrefrence.nepalStats
            }
        }
    }


    suspend fun getWorldStats(): String
    {
        return withContext(Dispatchers.IO)
        {
            try{
                var result = TestApi.retrofitService.getTotalWorldCases().await()

                var worldStats = "World total cases : ${result.totalCases}, deaths: ${result.totalDeaths}, recovered: ${result.totalRecovered}\n" +
                        "new cases: ${result.newCases}, deaths: ${result.newDeaths}, statistics taken at: ${result.statisticTakenAt}"

                appPrefrence.worldStats = worldStats
                return@withContext worldStats
            }
            catch (e: Exception)
            {
                return@withContext appPrefrence.worldStats
            }
        }
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
                    val updatedText = getNepalCases()

                    views.setTextViewText(R.id.tvNepalUpdates, updatedText)
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

                val updatedWorldStats = getWorldStats()
                views.setTextViewText(R.id.tvWorldTotalCases, updatedWorldStats)
                appWidgetManager.updateAppWidget(componentName, views)
            }
        }

        else if(intent?.action == UPDATE_MASK_TIPS)
        {
            CoroutineScope(Dispatchers.IO).launch {
                views.setImageViewResource(R.id.imgViewCoronaUpdate, R.drawable.images)
                appWidgetManager.updateAppWidget(componentName, views)

                val bitmap = getCoronaUpdate()
                if(bitmap == null)
                {
                    views.setImageViewResource(R.id.imgViewCoronaUpdate, R.mipmap.corona_foreground)
                }
                else
                {
                    val bitmapOptions = BitmapFactory.Options()
                    val newbitmap = BitmapFactory.decodeByteArray(bitmap,0, bitmap.size,bitmapOptions)
                    views.setImageViewBitmap(R.id.imgViewCoronaUpdate, newbitmap)
                }
                appWidgetManager.updateAppWidget(componentName,views)
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

        CoroutineScope(Dispatchers.Main).launch {
            val txtNepalUpdate = getNepalCases()
            val txtWorldStats = getWorldStats()
            val myImage = getCoronaUpdate()

            views.setViewVisibility(R.id.progressLoadingCoronaUpdate, View.GONE )
            val bitmapOptions = BitmapFactory.Options()
            if(myImage!= null)
            {
                val bitmap = BitmapFactory.decodeByteArray(myImage,0, myImage.size, bitmapOptions)
                views.setImageViewBitmap(R.id.imgViewCoronaUpdate,bitmap)
            }
            else
            {
                views.setImageViewResource(R.id.imgViewCoronaUpdate, R.drawable.coronavirusstaysafe)
            }

            views.setTextViewText(R.id.tvWorldTotalCases, txtWorldStats)
            views.setTextViewText(R.id.tvNepalUpdates, txtNepalUpdate)
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
