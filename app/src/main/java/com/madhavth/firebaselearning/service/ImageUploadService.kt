package com.madhavth.firebaselearning.service

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST
import retrofit2.http.Path

val BASE_URL_UPLOAD =  "https://up.flickr.com/services/upload/"

interface ImageUploadService {
    @POST("3/image")
    fun reverseImageSearch(@Path("url") url: String)

}


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()



private val retrofit2 = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL_UPLOAD)
    .build()

object SearchApi {
    val RETROFIT_SERVICE: ImageUploadService by lazy { retrofit2.create(ImageUploadService::class.java) }
}
