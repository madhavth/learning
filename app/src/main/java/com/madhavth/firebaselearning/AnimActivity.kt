package com.madhavth.firebaselearning

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import kotlinx.android.synthetic.main.activity_anim.*

class AnimActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim)

        btnTransparentActivityOpen.setOnClickListener {
            //open transparent activity
            val intent = Intent(this, TransparentActivity::class.java)
            startActivity(intent)
        }
    }
}
