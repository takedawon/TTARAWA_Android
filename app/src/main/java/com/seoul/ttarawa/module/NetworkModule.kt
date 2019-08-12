package com.seoul.ttarawa.module

import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.data.service.SeoulApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    // sample
    private val seoulRetrofit = createRetrofit("")

    // 서울 공공 api
    val seoulApi = seoulRetrofit.create(SeoulApi::class.java)

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