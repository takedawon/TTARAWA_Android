package com.seoul.ttarawa.data.remote.api

import com.seoul.ttarawa.data.remote.response.AreaBasedListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AreaBasedListApi {
    @GET("openapi/service/rest/KorService/areaBasedList")
    fun getAreaBasedList(
        @Query("serviceKey") serviceKey:String,
        @Query("pageNo") pageNo:Int,
        @Query("numOfRows") numOfRows:Int,
        @Query("MobileApp") MobileApp:String,
        @Query("MobileOS") MobileOS:String,
        @Query("arrange") arrange:String,
        @Query("cat1") cat1:String,
        @Query("cat2") cat2:String,
        @Query("cat3") cat3:String,
        @Query("contentTypeId") contentTypeId:String,
        @Query("sigunguCode") sigunguCode:String,
        @Query("areaCode") areaCode:String,
        @Query("listYN") listYN:String,
        @Query("_type") _type:String
    ): Call<AreaBasedListResponse>
}

/*
http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?
serviceKey=5QyVwm0GQxAdvNdc%2BRHjoLPF07dmzuYnQi%2F2BiMLpPQtGwPItZolkz4GLA4PPiS7pgTGKhGBhn5GHi8t9WRcnQ%3D%3D&
pageNo=1&
numOfRows=20&
MobileApp=AppTest&
MobileOS=ETC&
arrange=P&
cat1=A02&
contentTypeId=14&
areaCode=1&
listYN=Y
 */