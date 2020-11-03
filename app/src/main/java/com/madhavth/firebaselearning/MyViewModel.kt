package com.madhavth.firebaselearning

import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {
    fun testingSomethings(a: Int, b: Int):Int
    {
        return a * b
    }
}