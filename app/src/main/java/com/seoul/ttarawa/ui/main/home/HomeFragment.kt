package com.seoul.ttarawa.ui.main.home

import android.os.Bundle
import android.util.Log
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseFragment
import com.seoul.ttarawa.data.entity.SuggestRouteModel
import com.seoul.ttarawa.data.entity.WeatherModel
import com.seoul.ttarawa.data.remote.response.WeatherResponse
import com.seoul.ttarawa.databinding.FragmentHomeBinding
import com.seoul.ttarawa.ext.hide
import com.seoul.ttarawa.ext.show
import com.seoul.ttarawa.module.NetworkModule
import com.seoul.ttarawa.ui.path.PathActivity
import com.seoul.ttarawa.util.LocationUtil
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.LocationProvider
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider
import org.jetbrains.anko.support.v4.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    R.layout.fragment_home
) {

    private val provider: LocationProvider by lazy { createProvider() }

    private val homeAdapter: HomeAdapter by lazy { createHomeAdapter() }

    private lateinit var smartLocation: SmartLocation

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()

        requestPermission()
    }

    override fun initView() {
        initHomeAdapter()
    }

    private fun initHomeAdapter() {
        binding.rvHome.adapter = homeAdapter
    }

    private fun createHomeAdapter(): HomeAdapter =
        HomeAdapter().apply {
            setOnClickSuggestRoute { startPathActivityWithSuggestRouteKey(it) }
        }

    private fun startPathActivityWithSuggestRouteKey(routeKey: String) {
        startActivity<PathActivity>(PathActivity.EXTRA_SUGGEST_ROUTE_KEY to routeKey)
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
                showProgressBar()

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
        // 30 분 전 시간을 받기 위함
        cal.add(Calendar.MINUTE, -30)

        val currentDate: String = formatDate.format(cal.time)
        val currentTime: String = formatTime.format(cal.time)

        Log.e("weatherData : ", "$currentDate/$currentTime/$lat/$lon/")
        getWeather(baseDate = currentDate, baseTime = currentTime, nx = lat, ny = lon)
    }

    fun setWeatherView(ptyValue: Int, skyValue: Int) {
        val text = if (ptyValue == 0 && skyValue == 1)
            "어디가기 좋은 날씨네요!"
        else if (ptyValue == 0 && (skyValue == 3 || skyValue == 4))
            "구름이 많은 날씨네요!"
        else if (ptyValue == 1 || ptyValue == 2 || ptyValue == 4)
            "비가 오고 있어요! 우산 꼭 챙기세요!"
        else
            "눈이 오고 있어요! 우산 꼭 챙기세요!"

        hideProgressBar()

        // todo 테스트 데이터
        homeAdapter.replaceAll(
            listOf(
                WeatherModel("사용자님", text),
                SuggestRouteModel(
                    routeKey = "12345678",
                    title = "명륜진사갈비",
                    subTitle = "무한으로 즐겨요",
                    textColor = android.R.color.white,
                    imgUri = "https://avatars3.githubusercontent.com/u/36095102?s=460&v=4"
                ),
                SuggestRouteModel(
                    routeKey = "12345678",
                    title = "다원이가다해줄거야",
                    subTitle = "ㅎㅎㅎㅎㅎㅎㅎ",
                    textColor = android.R.color.black,
                    imgUri = "https://avatars2.githubusercontent.com/u/44185071?s=460&v=4"
                ),
                SuggestRouteModel(
                    routeKey = "12345678",
                    title = "야경이 밫나는 밤",
                    subTitle = "오늘 함께 떠나볼까요?",
                    textColor = android.R.color.white,
                    imgUri = "https://avatars3.githubusercontent.com/u/36095102?s=460&v=4"
                ),
                SuggestRouteModel(
                    routeKey = "12345678",
                    title = "서울 경복궁 코스",
                    subTitle = "가족 나들이 완성!",
                    textColor = android.R.color.black,
                    imgUri = "https://avatars2.githubusercontent.com/u/44185071?s=460&v=4"
                )
            )
        )
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
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<WeatherResponse?>,
                    response: Response<WeatherResponse?>
                ) {
                    Timber.e(response.message())
                    var ptyValue = 0.0
                    var skyValue = 0.0
                    var switch = 0
                    for (i in 0..19) {
                        response.body()?.response?.body?.items?.let {
                            if (it.item[i].category == "PTY" && switch == 0) {
                                ptyValue = it.item[i].fcstValue
                                switch = 1
                            } else if (it.item[i].category == "SKY" && switch == 1) {
                                skyValue = it.item[i].fcstValue
                                switch = 2
                            }
                        }
                        if (switch == 2)
                            break
                    }
                    setWeatherView(ptyValue.toInt(), skyValue.toInt())
                }
            })
    }

    override fun onDestroy() {
        smartLocation.location(provider).stop()
        super.onDestroy()
    }


    private fun showProgressBar() {
        binding.pbHome.show()
    }

    private fun hideProgressBar() {
        binding.pbHome.hide()
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}