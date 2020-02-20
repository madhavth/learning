package com.madhavth.firebaselearning

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun testFlatMap()
    {
        val scheduler = TestScheduler()

        val list = arrayListOf<Int>(1,2,3,4,5,6,7,8,9)

        Observable.just(list)
            .flatMap {
                s->
                 Observable.just(s)
                     .delay(1L, TimeUnit.SECONDS, scheduler)
            }
            .toList()
            .subscribe()


        scheduler.advanceTimeBy(1L, TimeUnit.MINUTES)
    }

}
