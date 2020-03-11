package com.madhavth.firebaselearning.CustomViews

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import timber.log.Timber

class CustomFrameLayout(context: Context,attributeSet: AttributeSet): FrameLayout(context,attributeSet), View.OnTouchListener {
    var isMinimized = false

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Timber.d("dispatchTouchEvent moving customView")

        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Timber.d("onTouchEvent moving customView to ${event?.x},${event?.y} ")
        return super.onTouchEvent(event)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Timber.d("moving customView to ${event?.x},${event?.y} ")
        if(isMinimized)
        when(event?.action)
        {
            MotionEvent.ACTION_DOWN -> {

            }

            MotionEvent.ACTION_MOVE -> {

            }

            MotionEvent.ACTION_UP -> {

            }
        }

        return false
    }

    fun toggleMinimized()
    {
        isMinimized = !isMinimized
    }
}