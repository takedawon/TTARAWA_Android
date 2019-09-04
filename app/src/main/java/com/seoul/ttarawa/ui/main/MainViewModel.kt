package com.seoul.ttarawa.ui.main

import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.base.BaseViewModel
import com.seoul.ttarawa.data.StateResult
import com.seoul.ttarawa.data.repository.WeatherRepository
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import java.net.URLDecoder

class MainViewModel(
    private val weatherRepo: WeatherRepository
) : BaseViewModel() {

    init {
        getWeather()
    }

    /**
     * 메인 액티비티 시작시 날씨 api 호출
     */
    private fun getWeather() {
        weatherRepo.getWeather(
            serviceKey = URLDecoder.decode(BuildConfig.KMA_KEY, "utf-8"),
            baseDate = "20190903",
            baseTime = "1400",
            nx = 60,
            ny = 127,
            numOfRows = 10,
            pageNo = 1,
            onResult = {
                when (it) {
                    is StateResult.Success -> {
                        Timber.e("Success")
                    }
                    is StateResult.Error -> {
                        Timber.e("Error")
                    }
                    StateResult.Loading -> {
                        Timber.e("Loading")
                    }
                }
            }
        )
            .addTo(compositeDisposable)
    }
}