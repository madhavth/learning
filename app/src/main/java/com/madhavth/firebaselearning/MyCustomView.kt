package com.madhavth.firebaselearning

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setMargins
import kotlin.properties.Delegates
import kotlin.random.Random
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf


enum class CustomSetting{
    OPTION1, OPTION2, OPTION3, DEFAULT
}

class MyCustomView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val TAG = javaClass.simpleName
    private val rect = Rect()
    private val paint = Paint()
    private var myColor: Int
    private val smallRect = Rect()
    private val secondPaint = Paint()
    val mlp: ViewGroup.MarginLayoutParams by lazy {  layoutParams as ViewGroup.MarginLayoutParams }
    private val linePaint = Paint()
    var customSetting: CustomSetting? = null
    private var dpHeight by Delegates.notNull<Float>()
    private var dpWidth by Delegates.notNull<Float>()

    fun increaseMARGIN()
    {
        mlp.setMargins(100)
    }


    fun decreaseMargin()
    {
        mlp.setMargins(0)
    }


    fun swapColor()
    {
        val red = Random.nextInt(0,256)
        val green = Random.nextInt(0,256)
        val blue = Random.nextInt(0,256)
        myColor = Color.rgb(red, green, blue)
        Log.d(TAG, "${javaClass.enclosingMethod?.name}: the random color is $myColor")
        this.invalidate()
    }

    init
    {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.MyCustomView)
        myColor = attrs.getColor(R.styleable.MyCustomView_myColor, Color.RED)
        customSetting = CustomSetting.values()[attrs
            .getInt(R.styleable.MyCustomView_customSetting,3)]
        attrs.recycle()
        setOptionsSettings()

        val displayMetrics = context.resources.displayMetrics
         dpHeight = displayMetrics.heightPixels / displayMetrics.density
         dpWidth = displayMetrics.widthPixels / displayMetrics.density
        Log.d(TAG,"the dpHeight and dpWidth are $dpHeight and $dpWidth")
    }

    private fun setOptionsSettings()
    {
        when(customSetting)
        {
            CustomSetting.OPTION1 ->{
                Log.d(TAG, "${javaClass.enclosingMethod?.name}: custom option1")
            }
            CustomSetting.OPTION2 ->{
                Log.d(TAG, "${javaClass.enclosingMethod?.name}: custom option2")

            }

            CustomSetting.OPTION3 ->{
                Log.d(TAG, "${javaClass.enclosingMethod?.name}: custom option3")

            }

            CustomSetting.DEFAULT ->{
                Log.d(TAG, "${javaClass.enclosingMethod?.name}: custom default option")

            }
        }
    }

    override fun onDraw(canvas: Canvas?)
    {
        val xAxis = 500f
        val yAxis = 500f
        val x1Axis = 0f

        val xRectAxis = 0
        val yRectAxis = 500

        val xSmallRect = 200
        val ySmallRect = 300

        paint.color = myColor
        linePaint.color = Color.BLUE

        rect.set(xRectAxis,xRectAxis, yRectAxis, yRectAxis)
        smallRect.set(xSmallRect,xSmallRect,ySmallRect,ySmallRect)
        secondPaint.color = Color.BLUE
        canvas?.drawRect(rect, paint)
        canvas?.drawRect(smallRect, secondPaint)

        canvas?.drawLine(x1Axis, x1Axis, xAxis, yAxis, linePaint)
        canvas?.drawLine(xAxis, x1Axis, x1Axis, yAxis, linePaint)
        Log.d(TAG, "${javaClass.enclosingMethod?.name}: myColor is $myColor")
    }
}