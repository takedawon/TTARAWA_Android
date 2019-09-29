package com.seoul.ttarawa.data.remote.api

import com.seoul.ttarawa.data.remote.response.GeoCodingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GeoCodingApi {
    @GET("v2/local/geo/coord2regioncode.json")
    fun getGeoCoding(
        @Header("Authorization") Authorization:String,
        @Query("x") x: Double,
        @Query("y") y: Double
    ): Call<GeoCodingResponse>
}