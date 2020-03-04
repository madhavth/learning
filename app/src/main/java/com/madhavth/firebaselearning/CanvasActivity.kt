package com.madhavth.firebaselearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_canvas.*

class CanvasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Draw"
        setContentView(R.layout.activity_canvas)

        fabClear.setOnClickListener {
            myCanvas.clearBitmap()
        }
    }
}
