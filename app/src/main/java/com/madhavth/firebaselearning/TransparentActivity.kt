package com.madhavth.firebaselearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.new_app_widget.view.*
import timber.log.Timber

class TransparentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transparent)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Timber.d("The touchevent on transparent activity")
        this.finish()
        return false;
    }
}
