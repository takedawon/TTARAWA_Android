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
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.module.NetworkModule
import com.seoul.ttarawa.util.LocationUtil
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    R.layout.fragment_home
) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()

        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            }

            override fun onPermissionGranted() {
                // 권한 허가시 실행 할 내용
                val provider: LocationGooglePlayServicesProvider? =
                    LocationGooglePlayServicesProvider()
                provider?.setCheckLocationSettings(true)
                val smartLocation = SmartLocation.Builder(
                    activity!!
                ).logging(true).build()
                smartLocation.location(provider).start(OnLocationUpdatedListener {
                    Log.i("location : ", it.longitude.toString() + "," + it.latitude)
                })
                val lastLocation = SmartLocation.with(activity!!).location().lastLocation
                if (lastLocation != null) {
                    toast(lastLocation.latitude.toString() + " " + lastLocation.longitude.toString())
                    val lat: Double = lastLocation.latitude
                    val lon: Double = lastLocation.longitude
                    val currentLocation: LocationUtil.Grid =
                        LocationUtil.convertToGrid(lat = lat, lng = lon)
                    setLocation(currentLocation.nx, currentLocation.ny)
                }

                smartLocation.location(provider).stop()

            }
        }

        TedPermission.with(activity!!)
            .setPermissionListener(permissionListener)
            .setRationaleMessage("앱의 기능을 사용하기 위해서는 권한이 필요합니다.")
            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            .setPermissions(
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION
                , android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .check()
    }

    override fun initView() {

    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
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

                override fun onResponse(call: Call<WeatherResponse?>, response: Response<WeatherResponse?>) {

                }
            })
    }

    fun setLocation(lat: Int, lon: Int) {
        val formatDate: SimpleDateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        val formatTime: SimpleDateFormat = SimpleDateFormat("HHmm", Locale.KOREA)
        val cal: Calendar = Calendar.getInstance()

        val currentDate: String = formatDate.format(cal.time)
        val currentTime: String = formatTime.format(cal.time)

        Log.e("weatherData : ", "$currentDate/$currentTime/$lat/$lon/")
        getWeather(baseDate = currentDate, baseTime = currentTime, nx = lat, ny = lon)
    }
}