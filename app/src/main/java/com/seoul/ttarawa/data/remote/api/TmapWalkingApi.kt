package com.seoul.ttarawa.data.remote.api

import com.seoul.ttarawa.data.remote.response.TmapWalkingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * api 에서 x 좌표와 y 좌표를 반대로 입력받음
 */
interface TmapWalkingApi {
    @GET("tmap/routes/pedestrian")
    fun getWalkingPath(
        @Header("appKey") appKey:String,
        @Query("version") version: String,
        @Query("startY") startX: Double,
        @Query("startX") startY: Double,
        @Query("endY") endX: Double,
        @Query("endX") endY: Double,
        @Query("startName") startName: String,
        @Query("endName") endName: String
    ): Call<TmapWalkingResponse>
}