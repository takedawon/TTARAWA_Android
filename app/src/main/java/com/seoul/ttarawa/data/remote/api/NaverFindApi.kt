package com.seoul.ttarawa.data.remote.api

import com.seoul.ttarawa.data.remote.response.NaverFindResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverFindApi {
    @GET ("v1/search/local")
    fun getNaverFind(
        @Header("X-Naver-Client-Id") clientId:String,
        @Header("X-Naver-Client-Secret") clientSecret:String,
        @Query("query") url:String,
        @Query("display") display:String,
        @Query("start") start:String,
        @Query("sort") sort:String
    ):Call<NaverFindResponse>
}