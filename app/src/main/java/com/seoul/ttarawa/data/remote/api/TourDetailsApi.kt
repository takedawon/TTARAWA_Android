package com.seoul.ttarawa.data.remote.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TourDetailsApi {
    @GET("openapi/service/rest/KorService/detailInfo")
    fun getTourDetail(
        @Query("serviceKey") serviceKey:String,
        @Query("numOfRows") numOfRows:Int,
        @Query("pageNo") pageNo:Int,
        @Query("MobileOS") MobileOS:String,
        @Query("MobileApp") MobileApp:String,
        @Query("contentId") contentId:Int,
        @Query("contentTypeId") contentTypeId:Int,
        @Query("_type") _type:String
    ): Call<TourDetailsApi>
}