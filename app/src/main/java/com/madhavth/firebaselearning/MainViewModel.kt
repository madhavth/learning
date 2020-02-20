package com.madhavth.firebaselearning

import android.util.Log
import androidx.lifecycle.*

class MainViewModel: ViewModel(){

    var value1 = MutableLiveData<Float>()
    var value2 = MutableLiveData<Int>()
    var merge = MediatorLiveData<String>()
    var changes = MutableLiveData<Int>()


    val checkChanges = Transformations.switchMap(merge
    ) {
        Log.d("TESTING", "$it changed")
        changes
    }


    fun increase()
    {
        changes.value = changes.value!! + 1
    }


    val TAG = "MainViewModel"

    init {
        changes.value = 0
        value1.value = 0f
        value2.value = 0

        merge.addSource(value1) {
            merge.value = value1.value.toString()
        Log.d(TAG, "value1 of merge is ${merge.value.toString()}")
        }

        merge.addSource(value2){
            merge.value = value2.value.toString()
            Log.d(TAG, "value2 of merge is ${merge.value}")
        }
    }


}