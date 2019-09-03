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

    private fun convertGRID_GPS(mode: Int, lat_X: Double, lng_Y: Double): LatXLngY {
        val RE = 6371.00877 // 지구 반경(km)
        val GRID = 5.0 // 격자 간격(km)
        val SLAT1 = 30.0 // 투영 위도1(degree)
        val SLAT2 = 60.0 // 투영 위도2(degree)
        val OLON = 126.0 // 기준점 경도(degree)
        val OLAT = 38.0 // 기준점 위도(degree)
        val XO = 43.0 // 기준점 X좌표(GRID)
        val YO = 136.0 // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //


        val DEGRAD = Math.PI / 180.0
        val RADDEG = 180.0 / Math.PI

        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)
        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn
        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)
        val rs = LatXLngY()

        if (mode == TO_GRID) {
            rs.lat = lat_X
            rs.lng = lng_Y
            var ra = Math.tan(Math.PI * 0.25 + lat_X * DEGRAD * 0.5)
            ra = re * sf / Math.pow(ra, sn)
            var theta = lng_Y * DEGRAD - olon
            if (theta > Math.PI) theta -= 2.0 * Math.PI
            if (theta < -Math.PI) theta += 2.0 * Math.PI
            theta *= sn
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5)
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5)
        } else {
            rs.x = lat_X
            rs.y = lng_Y
            val xn = lat_X - XO
            val yn = ro - lng_Y + YO
            var ra = Math.sqrt(xn * xn + yn * yn)
            if (sn < 0.0) {
                ra = -ra
            }
            var alat = Math.pow(re * sf / ra, 1.0 / sn)
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5

            var theta = 0.0
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0
            } else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5
                    if (xn < 0.0) {
                        theta = -theta
                    }
                } else
                    theta = Math.atan2(xn, yn)
            }
            val alon = theta / sn + olon
            rs.lat = alat * RADDEG
            rs.lng = alon * RADDEG
        }
        return rs
    }

    class LatXLngY {
        public double lat;
        public double lng;

        public double x;
        public double y;

    }
}

