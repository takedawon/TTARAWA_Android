package com.seoul.ttarawa.module

import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.data.remote.api.SeoulApi
import com.seoul.ttarawa.data.remote.api.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    val seoulApi = createRetrofit("").create(SeoulApi::class.java)

    val weatherApi = createRetrofit("http://newsky2.kma.go.kr/").create(WeatherApi::class.java)

    private fun createRetrofit(uri: String) =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .baseUrl(uri)
            .build()

    private fun createOkHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor(createLoggingInterceptor())
            .build()

    private fun createLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
}