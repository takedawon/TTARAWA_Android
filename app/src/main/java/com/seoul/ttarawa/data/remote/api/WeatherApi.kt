package com.seoul.ttarawa.data.remote.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    val BASEURL = "http://newsky2.kma.go.kr/"

    @GET("/service/SecndSrtpdFrcstInfoService2/ForecastTimeData")
    abstract fun getWeather(
        @Query("serviceKey") serviceKey: String,
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: String,
        @Query("ny") ny: String,
        @Query("numOfRows") numOfRows: String,
        @Query("pageNo") pageNo: String,
        @Query("_type") type: String
    ): Call<WeatherRepoResponse>
}