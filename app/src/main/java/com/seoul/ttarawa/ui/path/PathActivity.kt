package com.seoul.ttarawa.ui.path

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.entity.BaseSearchEntity
import com.seoul.ttarawa.data.entity.WayPointEntity
import com.seoul.ttarawa.data.local.entity.NodeEntity
import com.seoul.ttarawa.data.local.entity.PathEntity
import com.seoul.ttarawa.data.local.executor.LocalExecutor
import com.seoul.ttarawa.data.local.executor.provideLocalExecutor
import com.seoul.ttarawa.data.remote.FirebaseLeaf
import com.seoul.ttarawa.data.remote.leaf.PathLeaf
import com.seoul.ttarawa.data.remote.response.TmapWalkingResponse
import com.seoul.ttarawa.databinding.ActivityPathBinding
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.ext.getCurrentDay
import com.seoul.ttarawa.module.NetworkModule
import com.seoul.ttarawa.ui.path.dialog.PathSaveConfirmDialog
import com.seoul.ttarawa.ui.search.CategoryType
import com.seoul.ttarawa.ui.search.SearchActivity
import com.seoul.ttarawa.ui.search.TourDetailActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*

/**
 * 지도 + 경로
 */
class PathActivity : BaseActivity<ActivityPathBinding>(
    R.layout.activity_path
), OnMapReadyCallback {

    private var pathId: String = NEW_PATH

    private var firebaseUser: FirebaseUser? = null

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    private val nodeList = mutableListOf<NodeEntity>()

    private var chooseDate: String = ""

    private lateinit var locationSource: FusedLocationSource

    private lateinit var database: FirebaseDatabase

    private val localExecutor: LocalExecutor by lazy { provideLocalExecutor(this@PathActivity) }

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

        database = FirebaseDatabase.getInstance()

        chooseDate = intent.getStringExtra(EXTRA_DATE) ?: getCurrentDay()
        pathId = intent.getStringExtra(EXTRA_PATH_ID) ?: NEW_PATH

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        Timber.e(locationSource.lastLocation.toString())

        initFirebaseUser()
        initView()
    }

    private fun initFirebaseUser() {
        firebaseUser = FirebaseAuth.getInstance().currentUser
    }

    override fun initView() {
        initTransparentStatusBar()
        initMapFragment()
        initBottomSheet()

        bind {
            rvPath.apply {
                adapter = pathAdapter
                addItemDecoration(PathItemDecoration(this@PathActivity, pathAdapter))
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        binding.pathListHeaderShadow.visibility =
                            if (recyclerView.canScrollVertically(-1)) View.VISIBLE else View.GONE
                    }
                })
            }

            fabPathAdd click {
                startActivityForResult<SearchActivity>(
                    requestCode = SEARCH_REQUEST_CODE,
                    params = *arrayOf(
                        SearchActivity.EXTRA_DATE to chooseDate,
                        SearchActivity.EXTRA_LAT to latitude,
                        SearchActivity.EXTRA_LON to longitude
                    )
                )
            }

            fabPathSave click {
                showSaveConfirmDialog()
            }

            // 아이템 제거 버튼을 활성, 비활성화
            cbPathDeleteMode.setOnCheckedChangeListener { _, isChecked ->
                pathAdapter.changeDeleteMode(isChecked)
            }
        }
    }

    private fun showSaveConfirmDialog() {
        PathSaveConfirmDialog.newInstance().show(supportFragmentManager, null)
    }

    fun savePath(pathTitle: String) {
        val uid = firebaseUser?.uid

        if (uid != null) {
            savePathToRemote(uid, pathTitle)
        } else {
            savePathToLocal(pathTitle)
        }
    }

    private fun savePathToRemote(uid: String, pathTitle: String) {
        database.reference
            .child(FirebaseLeaf.DB_LEAF_PATH)
            .child(uid)
            .child(chooseDate)
            .push()
            .setValue(
                PathLeaf(
                    title = pathTitle,
                    date = chooseDate,
                    nodes = nodeList
                )
            ) { error, ref ->
                // todo 프로그레스
                if (error == null) {
                    toast(R.string.path_save_success)
                } else {
                    Timber.w("error = ${error.code}, ${error}")
                    toast(R.string.path_save_fail)
                }

            }
    }

    private fun savePathToLocal(pathTitle: String) {
        // todo 프로그레스
        try {
            val isSuccess = localExecutor.insertPath(

                path = PathEntity(
                    title = pathTitle,
                    date = chooseDate
                ),
                nodes = nodeList
            )
            if (isSuccess) {
                // todo 프로그레스
                toast(R.string.path_save_success)
                finish()
            }
        } catch (e: Exception) {
            // todo 프로그레스
            toast(R.string.path_save_fail)
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
        var mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_container) as? MapFragment
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
        bottomSheetBehavior?.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
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
     * 네이버 맵 위치 권한
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * 네이버 맵 초기화
     */
    override fun onMapReady(naverMap: NaverMap) {
        Timber.e("onMapReady")
        // 네이버맵 인스턴스 획득
        this.naverMap = naverMap

        naverMap.apply {
            // 지도 화면에 버스정류장, 지하철 표시
            setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)
            // 화면 확대시 건물 내부 활성화
            isIndoorEnabled = true
            // 화면 패딩 추가 : 중심점 변경
            setContentPadding(0, 0, 0, 250)

            locationSource = this@PathActivity.locationSource

            // 위치 변경 리스너
            addOnLocationChangeListener { location ->
                latitude = location.latitude
                longitude = location.longitude
            }

            locationTrackingMode = LocationTrackingMode.Follow
        }

        naverMap.uiSettings.apply {
            // 화면 확대시 건물 내부 층수 선택하는 뷰 활성화
            isIndoorLevelPickerEnabled = true
            // 위치 추적 버튼 활성화
            isLocationButtonEnabled = true
        }

        if (pathId != NEW_PATH) {
            bind {
                fabPathSave.visibility = View.GONE
                fabPathAdd.visibility = View.GONE
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
            }

            if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
                naverMap?.setContentPadding(0, 0, 0, getHeightMiddleOfDisplay())
                moveCameraCenterByLatLngList()
            } else if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED) {
                naverMap?.setContentPadding(0, 0, 0, 250)
                moveCameraCenterByLatLngList()
            }
        }

        if (isSavedPathLocal()) {
            initSavedPathFromLocal()
        } else {
            val uid = firebaseUser?.uid
            if (uid != null) {
                initSavedPathFromRemote(uid)
            }
        }
    }

    private fun initSavedPathFromLocal() {
        val pathAndNodes = localExecutor.getPathAndNodes(pathId.toInt())

        if (pathAndNodes != null) {
            initSavedPath(pathAndNodes.nodes)
        }
    }

    private fun initSavedPathFromRemote(uid: String) {
        database.reference
            .child(FirebaseLeaf.DB_LEAF_PATH)
            .child(uid)
            .child(chooseDate)
            .child(pathId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.w("$error")
                    toast(R.string.path_save_fail)
                }

                override fun onDataChange(path: DataSnapshot) {
                    val pathLeaf = path.getValue(PathLeaf::class.java)
                    if (pathLeaf != null) {
                        initSavedPath(pathLeaf.nodes)
                    }
                }
            })
    }

    private fun initSavedPath(nodes: List<NodeEntity>) {
        val tmpNodes = mutableListOf<NodeEntity>()

        for (node in nodes) {
            if (node.markerYn) {
                if (tmpNodes.isNotEmpty()) {
                    addPathLineInMap(tmpNodes.toList(), CategoryType.get(node.categoryCode))
                    tmpNodes.clear()
                }
                addMarkerInMap(node.toBaseSearchEntity())
            } else {
                tmpNodes.add(node)
            }
        }
        moveCameraCenterByLatLngList()
    }

    private fun isSavedPathLocal() = try {
        pathId.toInt()
        true
    } catch (e: NumberFormatException) {
        false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SEARCH_REQUEST_CODE -> {
                    val categoryCode =
                        data?.getIntExtra(TourDetailActivity.EXTRA_CATEGORY, -1) ?: -1

                    val model =
                        data?.getSerializableExtra(TourDetailActivity.EXTRA_ENTITY) as BaseSearchEntity

                    // 여기서 받아야 하는 정보들
                    // 출발시간, 도착시간, 이름, 주소, 부가정보, 카테고리
                    if (markerList.isEmpty()) {
                        // 처음에는 마커만 생성
                        Timber.e("addMarkerInMap onActivityResult")
                        addMarkerInMap(
                            searchEntity = model,
                            shouldMoveCamera = true
                        )
                    } else {
                        getRoadPath(
                            startLat = markerList.last.position.latitude,
                            startLon = markerList.last.position.longitude,
                            searchEntity = model
                        )
                    }
                    return
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getRoadPath(
        startLat: Double,
        startLon: Double,
        searchEntity: BaseSearchEntity
    ) {
        NetworkModule.tmapWalkingApi.getWalkingPath(
            appKey = "d21dbb20-70b0-4824-8b51-cf3cc6fd8aca",
            version = "1",
            startName = "123",
            endName = "456",
            startX = startLat,
            startY = startLon,
            endX = searchEntity.latitude,
            endY = searchEntity.longitude
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

                    val startLatLng = NodeEntity(
                        latitude = startLat,
                        longitude = startLon
                    )

                    // onResponse 안에서만 수집할 좌표 리스트
                    val nodes = mutableListOf<NodeEntity>()
                    nodes.add(startLatLng)

                    for (feature in it.features) {
                        val geometry = feature.geometry
                        // Log.e("테스트: type", geometry.type)

                        // 라인스트링일때만
                        if (geometry.type == "LineString") {
                            val list = geometry.coordinates
                            for (j in list.indices) { // j
                                val x = (list[j] as List<*>)[1].toString().toDouble()
                                val y = (list[j] as List<*>)[0].toString().toDouble()

                                nodes.add(
                                    NodeEntity(
                                        latitude = x,
                                        longitude = y
                                    )
                                )
                            }
                        }
                    }

                    val endLatLng =
                        NodeEntity(
                            latitude = searchEntity.latitude,
                            longitude = searchEntity.longitude
                        )
                    val categoryType = CategoryType.get(searchEntity.categoryCode)

                    nodes.add(endLatLng)
                    // 좌표리스트 맵에 반영
                    addPathLineInMap(nodes, categoryType)

                    addMarkerInMap(searchEntity)         // 마커 추가

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
        if (latLngList.isNotEmpty()) {
            naverMap?.moveCamera(
                CameraUpdate.fitBounds(
                    LatLngBounds.Builder().include(latLngList.flatten()).build(),
                    400, 400, 100, 100
                )
            )
        }
    }

    /**
     * 맵에 라인 추가
     * @param latLngs 좌표 리스트
     */
    private fun addPathLineInMap(nodes: List<NodeEntity>, category: CategoryType) {
        val latLngs = nodes.map {
            LatLng(it.latitude, it.longitude)
        }

        val pathOverlay = PathOverlay().apply {
            // 경로
            coords = latLngs
            // 두께
            width = 30
            // 테두리
            outlineWidth = 5
            outlineColor = ContextCompat.getColor(this@PathActivity, category.pathStrokeColor)
            // 라인 색
            color = ContextCompat.getColor(this@PathActivity, category.PathColor)
            // 라인 패턴
            patternImage = OverlayImage.fromResource(R.drawable.path_ic_arrow_24dp)
            patternInterval = 50
            // 세팅
            map = naverMap
        }
        nodeList.addAll(nodes)
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
     */
    private fun addMarkerInMap(
        searchEntity: BaseSearchEntity,
        shouldMoveCamera: Boolean = false
    ) {
        val latLng = LatLng(searchEntity.latitude, searchEntity.longitude)
        val marker = Marker(latLng)
        markerList.add(marker)

        pathAdapter.add(
            WayPointEntity(
                name = searchEntity.title,
                address = searchEntity.address,
                lat = searchEntity.latitude,
                lng = searchEntity.longitude,
                startTime = searchEntity.startTime,
                endTime = searchEntity.endTime
            )
        )

        nodeList.add(
            NodeEntity(
                title = searchEntity.title,
                content = searchEntity.content,
                address = searchEntity.address,
                latitude = searchEntity.latitude,
                longitude = searchEntity.longitude,
                categoryCode = searchEntity.categoryCode,
                startTime = searchEntity.startTime,
                endTime = searchEntity.endTime,
                markerYn = true
            )
        )

        val category = CategoryType.get(searchEntity.categoryCode)

        marker.apply {
            // 맵에 표시
            map = naverMap
            // 하단 텍스트
            // this.captionText = captionText

            icon = OverlayImage.fromResource(category.markerIconId)

            // 아이콘 색 변경
            // icon = MarkerIcons.GREEN
            iconTintColor = ContextCompat.getColor(this@PathActivity, category.PathColor)
        }

        if (shouldMoveCamera) {
            moveCameraCenterByLatLng(latLng)
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
        const val NEW_PATH = ""
        const val EXTRA_PATH_ID = "EXTRA_PATH_ID"
        const val EXTRA_DATE = "EXTRA_DATE"
        const val EXTRA_SUGGEST_ROUTE_KEY = "EXTRA_SUGGEST_ROUTE_KEY"
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        const val SEARCH_REQUEST_CODE = 2000
    }
}
