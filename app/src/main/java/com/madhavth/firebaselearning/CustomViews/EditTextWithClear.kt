package com.madhavth.firebaselearning.CustomViews

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.madhavth.firebaselearning.R
import timber.log.Timber

class EditTextWithClear(context: Context, attributeSet: AttributeSet)
    : AppCompatEditText(context, attributeSet)
{
    private var mClearButton: Drawable = context.resources
        .getDrawable(R.drawable.ic_close_gray_24dp,null)

    init {
        addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s.isNullOrEmpty())
                    showClearButton()
                else
                    hideClearButton()
            }

        })

        setOnTouchListener { v, event ->
            if(compoundDrawablesRelative[2] !=null)
            {
                var clearButtonStart: Float
                var clearButtonEnd: Float
                var isButtonClicked = false

                if(layoutDirection == View.LAYOUT_DIRECTION_LTR)
                {
                    clearButtonStart = (v.width - paddingEnd - mClearButton.intrinsicWidth).toFloat()

                    Timber.d("eventX and ClearButtonStart are ${event.x}, $clearButtonStart")

                    if(event.x > clearButtonStart)
                    {
                        isButtonClicked = true
                    }
                }
                else
                {
                    clearButtonEnd= (mClearButton.intrinsicWidth + paddingStart).toFloat()

                    if(event.x < clearButtonEnd)
                        isButtonClicked = true
                }

                if(isButtonClicked)
                {
                   if(event.action == MotionEvent.ACTION_DOWN)
                   {
                        mClearButton = context.resources.getDrawable(R.drawable.ic_close_black_24dp,null)
                        showClearButton()
                   }

                    if(event.action == MotionEvent.ACTION_UP)
                    {
                        mClearButton = context.resources.getDrawable(R.drawable.ic_close_gray_24dp,null)
                        text?.clear()
                        hideClearButton()
                        return@setOnTouchListener true
                    }
                }
            }

            false
        }
    }

    private fun showClearButton()
    {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,mClearButton,null)
    }

    private fun hideClearButton()
    {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null)
    }


}