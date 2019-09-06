package com.seoul.ttarawa.data.remote.api

import androidx.annotation.IntRange
import com.seoul.ttarawa.data.remote.response.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("service/SecndSrtpdFrcstInfoService2/ForecastTimeData")
    fun getWeather(
        @Query("serviceKey") serviceKey: String,
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int,
        @IntRange(from = 1, to = 99) @Query("numOfRows") numOfRows: Int,
        @IntRange(from = 1) @Query("pageNo") pageNo: Int,
        @Query("_type") type: String = "json"
    ): Call<WeatherResponse>
}