package com.seoul.ttarawa.ui.map

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.MarkerIcons
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.remote.response.TmapWalkingResponse
import com.seoul.ttarawa.databinding.ActivityPathBinding
import com.seoul.ttarawa.module.NetworkModule
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * 지도 + 경로
 */
class PathActivity : BaseActivity<ActivityPathBinding>(
    R.layout.activity_path
), OnMapReadyCallback {

    private var naverMap: NaverMap? = null
    private val latLngList = LinkedList<List<LatLng>>()
    private val pathOverlayList = LinkedList<PathOverlay>()
    private val markerList = LinkedList<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        initMapFragment()
    }

    private fun initMapFragment() {
        var mapFragment = supportFragmentManager.findFragmentById(R.id.map_container) as? MapFragment
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.map_container, mapFragment).commit()
        }
        mapFragment?.getMapAsync(this@PathActivity)

        getRoadPath(37.47276907, 126.89075388, 37.47371341, 126.89094828)

        getRoadPath(37.47371341, 126.89094828, 37.48371341, 126.89575388)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        naverMap.apply {
            setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)
            isIndoorEnabled = true
            setContentPadding(0, 0, 0, 250)
        }

        naverMap.uiSettings.apply {
            isIndoorLevelPickerEnabled = true
        }

    }

    private fun getRoadPath(
        startLat: Double,
        startLon: Double,
        endLat: Double,
        endLon: Double
    ) {
        NetworkModule.tmapWalkingApi.getWalkingPath(
            appKey = "d21dbb20-70b0-4824-8b51-cf3cc6fd8aca",
            version = "1",
            startName = "123",
            endName = "456",
            startX = startLon,
            startY = startLat,
            endX = endLon,
            endY = endLat
        ).enqueue(object : Callback<TmapWalkingResponse?> {
            override fun onFailure(call: Call<TmapWalkingResponse?>, t: Throwable) {
                t.printStackTrace()
                toast("실패")
                Log.e("경로에러", call.toString())
                Log.e("경로에러", t.localizedMessage)

            }

            override fun onResponse(
                call: Call<TmapWalkingResponse?>,
                response: Response<TmapWalkingResponse?>
            ) {
                toast("성공")

                response.body()?.let {

                    val startLatLng = LatLng(startLat, startLon)
                    naverMap?.moveCamera(CameraUpdate.scrollTo(startLatLng))

                    if (latLngList.isEmpty()) {
                        addMarkerInMap(startLatLng, "시작")
                    }

                    val latLngs = mutableListOf<LatLng>()
                    latLngs.add(startLatLng)

                    for (feature in it.features) {
                        val geometry = feature.geometry
                        Log.e("테스트: type", geometry.type)

                        if (geometry.type == "LineString") {
                            val list = geometry.coordinates
                            for (j in list.indices) { // j
                                val x = (list[j] as List<*>)[1].toString().toDouble()
                                val y = (list[j] as List<*>)[0].toString().toDouble()
                                latLngs.add(LatLng(x, y))
                            }
                        }
                    }

                    val endLatLng = LatLng(endLat, endLon)
                    addMarkerInMap(endLatLng, "종료")

                    latLngs.add(endLatLng)

                    addPathLineInMap(latLngs)
                    moveCameraCenterFromLatLngList()
                }
            }
        })
    }

    private fun moveCameraCenterFromLatLngList() {
        naverMap?.moveCamera(
            CameraUpdate.fitBounds(
                LatLngBounds.Builder().include(latLngList.flatten()).build(),
                400, 400, 400, 400
            )
        )
    }

    private fun addPathLineInMap(latLngs: List<LatLng>) {
        val pathOverlay = PathOverlay().apply {
            // 경로
            coords = latLngs
            // 두께
            width = 30
            // 테두리
            outlineWidth = 5
            outlineColor = ContextCompat.getColor(this@PathActivity, R.color.colorAccent)
            // 라인 색
            color = Color.GREEN
            // 라인 패턴
            patternImage = OverlayImage.fromResource(R.drawable.path_ic_arrow_24dp)
            patternInterval = 50
            // 세팅
            map = naverMap
        }
        latLngList.add(latLngs)
        pathOverlayList.add(pathOverlay)
    }

    private fun clearAllPathLine() {
        pathOverlayList.forEach { it.map = null }
        pathOverlayList.clear()
        latLngList.clear()
    }


    private fun clearPathLineInMap(position: Int) {
        pathOverlayList[position].map = null
        latLngList.remove(latLngList[position])
        pathOverlayList.remove(pathOverlayList[position])
    }

    private fun addMarkerInMap(latLng: LatLng, captionText: String) {
        val marker = Marker(latLng)
        markerList.add(marker)

        marker.apply {
            map = naverMap
            this.captionText = captionText
            // icon = OverlayImage.fromResource(R.drawable.search_ic_movie_checked)
            icon = MarkerIcons.GREEN
            iconTintColor = Color.RED
        }
    }

    private fun clearAllMarker() {
        markerList.forEach { it.map = null }
        markerList.clear()
    }


    private fun clearMarkerInMap(position: Int) {
        markerList[position].map = null
        markerList.remove(markerList[position])
    }

    companion object {
        const val EXTRA_DATE = "EXTRA_DATE"
        const val EXTRA_SUGGEST_ROUTE_KEY = "EXTRA_SUGGEST_ROUTE_KEY"
    }
}