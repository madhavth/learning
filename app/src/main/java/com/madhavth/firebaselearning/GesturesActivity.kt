package com.madhavth.firebaselearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.activity_gestures.*
import timber.log.Timber

class GesturesActivity : AppCompatActivity(), GestureDetector.OnGestureListener
    , GestureDetector.OnDoubleTapListener, ScaleGestureDetector.OnScaleGestureListener {

    private lateinit var mDetector: GestureDetectorCompat
    private lateinit var sDetector: ScaleGestureDetector
    val DEBUG_TAG = "GesturesTest"

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestures)
        mDetector = GestureDetectorCompat(this, this)
        sDetector = ScaleGestureDetector(this, this)

        mDetector.setOnDoubleTapListener(this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun onDown(event: MotionEvent): Boolean {
        Timber.d("$DEBUG_TAG onDown: ")
        return true
    }

    override fun onFling(
        event1: MotionEvent,
        event2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Timber.d("$DEBUG_TAG onFling: 1 2")
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        Timber.d("$DEBUG_TAG onLongPress: ")
    }

    override fun onScroll(
        event1: MotionEvent,
        event2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Timber.d("$DEBUG_TAG onScroll: 1 2")
        return true
    }

    override fun onShowPress(event: MotionEvent) {
        Timber.d("$DEBUG_TAG onShowPress: ")
    }

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        Timber.d("$DEBUG_TAG onSingleTapUp: ")
        return true
    }

    override fun onDoubleTap(event: MotionEvent): Boolean {
        Timber.d("$DEBUG_TAG onDoubleTap: ")
        btnGesturePractice.setBackgroundColor(resources.getColor(R.color.colorAccent))
        return true
    }

    override fun onDoubleTapEvent(event: MotionEvent): Boolean {
        Timber.d("$DEBUG_TAG onDoubleTapEvent: ")
        return true
    }

    override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
        Timber.d("$DEBUG_TAG onSingleTapConfirmed: ")
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
    }

    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        return true
    }

}
