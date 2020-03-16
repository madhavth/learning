package com.madhavth.firebaselearning.CustomViews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import com.madhavth.firebaselearning.R
import timber.log.Timber

class SliderView(context: Context, attrSet: AttributeSet): View(context,attrSet)
{
    private val TAG = javaClass.simpleName

    private var _paintSlider: Paint = Paint()
    private var _paintDrag: Paint = Paint()
    private var _paintText: Paint = Paint()
    private var _rectSlider: RectF = RectF()
    private var _rectDrag: RectF = RectF()

    var x1:Float = 0f
    var y1:Float = 0f
    var vx:Float = 0f
    var vy:Float = 0f

    private var OXList = mutableListOf<List<Int?>>(mutableListOf<Int?>(null,null,null),
        arrayListOf<Int?>(null,null,null),arrayListOf<Int?>(null,null,null))

    private var OYList = mutableListOf<List<Int?>>(mutableListOf<Int?>(null,null,null),
        arrayListOf<Int?>(null,null,null),arrayListOf<Int?>(null,null,null))


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

        _paintText.textSize= 18f
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

        //draw tic tac lines borders
        canvas?.drawLine(width * ratio_X,0f, width*ratio_X, height.toFloat(),_paintSlider)
        canvas?.drawLine(width * 2* ratio_X,0f, width*2*ratio_X, height.toFloat(),_paintSlider)
        canvas?.drawLine(0f,height* ratio_Y, width.toFloat(), height * ratio_Y,_paintSlider)
        canvas?.drawLine(0f,height*2* ratio_Y, width.toFloat(), height *2* ratio_Y,_paintSlider)

        canvas?.drawRect(_rectDrag, _paintDrag)
        canvas?.drawRect(_rectSlider,_paintSlider)

        for(i in 0..2)
        {
            for(j in 0..2)
            {
                Timber.d("i,j $i,$j are ${OXList[i][j]}, ${OYList[i][j]}")
                if(OXList[i][j]!=null && OYList[i][j]!=null)
                {
                    canvas?.drawText("O",OXList[i][j]?.toFloat()?:0f,OYList[i][j]?.toFloat()?:0f,_paintSlider)
                }
            }
        }
//        canvas?.drawText("X",width* ratio_X/3, height*ratio_Y/3, _paintDrag)
    }

    var touchX = 0
    var touchY = 0
    var ltrb = arrayListOf<Int>(0,0,0,0)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Timber.d("the width and height are now $w, $h")

            touchX = w * (1)/3
            touchY = h * (1)/3
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Timber.d("left right top bottom are $left, $right, $top, $bottom")
        Timber.d("touchX,Y List - $touchX, $touchY")

        ltrb[0] = left
        ltrb[1] = top
        ltrb[2] = right
        ltrb[3] = bottom
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Timber.d("onTouchEvent ${MotionEvent.actionToString(event?.action?: 0)}")
        if(event?.action == MotionEvent.ACTION_DOWN)
        {
            Timber.d("ltrb are $ltrb")
            Timber.d("touchX,Y are $touchX, $touchY")
            Timber.d("the raw x,y are ${event.rawX},${event.rawY}")


            if(event.rawX<= ltrb[2] && event.rawX >= ltrb[2]-touchX)
            {
                OXList[0] = arrayListOf(OXList[0][0],OXList[0][1],event.rawX.toInt())
            }

            if(event.rawY >=ltrb[1] && event.rawY <=ltrb[1]+ touchY)
            {
                OYList[0] = arrayListOf(OYList[0][0],OYList[0][1],event.rawY.toInt())
            }

            invalidate()
        }
        return super.onTouchEvent(event)
    }
}