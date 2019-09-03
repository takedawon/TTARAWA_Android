package com.seoul.ttarawa.data.remote.datasource

import com.seoul.ttarawa.data.remote.response.WeatherResponse
import io.reactivex.Single

interface WeatherRemoteDataSource {

    fun getWeather(
        serviceKey: String,
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int,
        numOfRows: Int,
        pageNo: Int
    ): Single<WeatherResponse>
}
