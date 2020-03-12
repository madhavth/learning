package com.madhavth.firebaselearning

import android.app.PictureInPictureParams
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Rational
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.madhavth.firebaselearning.Widgets.DRAW_OVER_OTHER_APPS
import com.madhavth.firebaselearning.Widgets.JSOUP_ADDRESS
import com.madhavth.firebaselearning.service.OverlayService
import kotlinx.android.synthetic.main.activity_scraping.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import timber.log.Timber


class ScrapingActivity : AppCompatActivity() {

    var displayMetrics: Lazy<DisplayMetrics> = lazy { this.resources.displayMetrics }

    private val coroutineScope=  CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scraping)

        getId()

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
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            if(Settings.canDrawOverlays(this))
//            {
//                Toast.makeText(applicationContext
//                , "trying to draw floating overlay",
//                Toast.LENGTH_SHORT).show()
//                val intent= Intent(this, FloatingOverlayService::class.java)
//                stopService(intent)
//                startService(intent)
//            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val params = PictureInPictureParams.Builder()
                    .setAspectRatio(getPipRatio()).build()

                enterPictureInPictureMode(params)
            }
        }

    }

    private fun getPipRatio(): Rational
    {
        return Rational(displayMetrics.value.heightPixels, displayMetrics.value.widthPixels)
//        return Rational(20,20)
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


    private suspend fun jsoupTask()
    {
        withContext(Dispatchers.IO)
        {
            val document = Jsoup.connect(JSOUP_ADDRESS).get()

            Timber.tag("DocumentJSOUP").d("$document")

            val element = document.select(".mo-optin-form-note").first()
            Timber.tag("JSOUPElement").d("${element.children()}")
        }
    }

    private fun getId()
    {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            task ->
             if(!task.isSuccessful)
                 return@addOnCompleteListener
            else
             {
                 val token = task.result?.token
                 Timber.d("Token is $token")
            }
        }
    }
}
