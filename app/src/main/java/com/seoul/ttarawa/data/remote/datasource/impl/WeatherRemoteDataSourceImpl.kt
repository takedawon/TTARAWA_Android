package com.seoul.ttarawa.data.remote.datasource.impl

import com.seoul.ttarawa.data.remote.api.WeatherApi
import com.seoul.ttarawa.data.remote.datasource.WeatherRemoteDataSource
import com.seoul.ttarawa.data.remote.response.WeatherResponse
import io.reactivex.Single

class WeatherRemoteDataSourceImpl(
    private val weatherApi: WeatherApi
) : WeatherRemoteDataSource {

    override fun getWeather(
        serviceKey: String,
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int,
        numOfRows: Int,
        pageNo: Int
    ): Single<WeatherResponse> =
        weatherApi.getWeather(
            serviceKey = serviceKey,
            baseDate = baseDate,
            baseTime = baseTime,
            nx = nx,
            ny = ny,
            numOfRows = numOfRows,
            pageNo = pageNo
        )
}

