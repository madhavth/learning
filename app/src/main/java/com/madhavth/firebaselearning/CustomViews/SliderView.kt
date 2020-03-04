package com.madhavth.firebaselearning.CustomViews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import com.madhavth.firebaselearning.R
import timber.log.Timber
import kotlin.math.min

class SliderView(context: Context, attrSet: AttributeSet): View(context,attrSet)
{
    private val TAG = javaClass.simpleName

    private var _paintSlider: Paint = Paint()
    private var _paintDrag: Paint = Paint()
    private var _rectSlider: RectF = RectF()
    private var _rectDrag: RectF = RectF()

    var x1:Float = 0f
    var y1:Float = 0f
    var vx:Float = 0f
    var vy:Float = 0f

    private val displayMetrics: DisplayMetrics = context.resources.displayMetrics
    val dpHeight = displayMetrics.heightPixels / displayMetrics.density
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density

    var mHeight = height.toFloat()
    var mWidth = width.toFloat()

    init {
        val ta = context.obtainStyledAttributes(attrSet, R.styleable.SliderView)
        ta.recycle()
        _paintSlider.isAntiAlias= true
        _paintDrag.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Timber.d("onMeasure w: ${MeasureSpec.toString(widthMeasureSpec)}")
        Timber.d("onMeasure h: ${MeasureSpec.toString(heightMeasureSpec)}")

        val desiredWidth = suggestedMinimumWidth + paddingStart + paddingEnd
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        Timber.tag("heightWidth").d("desiredWidth height $desiredWidth, $desiredHeight")
        setMeasuredDimension(measureDimension(desiredWidth, widthMeasureSpec), measureDimension(desiredHeight, heightMeasureSpec))
    }


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


    private val ratio_X = 1/3f
    private val ratio_Y = 1/3f

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        Timber.tag("WidthHeight").d("width are height are $width and $height")

        _paintDrag.color = Color.BLUE
        _paintDrag.textSize= 100f

        _paintSlider.strokeWidth = 5f
        _paintSlider.color = Color.BLACK

        Timber.d("measured Height and width are $measuredHeight and $measuredWidth")

        canvas?.drawLine(width * ratio_X,0f, width*ratio_X, height.toFloat(),_paintSlider)
        canvas?.drawLine(width * 2* ratio_X,0f, width*2*ratio_X, height.toFloat(),_paintSlider)

        canvas?.drawLine(0f,height* ratio_Y, width.toFloat(), height * ratio_Y,_paintSlider)
        canvas?.drawLine(0f,height*2* ratio_Y, width.toFloat(), height *2* ratio_Y,_paintSlider)

        canvas?.drawRect(_rectDrag, _paintDrag)
        canvas?.drawRect(_rectSlider,_paintSlider)


        canvas?.drawText("X",width* ratio_X/3, height*ratio_Y/3, _paintDrag)

    }
}