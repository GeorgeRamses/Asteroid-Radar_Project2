package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val imgRetrofit = Retrofit.Builder()
    .baseUrl(Constants.IMAGE_BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidList(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") type: String,
    ): String


}

interface ImageofDayService {
    @GET("apod")
    suspend fun getPictureofDay(@Query("api_key") type: String): PictureOfDay
}

object ImagofDayApi {
    val imageofDayService: ImageofDayService by lazy {
        imgRetrofit.create(ImageofDayService::class.java)
    }
}

object AsteroidApi {
    val asteroidApiService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}