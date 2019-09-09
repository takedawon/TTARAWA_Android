package com.seoul.ttarawa.module

import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.data.remote.api.TmapWalkingApi
import com.seoul.ttarawa.data.remote.api.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    // val seoulApi: SeoulApi = createRetrofit("").create(SeoulApi::class.java)

    val weatherApi: WeatherApi = createRetrofit("http://newsky2.kma.go.kr/").create(WeatherApi::class.java)

    val tmapWalkingApi: TmapWalkingApi = createRetrofit("https://api2.sktelecom.com/").create(TmapWalkingApi::class.java)

    @JvmStatic
    private fun createRetrofit(uri: String) =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .baseUrl(uri)
            .build()

    @JvmStatic
    private fun createOkHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor(createLoggingInterceptor())
            .build()

    @JvmStatic
    private fun createLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
}