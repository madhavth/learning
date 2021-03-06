package com.madhavth.firebaselearning

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PictureInPictureParams
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.LiveFolders.INTENT
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Rational
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.firebase.iid.FirebaseInstanceId
import com.madhavth.firebaselearning.Widgets.DRAW_OVER_OTHER_APPS
import com.madhavth.firebaselearning.Widgets.JSOUP_ADDRESS
import com.madhavth.firebaselearning.service.OverlayService
import kotlinx.android.synthetic.main.activity_scraping.*
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import timber.log.Timber


class ScrapingActivity : AppCompatActivity() {

    var displayMetrics: Lazy<DisplayMetrics> = lazy { this.resources.displayMetrics }
    var list: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scraping)

        btnShareIntent.setOnClickListener {
            val sendIntent = Intent()

            sendIntent.apply {
                action = Intent.ACTION_PROCESS_TEXT
                type = "text/plain"
                addCategory(Intent.CATEGORY_DEFAULT)
                putExtra(Intent.EXTRA_PROCESS_TEXT, "google is my bitch")
            }


            val shareIntent=  Intent.createChooser(sendIntent, null)

            shareIntent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            ContextCompat.startActivity(this, shareIntent!!, null)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Timber.d("text queried is $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Timber.d("text is $newText")
                return true
            }

        })

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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (!Settings.canDrawOverlays(this)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse(String.format("package:%s", packageName))
                    )
                    startActivityForResult(intent, DRAW_OVER_OTHER_APPS)
                } else {
                    val intent = Intent(this, OverlayService::class.java)
                    startService(intent)

                    Toast.makeText(
                        applicationContext
                        , "freely draw",
                        Toast.LENGTH_SHORT
                    ).show()
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


        //trigger broadcast receiver to send toast message
        btnService3.setOnClickListener {
//            val intent = Intent(this, MyBroadCastReceiver::class.java)
//            intent.action = TRIGGER_NOTIFICATION
//            sendBroadcast(intent)
//            val intentCamera = Intent(this, CameraActivity::class.java)
//            startActivity(intentCamera)

            val intent = Intent(this, TicTacGamePlayActivity::class.java)
            startActivity(intent)
        }


        for (i in 0..3) {
            val tempList = ArrayList<String>()
            for (j in 0..2) {
                tempList.add(j.toString())
            }
            list.add(tempList)
        }

        viewPager2.adapter = PagerAdapter(this)


        myCustomViewGroup.setOnClickListener {
            Toast.makeText(
                this
                , "navigating",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this@ScrapingActivity, AnimActivity::class.java)
            startActivity(intent)
        }

        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)

        valueAnimator.duration = 2000

        valueAnimator.addUpdateListener {
            btnService.x = it.animatedValue.toString().toFloat() * 100
            btnService.y = it.animatedValue.toString().toFloat() * 100
        }

        valueAnimator.startDelay = 2000
        valueAnimator.start()

        anotherValueAnimator()


        objectAnimator()
    }

    private fun anotherValueAnimator() {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)

        valueAnimator.addUpdateListener {
            btnService.x = it.animatedFraction * 200
            btnService2.y = it.animatedFraction * 100
        }
        valueAnimator.start()
    }

    private fun objectAnimator() {
        val objectAnimator = ObjectAnimator
            .ofFloat(btnService3, "translationY", 0f, 500f)
            .setDuration(5000)

        objectAnimator.interpolator = AccelerateDecelerateInterpolator()
        objectAnimator.startDelay = 500
        objectAnimator.start()
    }

    private fun getPipRatio(): Rational {
        return Rational(displayMetrics.value.heightPixels, displayMetrics.value.widthPixels)
//        return Rational(20,20)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DRAW_OVER_OTHER_APPS) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    Toast.makeText(
                        this
                        , "can draw over this app now",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this
                        , "permission not given for drawing",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        btnAnimationPage.setOnClickListener {

        }

    }


    private suspend fun jsoupTask() {
        withContext(Dispatchers.IO)
        {
            try {
                val document = Jsoup.connect(JSOUP_ADDRESS).get()

                Timber.tag("DocumentJSOUP").d("$document")

                val element = document.select(".mo-optin-form-note").first()
                Timber.tag("JSOUPElement").d("${element.children()}")
            } catch (e: Exception) {

            }
        }
    }


    private fun getId() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (!task.isSuccessful)
                return@addOnCompleteListener
            else {
                val token = task.result?.token
                Timber.d("Token is $token")
            }
        }
    }

    inner class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return list.size
        }

        override fun createFragment(position: Int): Fragment {
            val fragment = HorizantalListFragment.getInstance()
            val bundle = Bundle()
            bundle.putStringArrayList("myItems", list[position])
            fragment.arguments = bundle
            return fragment
        }

    }
}
