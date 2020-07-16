package com.madhavth.firebaselearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_lock_screen.*

class TicTacActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myArray = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString()
        Log.d("TicTacActivity","This is the beginning of my time !!!")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TicTacActivity","I am destroyed!!!")
    }
}
