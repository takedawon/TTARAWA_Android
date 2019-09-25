package com.seoul.ttarawa.ui.path

import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.remote.response.TmapWalkingResponse
import com.seoul.ttarawa.databinding.ActivityPathBinding
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.module.NetworkModule
import com.seoul.ttarawa.ui.search.SearchActivity
import org.jetbrains.anko.startActivity
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

    /**
     * 바텀시트동작 제어
     */
    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    /**
     * 바텀시트에 있는 어댑터
     */
    private val pathAdapter = PathAdapter()

    /**
     * 네이버 맵 객체
     */
    private var naverMap: NaverMap? = null
    /**
     * 마커와 꺾인점의 좌표 리스트
     */
    private val latLngList = LinkedList<List<LatLng>>()
    /**
     * 맵에 보이는 패스오버레이(라인) 리스트
     */
    private val pathOverlayList = LinkedList<PathOverlay>()
    /**
     * 맵에 보이는 마커 리스트
     */
    private val markerList = LinkedList<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

        getRoadPath(37.47276907, 126.89075388, 37.47371341, 126.89094828)

        getRoadPath(37.47371341, 126.89094828, 37.48371341, 126.89575388)
    }

    override fun initView() {
        initTransparentStatusBar()
        initMapFragment()
        initBottomSheet()

        bind {
            rvPath.adapter = pathAdapter

            fabPathAdd click {
                startActivity<SearchActivity>()
            }

            // 아이템 제거 버튼을 활성, 비활성화
            cbPathDeleteMode.setOnCheckedChangeListener { _, isChecked ->
                pathAdapter.changeDeleteMode(isChecked)
            }
        }
    }

    private fun initTransparentStatusBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // 스테이터스바 글자 색 어둡게 변경, 23 이상
        var flag = window.decorView.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flag = flag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.decorView.systemUiVisibility = flag
        window.statusBarColor = Color.TRANSPARENT
    }

    /**
     * @see OnMapReadyCallback.onMapReady
     */
    private fun initMapFragment() {
        var mapFragment = supportFragmentManager.findFragmentById(R.id.map_container) as? MapFragment
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.map_container, mapFragment).commit()
        }
        // 맵프래그먼트와 OnMapReadyCallback 연결
        mapFragment?.getMapAsync(this@PathActivity)
    }

    private fun initBottomSheet() {
        // 바텀시트 최대 높이 조정
        binding.clPathBottomSheet.layoutParams.height = getHeightMiddleOfDisplay()
        // 바텀시트 초기화
        bottomSheetBehavior = BottomSheetBehavior.from(binding.clPathBottomSheet)
        // 바텀시트 콜백
        bottomSheetBehavior?.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                /*ignored*/
            }

            // 바텀시트 위치 변경시 네이버 맵 중앙 변경
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    naverMap?.setContentPadding(0, 0, 0, getHeightMiddleOfDisplay())
                    moveCameraCenterByLatLngList()
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    naverMap?.setContentPadding(0, 0, 0, 250)
                    moveCameraCenterByLatLngList()
                }
            }
        })
    }

    /**
     * @return 디스플레이의 중간 사이즈
     */
    private fun getHeightMiddleOfDisplay(): Int {
        val point = Point()
        this@PathActivity.windowManager.defaultDisplay.getSize(point)
        return point.y / 2
    }

    /**
     * 네이버 맵 초기화
     */
    override fun onMapReady(naverMap: NaverMap) {
        // 네이버맵 인스턴스 획득
        this.naverMap = naverMap

        naverMap.apply {
            // 지도 화면에 버스정류장, 지하철 표시
            setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)
            // 화면 확대시 건물 내부 활성화
            isIndoorEnabled = true
            // 화면 패딩 추가 : 중심점 변경
            setContentPadding(0, 0, 0, 250)
        }

        naverMap.uiSettings.apply {
            // 화면 확대시 건물 내부 층수 선택하는 뷰 활성화
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

                    // fixme 첫 시작시에는 마커만 보이게 될 것임. 수정이 필요한 부분
                    if (latLngList.isEmpty()) {
                        addMarkerInMap(startLatLng, "시작")           // 마커 추가
                    }

                    // onResponse 안에서만 수집할 좌표 리스트
                    val latLngs = mutableListOf<LatLng>()
                    latLngs.add(startLatLng)

                    for (feature in it.features) {
                        val geometry = feature.geometry
                        Log.e("테스트: type", geometry.type)

                        // 라인스트링일때만
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
                    addMarkerInMap(endLatLng, "종료")         // 마커 추가

                    latLngs.add(endLatLng)
                    // 좌표리스트 맵에 반영
                    addPathLineInMap(latLngs)
                    // 맵 이동
                    moveCameraCenterByLatLngList()
                }
            }
        })
    }

    /**
     * 특정 좌표를 기준으로 중심점 이동
     * @param latLng 좌표
     */
    private fun moveCameraCenterByLatLng(latLng: LatLng) {
        naverMap?.moveCamera(CameraUpdate.scrollTo(latLng))
    }

    /**
     * 전체 [latLngList] 를 기준으로 중심점 변경
     */
    private fun moveCameraCenterByLatLngList() {
        naverMap?.moveCamera(
            CameraUpdate.fitBounds(
                LatLngBounds.Builder().include(latLngList.flatten()).build(),
                400, 400, 400, 400
            )
        )
    }

    /**
     * 맵에 라인 추가
     * @param latLngs 좌표 리스트
     */
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

    /**
     * 전체 경로 제거
     */
    private fun clearAllPathLine() {
        pathOverlayList.forEach { it.map = null }
        pathOverlayList.clear()
        latLngList.clear()
    }


    /**
     * 특정 경로 제거
     */
    private fun clearPathLineInMap(position: Int) {
        pathOverlayList[position].map = null
        latLngList.remove(latLngList[position])
        pathOverlayList.remove(pathOverlayList[position])
    }

    /**
     * 마커 추가
     * @param latLng 좌표
     * @param captionText 마커 하단에 보이는 텍스트
     */
    private fun addMarkerInMap(latLng: LatLng, captionText: String) {
        val marker = Marker(latLng)
        markerList.add(marker)

        marker.apply {
            // 맵에 표시
            map = naverMap
            // 하단 텍스트
            this.captionText = captionText

            icon = OverlayImage.fromResource(R.drawable.path_ic_cafe_marker)

            // 아이콘 색 변경
            // icon = MarkerIcons.GREEN
            iconTintColor = Color.BLUE
        }
    }

    /**
     * 전체 마커 제거
     */
    private fun clearAllMarker() {
        markerList.forEach { it.map = null }
        markerList.clear()
    }

    /**
     * 특정 마커 제거
     */
    private fun clearMarkerInMap(position: Int) {
        markerList[position].map = null
        markerList.remove(markerList[position])
    }

    companion object {
        const val EXTRA_DATE = "EXTRA_DATE"
        const val EXTRA_SUGGEST_ROUTE_KEY = "EXTRA_SUGGEST_ROUTE_KEY"
    }
}