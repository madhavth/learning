package com.madhavth.firebaselearning.CustomViewGroups

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.adapter.FragmentStateAdapter
import timber.log.Timber

class MyCustomViewGroup(context: Context, attributeSet: AttributeSet): ViewGroup(context,attributeSet)
{

    private fun measureDimension(desiredSize: Int,  measuredSpec: Int): Int
    {
            var result = 0
            val specMode = MeasureSpec.getMode(measuredSpec)
            val specSize = MeasureSpec.getSize(measuredSpec)

            Timber.d("specSize measureDimension specSize is $specSize")
            if(specMode == MeasureSpec.EXACTLY)
            {
                result = specSize
            }
            else
            {
                result = desiredSize
                if(specMode == MeasureSpec.AT_MOST)
                {
                    result = specSize/2
                }
            }

            if(result< desiredSize)
                Timber.d("result is less than desiredSize $result, $desiredSize")

            Timber.d("the result is $result")
            return result
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val displayMetrics = context.resources.displayMetrics
        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        val pxHeight = displayMetrics.heightPixels
        val pxWidth = displayMetrics.widthPixels

        var totalWidth = 0
        var totalHeight = 0

        for(i in 0 until childCount)
        {
            val child =getChildAt(i)

            val lp = child.layoutParams
            Timber.d("child lp ${lp.width}, ${lp.height}")

            totalHeight+= child.measuredHeight

            totalWidth+= child.measuredWidth

            if(totalWidth >= pxWidth)
                totalWidth = pxWidth

            measureChild(child,widthMeasureSpec, heightMeasureSpec)
        }

        val measuredWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        val measuredHeightSize = MeasureSpec.getSize(heightMeasureSpec)


//        setMeasuredDimension(measureDimension(measuredWidthSize,widthMeasureSpec),
//        measureDimension(measuredHeightSize, heightMeasureSpec))
        setMeasuredDimension(totalWidth, totalHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var prevChildRight = 0
        var prevChildBottom = 0

        val count = childCount

        for(i in 0 until count)
        {
            val child = getChildAt(i)
            child.layout(prevChildRight, prevChildBottom,
                prevChildRight + child.measuredWidth, prevChildBottom + child.measuredHeight
                )
            prevChildBottom+= child.measuredHeight
        }
    }
}