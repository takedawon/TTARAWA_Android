package com.seoul.ttarawa.data.remote.api

import com.seoul.ttarawa.data.remote.response.EventDetailsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EventDetailsApi {
    @GET("openapi/service/rest/KorService/searchFestival")
    fun getEventDetail(
        @Query("serviceKey") serviceKey:String,
        @Query("numOfRows") numOfRows:Int,
        @Query("pageNo") pageNo:Int,
        @Query("MobileOS") MobileOS:String,
        @Query("MobileApp") MobileApp:String,
        @Query("arrange") arrange:String,
        @Query("listYN") listYN:String,
        @Query("areaCode") areaCode:Int,
        @Query("eventStartDate") eventStartDate:Int,
        @Query("eventEndDate") eventEndDate:Int,
        @Query("_type") _type:String
        ):Call<EventDetailsResponse>
}