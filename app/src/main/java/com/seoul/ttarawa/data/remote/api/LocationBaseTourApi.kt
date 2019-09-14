package com.seoul.ttarawa.data.remote.api

import com.seoul.ttarawa.data.remote.response.LocationBaseTourResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface LocationBaseTourApi {
    @GET("openapi/service/rest/KorService/locationBasedList")
    fun getLocationBaseTour(
        @Query("serviceKey") serviceKey: String,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") mobileOS: String,
        @Query("MobileApp") mobileApp: String,
        @Query("arrange") arrange: String,
        @Query("contentTypeId") contentTypeId: Int,
        @Query("mapX") mapX: String,
        @Query("mapY") mapY: String,
        @Query("radius") radius: Int,
        @Query("listYN") listYN: String,
        @Query("_type") _type: String = "json"
    ): Call<LocationBaseTourResponse>
}