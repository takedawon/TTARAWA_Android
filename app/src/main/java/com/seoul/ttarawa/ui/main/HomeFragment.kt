package com.seoul.ttarawa.ui.main

import android.os.Bundle
import android.util.Log
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseFragment
import com.seoul.ttarawa.data.remote.response.WeatherResponse
import com.seoul.ttarawa.databinding.FragmentHomeBinding
import com.seoul.ttarawa.module.NetworkModule
import com.seoul.ttarawa.util.LocationUtil
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.LocationProvider
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    R.layout.fragment_home
) {

    private val provider: LocationProvider by lazy { createProvider() }

    private lateinit var smartLocation: SmartLocation

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()

        requestPermission()
    }

    override fun initView() {

    }

    private fun requestPermission() {
        TedPermission.with(activity!!)
            .setPermissionListener(createPermissionListener())
            .setRationaleMessage("앱의 기능을 사용하기 위해서는 권한이 필요합니다.")
            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            .setPermissions(
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION
                , android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .check()
    }

    private fun createPermissionListener(): PermissionListener {
        return object : PermissionListener {
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                // todo
            }

            override fun onPermissionGranted() {
                // 권한 허가시 실행 할 내용
                smartLocation = SmartLocation.Builder(activity!!).logging(true).build()

                smartLocation.location(provider).start {
                    val lat: Double = it.latitude
                    val lon: Double = it.longitude
                    val currentLocation: LocationUtil.Grid =
                        LocationUtil.convertToGrid(lat = lat, lng = lon)
                    setLocation(currentLocation.nx, currentLocation.ny)
                }
            }
        }
    }

    private fun createProvider(): LocationProvider {
        return LocationGooglePlayServicesProvider().apply {
            setCheckLocationSettings(true)
        }
    }

    fun setLocation(lat: Int, lon: Int) {
        val formatDate = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        val formatTime = SimpleDateFormat("HHmm", Locale.KOREA)
        val cal: Calendar = Calendar.getInstance()

        val currentDate: String = formatDate.format(cal.time)
        val currentTime: String = formatTime.format(cal.time)

        Log.e("weatherData : ", "$currentDate/$currentTime/$lat/$lon/")
        getWeather(baseDate = currentDate, baseTime = currentTime, nx = lat, ny = lon)
    }

    private fun getWeather(baseDate: String, baseTime: String, nx: Int, ny: Int) {
        NetworkModule.weatherApi.getWeather(
            serviceKey = URLDecoder.decode(BuildConfig.KMA_KEY, "utf-8"),
            baseDate = baseDate,
            baseTime = baseTime,
            nx = nx,
            ny = ny,
            numOfRows = 20,
            pageNo = 1
        )
            .enqueue(object : Callback<WeatherResponse?> {
                override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<WeatherResponse?>,
                    response: Response<WeatherResponse?>
                ) {
                    var ptyValue = 0
                    var skyValue = 0
                    var switch = 0
                    for (i in 0..19) {
                        response.body()?.let {
                            if (it.response.body.items.item[i].category == "PTY" && switch == 0) {
                                ptyValue = it.response.body.items.item[i].fcstValue
                                switch = 1
                            } else if (it.response.body.items.item[i].category == "SKY" && switch == 1) {
                                skyValue = it.response.body.items.item[i].fcstValue
                                switch = 2
                            }
                        }
                        if (switch == 2)
                            break
                    }


                }
            })
    }

    override fun onDestroy() {
        smartLocation.location(provider).stop()
        super.onDestroy()
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}