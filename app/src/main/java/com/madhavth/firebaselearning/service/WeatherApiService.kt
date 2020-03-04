package com.madhavth.firebaselearning.service

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.weatherstack.com/"

interface WeatherApiService {
    @GET("current")
    fun getWeatherAsync(
        @Query("access_key") access_key: String = "173c55d54e6399c603e6d4a36101661f",
        @Query("query") query: String = "kathmandu"): Deferred<Weather2>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()



private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

object WeatherApi {
    val RETROFIT_SERVICE: WeatherApiService by lazy { retrofit.create(WeatherApiService::class.java) }
}

