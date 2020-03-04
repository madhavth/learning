package com.madhavth.firebaselearning

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.Scene
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginStart
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import com.madhavth.firebaselearning.Widgets.BUTTON_ACTION
import com.madhavth.firebaselearning.service.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.scene_one.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val PREFS_NAME = "Button_Widget"

    var animating= true
    private lateinit var transitionManager: Transition
    var isAScene = true
    var scaled = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        getData()


        val intentFilter = IntentFilter()
        intentFilter.addAction(BUTTON_ACTION)
        intentFilter.priority = 100

        registerReceiver(MyBroadReceiver(), intentFilter)

        //set pref
        setPref()

        testingAPI()
        title= "Animation Testing"
        btnTest1.setOnClickListener {
            crossFade()
            doSomethingWithSceneId()
        }

        btnTest2.setOnClickListener {
            rotate()
        }

        btnTest3.setOnClickListener {
            changeScene()
        }

        transitionManager = TransitionInflater.from(this)
            .inflateTransition(R.transition.new_transition)


        btnStart.setOnClickListener {
            val intent = Intent(this, MyService::class.java)
            intent.putExtra("KEY1","use this value in the service")
            startService(intent)
        }


        btnStop.setOnClickListener {
            val intent = Intent(this, MyService::class.java)
            stopService(intent)
        }


        btnGetWeather.setOnClickListener {


            CoroutineScope(Dispatchers.Main).launch{
                var temperature = ""
                var city = ""

                try{
                    val weather = WeatherApi.RETROFIT_SERVICE.getWeatherAsync().await()
                    city = weather.location?.name.toString()
                    temperature = weather.current?.temperature.toString()
                    Toast.makeText(applicationContext
                    , "success got weather $temperature $city",
                    Toast.LENGTH_SHORT).show()
//                    tvWeatherReport.text = weather.toString()
                }
                catch (e: Exception)
                {
                    Toast.makeText(applicationContext
                    , "$e",
                    Toast.LENGTH_SHORT).show()
                    tvWeatherReport.text = e.toString()
                }
            }
        }

        btnIncreaseCV.setOnClickListener(this)
        btnDecreaseCV.setOnClickListener(this)
        btnColorCV.setOnClickListener(this)

    }





    private fun setPref() {
        val sharedPreferences = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(PREFS_NAME,"default")
        editor.apply()
    }

    private fun crossFade()
    {
        btnTest1.alpha = 0f

        btnTest1.animate()
            .alpha(1f)
            .setDuration(3000)
            .setListener(null)

        //change to second activity
        val options = ActivityOptions.makeSceneTransitionAnimation(this,btnTest1,"test1")
        val intent = Intent(this, SecondActivity::class.java)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }


    private fun rotate()
    {
        btnTest2.rotation = 0f
        btnTest2.animate()
            .rotation(360f)
            .setDuration(3000)
            .setListener(null)
    }

    private fun enLarge()
    {
        btnTest3.scaleX = 1f
        btnTest3.scaleY = 1f

        btnTest3.animate()
            .scaleX(2f)
            .scaleY(2f)
            .setDuration(1500)
            .setListener(null)

    }


    private fun doSomethingWithSceneId()
    {
        if(!scaled)
            myImageView.scaleX = 1.5f

        else
            myImageView.scaleX = 1.0f

        scaled = !scaled
    }

    private fun changeScene()
    {
        val sceneOne:Scene = Scene.getSceneForLayout(rootScene, R.layout.scene_one, this)
        val sceneTwo:Scene = Scene.getSceneForLayout(rootScene, R.layout.scene_two, this)


        if(isAScene)
        {
            TransitionManager.go(sceneTwo, transitionManager)
        }
        else
        {
            TransitionManager.go(sceneOne, transitionManager)
        }

        isAScene = !isAScene
    }




    private fun testingAPI()
    {
        val myList = arrayListOf(
            TestEntity(id=1,username = "asd",name = "asd"),
            TestEntity(id=112,username = "asd",name = "asd"),
            TestEntity(id=23,username = "asd",name = "asd")
        )
        val observable = Observable.fromIterable(myList)


        val observer = object: Observer<TestEntity>
        {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: TestEntity) {
//             Toast.makeText(applicationContext
//             , "$t received",
//             Toast.LENGTH_SHORT).show()
            }

            override fun onError(e: Throwable) {
//                Toast.makeText(applicationContext
//                , "error occurred",
//                Toast.LENGTH_SHORT).show()
            }

        }

        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)


        fetchDataFromAPI()
    }

    @SuppressLint("CheckResult")
    fun fetchDataFromAPI()
    {
        val myData = TestApi.RETROFIT_SERVICE.getUsers()

        myData
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe{
                myList ->
                    setList(myList)
            }
    }


    fun setList(myList: List<TestEntity>)
    {
        Toast.makeText(applicationContext
        , "myList is $myList",
        Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when(v)
        {
            btnIncreaseCV -> {
                myCustomView.increaseMARGIN()

            }

            btnDecreaseCV ->{
                myCustomView.decreaseMargin()
            }

            btnColorCV -> {
                myCustomView.swapColor()
            }
        }
    }

    fun showToast(msg: String)
    {
        Toast.makeText(this
        , msg,
        Toast.LENGTH_SHORT).show()
    }
//    fun getData(): Observer<List<TestEntity>>
//    {
//        val myDisposable = CompositeDisposable()
//
//        myDisposable.add(TestApi.RETROFIT_SERVICE.getUsers()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe(this::handleResposne)
//        )
//        myDisposable.clear()
//    }
//
//
//    fun handleResposne(list: List<TestEntity>)
//    {
//        Log.d("MainActivity", list.toString())
//        tvFetchedData.text = list.toString()
//    }

}

