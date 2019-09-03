package com.seoul.ttarawa.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.remote.api.BASE_URL
import com.seoul.ttarawa.data.remote.api.WeatherApi
import com.seoul.ttarawa.data.remote.api.WeatherRepoResponse
import com.seoul.ttarawa.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.net.URLDecoder


class MainActivity : BaseActivity<ActivityMainBinding>(
    R.layout.activity_main
) {
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        getWeather("20190903","1400","60","127",
            "10","1","json")
    }

    private fun initView() {
        bind {
            with(babMain) {
                setNavigationOnClickListener {}

                replaceMenu(R.menu.menu_main_setting)
                setOnMenuItemClickListener {
                    if (it.itemId == R.id.menu_setting) {
                        toast("setting")
                        return@setOnMenuItemClickListener true
                    }
                    return@setOnMenuItemClickListener false
                }
            }
        }
    }

    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            // 디버그 버전은 로그 보여주고
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            // 출시된 앱은 로그를 가리고
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    private fun getWeather(
        baseDate: String, baseTime: String, nx: String, ny: String,
        numOfRows: String, pageNo: String, _type: String) {

        val weatherService: WeatherApi
        val openKey =
            "5QyVwm0GQxAdvNdc%2BRHjoLPF07dmzuYnQi%2F2BiMLpPQtGwPItZolkz4GLA4PPiS7pgTGKhGBhn5GHi8t9WRcnQ%3D%3D"

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(createOkHttpClient())
            .build()

        weatherService = retrofit.create(WeatherApi::class.java)
        var call: Call<WeatherRepoResponse>? = null
        try {
            call = weatherService.getWeather(
                URLDecoder.decode(openKey, "utf-8"),
                baseDate,
                baseTime,
                nx,
                ny,
                "20",
                "1",
                "json"
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        call?.enqueue(object : Callback<WeatherRepoResponse> {

            override fun onFailure(call: Call<WeatherRepoResponse>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(
                call: Call<WeatherRepoResponse>,
                response: Response<WeatherRepoResponse>
            ) {
                if (response.isSuccessful) {
                    val repo = response.body()
                    if (repo != null) {
                        Log.e("testWeather",repo.response.body.totalCount.toString())
                    }
                }
            }
        })
    }
}

