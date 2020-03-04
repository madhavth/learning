package com.madhavth.firebaselearning.CustomViews

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.madhavth.firebaselearning.R
import java.lang.Math.abs

class MyCanvasView(context: Context,attributeSet: AttributeSet)
    : View(context, attributeSet) {
    private val mBackgroundColor = ContextCompat.getColor(context, R.color.opaque_orange)
    private val mDrawColor = ContextCompat.getColor(context, R.color.opaque_yellow)
    private val mPath = Path()
    private lateinit var mExtraCanvas: Canvas
    private lateinit var mExtraBitmap: Bitmap
    private val mPaint = Paint()
    private var mx = 0f
    private var mY = 0f
    private val TouchTolerance = 4f


    init {
        mPaint.color = mDrawColor
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.strokeWidth =12f
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(mExtraBitmap,0f, 0f,null)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when(event?.action)
        {
            MotionEvent.ACTION_UP -> {
                touchUp()
            }

            MotionEvent.ACTION_DOWN -> {
                touchStart(event.x, event.y)
            }

            MotionEvent.ACTION_MOVE -> {
                touchMove(event.x,event.y)
                invalidate()
            }
        }

        return true
    }

    private fun touchUp()
    {
        mPath.reset()
    }

    private fun touchMove(x: Float, y: Float)
    {
        val dx = kotlin.math.abs(x - mx)
        val dy= kotlin.math.abs(y - mY)

        if(dx >= TouchTolerance || dy >= TouchTolerance)
        {
            mPath.quadTo(mx,mY, (x+mx)/2, (y+mY)/2)
            mx = x
            mY = y
            mExtraCanvas.drawPath(mPath, mPaint)
        }
    }


    private fun touchStart(x: Float, y: Float)
    {
        mPath.moveTo(x,y)
        mx =x
        mY =y
    }

    fun clearBitmap()
    {
        mExtraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mExtraCanvas = Canvas(mExtraBitmap)
        mExtraCanvas.drawColor(mBackgroundColor)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mExtraBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888)
        mExtraCanvas = Canvas(mExtraBitmap)
        mExtraCanvas.drawColor(mBackgroundColor)
    }
}