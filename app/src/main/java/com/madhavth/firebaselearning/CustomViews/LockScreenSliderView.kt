package com.madhavth.firebaselearning.CustomViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.Dimension
import androidx.work.impl.model.WorkSpec
import timber.log.Timber

enum class DIMENSION{
    HEIGHT, WIDTH
}

class LockScreenSliderView(context: Context, attributeSet: AttributeSet)
    : View(context,attributeSet)
{
    val displayMetrics = context.resources.displayMetrics
    val dpHeight = displayMetrics.heightPixels / displayMetrics.density
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density

    private val paint  =Paint()
    override fun onDraw(canvas: Canvas?) {
        paint.color = Color.RED
        canvas?.drawRect(0f,0f,width.toFloat(),height.toFloat(), paint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        setMeasuredDimension(getDimension(DIMENSION.WIDTH, widthMeasureSpec)
            , getDimension(DIMENSION.HEIGHT,heightMeasureSpec))
    }

    private fun getDimension(dimension: DIMENSION, measureSpec: Int): Int
    {
        val HEIGHT_TAG = "heightWidth"
        var result = 0
        var specSize = MeasureSpec.getSize(measureSpec)
        var specMode = MeasureSpec.getMode(measureSpec)

        Timber.d("specMode ${MeasureSpec.toString(measureSpec)} $dimension")

        result = when(specMode) {
            MeasureSpec.EXACTLY -> {
                if(dimension == DIMENSION.WIDTH)
                    specSize
                else
                    specSize
            }

            MeasureSpec.AT_MOST -> {
                Timber.tag(HEIGHT_TAG).d("specSize is $specSize for $dimension")

                if(dimension == DIMENSION.WIDTH) {
                    specSize
                } else {
                    specSize/8
                }
            }


            else -> {
                specSize
            }
        }

        return result
    }
}
