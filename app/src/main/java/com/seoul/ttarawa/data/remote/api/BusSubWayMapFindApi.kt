package com.seoul.ttarawa.data.remote.api

import com.seoul.ttarawa.data.remote.response.BusSubwayMapFindResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BusSubWayMapFindApi {
    @GET("api.odsay.com/v1/api/searchPubTransPathR")
    fun getBusSubwayMapFind(
        @Query("apiKey") apiKey: String,
        @Query("lang") lang: String,
        @Query("SX") sx: String,
        @Query("SY") sy: String,
        @Query("EX") ex: String,
        @Query("EY") ey: String,
        @Query("OPT") opt: String,
        @Query("SearchType") searchType: String,
        @Query("SearchPathType") searchPathType: String
    ): Call<BusSubwayMapFindResponse>
}