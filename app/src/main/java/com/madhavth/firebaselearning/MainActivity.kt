package com.madhavth.firebaselearning

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.*
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.scene_one.myImageView
import kotlinx.android.synthetic.main.scene_one.view.*
import kotlinx.android.synthetic.main.scene_one.view.myImageView
import kotlinx.android.synthetic.main.scene_two.view.*


class MainActivity : AppCompatActivity() {

    var animating= true
    private lateinit var transitionManager: Transition
    var isAScene = true
    var scaled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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


}
