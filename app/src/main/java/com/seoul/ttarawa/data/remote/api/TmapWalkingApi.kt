package com.seoul.ttarawa.data.remote.api

import com.seoul.ttarawa.data.remote.response.TmapWalkingResponse
import com.seoul.ttarawa.data.remote.response.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TmapWalkingApi {
    @GET("tmap/routes/pedestrian")
    fun getWalkingPath(
        @Header("appKey") appKey:String,
        @Query("version") version: String,
        @Query("startX") startX: String,
        @Query("startY") startY: String,
        @Query("endX") endX: String,
        @Query("endY") endY: String,
        @Query("startName") startName: String,
        @Query("endName") endName: String
    ): Call<TmapWalkingResponse>
}