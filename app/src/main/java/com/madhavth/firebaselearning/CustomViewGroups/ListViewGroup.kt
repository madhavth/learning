package com.madhavth.firebaselearning.CustomViewGroups

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class ListViewGroup(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet)
{

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
    }

}
