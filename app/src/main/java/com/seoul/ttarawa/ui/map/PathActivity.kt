package com.seoul.ttarawa.ui.map

import android.os.Bundle
import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.remote.api.TmapWalkingApi
import com.seoul.ttarawa.data.remote.response.TmapWalkingResponse
import com.seoul.ttarawa.data.remote.response.WeatherResponse
import com.seoul.ttarawa.databinding.ActivityPathBinding
import com.seoul.ttarawa.module.NetworkModule
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * 지도 + 경로
 */
class PathActivity : BaseActivity<ActivityPathBinding>(
    R.layout.activity_path
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        bind {
            rlPathMap.addView(MapView(this@PathActivity).apply {
                isHDMapTileEnabled = true
            })


        }
    }

    private fun getRoadPath(startLat:String, startLon:String, endLat:String, endLon:String) {
        NetworkModule.tmapWalkingApi.getWalkingPath(
            appKey = BuildConfig.TMAP_KEY,
            version = "1",
            startName = "123",
            endName = "456",
            startX = startLon,
            startY = startLat,
            endX = endLon,
            endY = endLat
        ).enqueue(object:Callback<TmapWalkingResponse?> {
            override fun onFailure(call: Call<TmapWalkingResponse?>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<TmapWalkingResponse?>,
                response: Response<TmapWalkingResponse?>
            ) {

            }
        })
    }

    companion object {
        const val EXTRA_DATE = "EXTRA_DATE"
        const val EXTRA_SUGGEST_ROUTE_KEY = "EXTRA_SUGGEST_ROUTE_KEY"
    }
}
