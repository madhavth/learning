package com.madhavth.firebaselearning.Widgets

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.madhavth.firebaselearning.R
import kotlinx.android.synthetic.main.activity_widget_configure.*

class WidgetConfigure : AppCompatActivity() {

    private val PREFS_NAME = "Button_Widget"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)
        setContentView(R.layout.activity_widget_configure)

        val appWidgetID=  intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if(appWidgetID == AppWidgetManager.INVALID_APPWIDGET_ID)
            finish()

        btnFinishConfigure.setOnClickListener {
            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID)
            setResult(RESULT_OK, resultValue)
            finish()
        }

    }
}
