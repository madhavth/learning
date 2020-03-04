package com.madhavth.firebaselearning.service

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

interface TestApiService {
    @GET("users")
    fun getUsers():
    // The Coroutine Call Adapter allows us to return a Deferred, a Job with a result
            Observable<List<TestEntity>>
}


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object TestApi {
    val RETROFIT_SERVICE: TestApiService by lazy { retrofit.create(TestApiService::class.java) }
}
