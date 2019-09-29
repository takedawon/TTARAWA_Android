package com.seoul.ttarawa.module

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.data.remote.api.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    // val seoulApi: SeoulApi = createRetrofit("").create(SeoulApi::class.java)

    val weatherApi: WeatherApi = createRetrofit("http://newsky2.kma.go.kr/").create(WeatherApi::class.java)

    val tmapWalkingApi: TmapWalkingApi = createRetrofit("https://api2.sktelecom.com/").create(TmapWalkingApi::class.java)

    val locationBaseTourApi: LocationBaseTourApi = createRetrofit("http://api.visitkorea.or.kr/").create(LocationBaseTourApi::class.java)

    val tourImageApi: TourImageApi = createRetrofit("http://api.visitkorea.or.kr/").create(TourImageApi::class.java)

    val tourDetailsApi: TourDetailsApi = createRetrofit("http://api.visitkorea.or.kr/").create(TourDetailsApi::class.java)

    val eventDetailsApi: EventDetailsApi = createRetrofit("http://api.visitkorea.or.kr/").create(EventDetailsApi::class.java)

    val kobisApi: KobisApi = createRetrofit("http://www.kobis.or.kr/kobisopenapi/webservice/rest/").create(KobisApi::class.java)

    @JvmStatic
    private fun createRetrofit(uri: String) =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .baseUrl(uri)
            .build()

    @JvmStatic
    private fun createNetworkInterceptor() = StethoInterceptor()

    @JvmStatic
    private fun createOkHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor(createLoggingInterceptor())
            .addNetworkInterceptor(createNetworkInterceptor())
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