package com.madhavth.firebaselearning.service.retorift

import android.os.Environment
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.madhavth.firebaselearning.service.NepalCases
import com.madhavth.firebaselearning.service.WorldCases
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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

    @GET("cases_by_country.php")
    @Headers("X-RapidAPI-Key:eb20f16fa6mshdf197bdd685ca09p1f4332jsn5a977222635c", "X-RapidAPI-Host:coronavirus-monitor.p.rapidapi.com")
    fun getCasesByCountry(): Deferred<ResponseBody>

    @GET("worldstat.php")
    @Headers("X-RapidAPI-Key:eb20f16fa6mshdf197bdd685ca09p1f4332jsn5a977222635c", "X-RapidAPI-Host:coronavirus-monitor.p.rapidapi.com")
    fun getTotalWorldCases(): Deferred<WorldCases>


}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val cacheSize = 10 * 1024 * 1024L
val myCache = Cache(Environment.getDownloadCacheDirectory(), cacheSize)

val okHttpClient = OkHttpClient.Builder()
    .cache(myCache).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .build()

object TestApi {
    val retrofitService: CoronaApiService by lazy { retrofit.create(CoronaApiService::class.java) }
}
