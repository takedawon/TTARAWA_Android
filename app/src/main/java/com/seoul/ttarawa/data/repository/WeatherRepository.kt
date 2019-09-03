package com.seoul.ttarawa.data.repository

import androidx.annotation.IntRange
import com.seoul.ttarawa.data.StateResult
import com.seoul.ttarawa.data.remote.response.WeatherResponse
import io.reactivex.disposables.Disposable

interface WeatherRepository {

    fun getWeather(
        serviceKey: String,
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int,
        @IntRange(from = 1, to = 99) numOfRows: Int,
        @IntRange(from = 1) pageNo: Int,
        onResult: (result: StateResult<WeatherResponse>) -> Unit
    ): Disposable
}
