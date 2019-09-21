package com.seoul.ttarawa.data.remote.api

import com.seoul.ttarawa.data.remote.response.TourImageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TourImageApi {
    @GET("openapi/service/rest/KorService/detailImage")
    fun getTourImage(
        @Query("serviceKey") serviceKey: String,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") mobileOS: String,
        @Query("MobileApp") mobileApp: String,
        @Query("contentId") contentId: Int,
        @Query("imageYN") imageYN: String,
        @Query("subImageYN") subImageYN: String,
        @Query("_type") _type: String = "json"
    ): Call<TourImageResponse>
}
