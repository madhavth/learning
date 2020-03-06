package com.madhavth.firebaselearning

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.JobIntentService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.madhavth.firebaselearning.Widgets.ACTION_STOP_VIEW
import com.madhavth.firebaselearning.Widgets.DRAW_OVER_OTHER_APPS
import com.madhavth.firebaselearning.Widgets.JSOUP_ADDRESS
import com.madhavth.firebaselearning.service.MyJobIntentService
import com.madhavth.firebaselearning.service.OverlayService
import kotlinx.android.synthetic.main.activity_gestures.*
import kotlinx.android.synthetic.main.activity_scraping.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import timber.log.Timber

class ScrapingActivity : AppCompatActivity() {

    private val coroutineScope=  CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scraping)

        coroutineScope.launch {
            jsoupTask()
        }

        btnService.setOnClickListener {
//            val RSS_JOB_ID = 100
//            val serviceIntent = Intent().apply {
//                putExtra("download_url", "https://www.google.com/")
//            }
//            MyJobIntentService().enqueueWork(this, 211, serviceIntent)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if(!Settings.canDrawOverlays(this))
            {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse(String.format("package:%s", packageName)))
                startActivityForResult(intent, DRAW_OVER_OTHER_APPS)
            }
            else
            {
                val intent = Intent(this, OverlayService::class.java)
                startService(intent)

                Toast.makeText(applicationContext
                , "freely draw",
                Toast.LENGTH_SHORT).show()
            }
        }

        btnService2.setOnClickListener {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == DRAW_OVER_OTHER_APPS)
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(Settings.canDrawOverlays(this))
                {
                    Toast.makeText(this
                    , "can draw over this app now",
                    Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(this
                    , "permission not given for drawing",
                    Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    suspend fun jsoupTask()
    {
        withContext(Dispatchers.IO)
        {
            val document = Jsoup.connect(JSOUP_ADDRESS).get()

            Timber.tag("DocumentJSOUP").d("$document")

            val element = document.select(".mo-optin-form-note").first()
            Timber.tag("JSOUPElement").d("${element.children()}")
        }
    }
}
