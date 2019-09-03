package com.seoul.ttarawa.data.repository.impl

import androidx.annotation.IntRange
import com.seoul.ttarawa.data.StateResult
import com.seoul.ttarawa.data.remote.datasource.WeatherRemoteDataSource
import com.seoul.ttarawa.data.remote.response.WeatherResponse
import com.seoul.ttarawa.data.repository.WeatherRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class WeatherRepositoryImpl(
    private val remote: WeatherRemoteDataSource
) : WeatherRepository {

    override fun getWeather(
        serviceKey: String,
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int,
        numOfRows: Int,
        pageNo: Int,
        onResult: (result: StateResult<WeatherResponse>) -> Unit
    ): Disposable {
        onResult(StateResult.loading())

        return remote.getWeather(
            serviceKey = serviceKey,
            baseDate = baseDate,
            baseTime = baseTime,
            nx = nx,
            ny = ny,
            numOfRows = numOfRows,
            pageNo = pageNo
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    onResult(StateResult.error(it))
                },
                onSuccess = {
                    onResult(StateResult.success(it))
                }
            )
    }

}
