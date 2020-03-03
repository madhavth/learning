package com.madhavth.firebaselearning

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Scene
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.madhavth.firebaselearning.Widgets.BUTTON_ACTION
import com.madhavth.firebaselearning.databinding.ActivitySecondBinding
import com.madhavth.firebaselearning.service.TestApi
import com.madhavth.firebaselearning.service.TestEntity
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.test_layout_1.*
import kotlinx.android.synthetic.main.test_layout_1.view.*
import kotlinx.android.synthetic.main.test_layout_1.view.btnTesting
import kotlinx.android.synthetic.main.test_layout_2.*
import kotlinx.android.synthetic.main.test_layout_2.view.*
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class SecondActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_second)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        testButton1.setOnClickListener {
            viewModel.increase()
            viewModel.value2.value = 10

//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            this.finish()
            changeLayout()
        }

        testButton2.setOnClickListener {
            val intent = Intent()
            intent.action = BUTTON_ACTION
            sendBroadcast(intent)
        }


        observeThis()
        observeMerge()
    }


    var toggle = true

    private fun changeLayout()
    {
        val transition: Transition = TransitionInflater.from(this).inflateTransition(R.transition.change_image_transform)
        val firstScene = Scene.getSceneForLayout(changing_layout,
            R.layout.test_layout_1,this)
        val secondScene = Scene.getSceneForLayout(changing_layout,
            R.layout.test_layout_2,this)


        TransitionManager.go(kotlin.run {
            if(toggle)
                secondScene
            else
                firstScene
        }, transition)

        toggle= !toggle
    }



    fun printData(i: Int)
    {
        Log.d("INTEGER", i.toString())
    }

    private fun observeThis()
    {
        val scheduler = TestScheduler()
        val myList = arrayListOf<Int>(1,2,3,4,5,6,7,8,9)

        val observable = Observable.fromArray(myList)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())


        observable
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleData)


        scheduler.advanceTimeBy(5, TimeUnit.SECONDS)
    }


    fun handleData(s: List<Int>)
    {
        Toast.makeText(applicationContext
        , "$s",
        Toast.LENGTH_LONG).show()
    }



    private fun observeMerge()
    {
        viewModel.merge.observe(this, Observer{
            viewModel.increase()
        })

    }
}
