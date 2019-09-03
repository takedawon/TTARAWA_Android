package com.seoul.ttarawa.module

import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.data.remote.api.SeoulApi
import com.seoul.ttarawa.data.remote.api.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single<SeoulApi> {
        createRetrofit("").create(SeoulApi::class.java)
    }

    single<WeatherApi> {
        createRetrofit("http://newsky2.kma.go.kr/").create(WeatherApi::class.java)
    }
}

private fun createRetrofit(uri: String) =
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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