package com.seoul.ttarawa.data.remote.api

import com.seoul.ttarawa.data.remote.response.TmapWalkingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TmapWalkingApi {
    @GET("tmap/routes/pedestrian")
    fun getWalkingPath(
        @Header("appKey") appKey:String,
        @Query("version") version: String,
        @Query("startX") startX: Double,
        @Query("startY") startY: Double,
        @Query("endX") endX: Double,
        @Query("endY") endY: Double,
        @Query("startName") startName: String,
        @Query("endName") endName: String
    ): Call<TmapWalkingResponse>
}