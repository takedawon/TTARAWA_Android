package com.seoul.ttarawa.ui.home

import android.util.Log
import android.widget.Toast
import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.base.BaseViewModel
import com.seoul.ttarawa.data.StateResult
import com.seoul.ttarawa.data.repository.WeatherRepository
import timber.log.Timber
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(private val weatherRepo: WeatherRepository) : BaseViewModel() {
    private fun getWeather(baseDate: String, baseTime: String, nx: Int, ny: Int) {
        weatherRepo.getWeather(
            serviceKey = URLDecoder.decode(BuildConfig.KMA_KEY, "utf-8"),
            baseDate = baseDate,
            baseTime = baseTime,
            nx = nx,
            ny = ny,
            numOfRows = 20,
            pageNo = 1,
            onResult = {
                when (it) {
                    is StateResult.Success -> {
                        Timber.e("Success")
                        Log.e("weather", it.toString())
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
    }

    fun setLocation(lat: Int, lon: Int) {
        val formatDate: SimpleDateFormat = SimpleDateFormat("yyyyMMdd")
        val formatTime: SimpleDateFormat = SimpleDateFormat("HHmm")
        val cal: Calendar = Calendar.getInstance()

        var currentDate: String = formatDate.format(cal.time)
        var currentTime: String = formatTime.format(cal.time)
        Log.e("weatherData : ", currentDate+"/"+currentTime +"/" +lat+"/"+lon+ "/")
        getWeather(baseDate = currentDate, baseTime = currentTime, nx = lat, ny = lon)
    }
}

