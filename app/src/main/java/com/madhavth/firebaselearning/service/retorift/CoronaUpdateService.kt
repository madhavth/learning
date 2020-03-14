package com.madhavth.firebaselearning.service.retorift

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

private const val BASE_URL = "https://coronavirus-monitor.p.rapidapi.com/coronavirus/"

interface CoronaApiService {
    //@GET("gdg-directory.json")
    @GET("masks.php")
    @Headers("X-RapidAPI-Key:eb20f16fa6mshdf197bdd685ca09p1f4332jsn5a977222635c", "X-RapidAPI-Host:coronavirus-monitor.p.rapidapi.com")
    fun getUpdates():
    // The Coroutine Call Adapter allows us to return a Deferred, a Job with a result
            Deferred<ResponseBody>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

object TestApi {
    val retrofitService: CoronaApiService by lazy { retrofit.create(CoronaApiService::class.java) }
}
