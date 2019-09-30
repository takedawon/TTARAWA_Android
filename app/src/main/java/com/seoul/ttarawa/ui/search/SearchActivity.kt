package com.seoul.ttarawa.ui.search

import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.Tm128
import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.entity.BaseSearchEntity
import com.seoul.ttarawa.data.entity.LocationTourModel
import com.seoul.ttarawa.data.entity.NaverFindModel
import com.seoul.ttarawa.data.remote.response.*
import com.seoul.ttarawa.databinding.ActivitySearchBinding
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.ext.getCurrentDay
import com.seoul.ttarawa.ext.hide
import com.seoul.ttarawa.ext.show
import com.seoul.ttarawa.module.NetworkModule
import com.seoul.ttarawa.ui.search.timepicker.TimePickerDialogFragment
import org.jetbrains.anko.dip
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.Serializable
import java.net.URLDecoder

/**
 * 검색 화면
 * 조건을 추가하고, 검색을 한다
 * 검색 결과를 가지고 PathFragment 로 이동
 */
class SearchActivity : BaseActivity<ActivitySearchBinding>(
    R.layout.activity_search
) {

    private var longitude: Double = 37.568477
    private var latitude: Double = 126.981611
    private var chooseDate: String = ""

    private var category = CategoryType.NONE_SELECTED

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    private val searchAdapter by lazy { createSearchAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chooseDate = intent.getStringExtra(EXTRA_DATE) ?: getCurrentDay()
        latitude = intent.getDoubleExtra(EXTRA_LAT, 126.981611)
        longitude = intent.getDoubleExtra(EXTRA_LON, 37.568477)

        initView()
        Timber.e(latitude.toString())
    }

    override fun initView() {
        initActionBar()
        initBottomSheetBehavior()

        bind {
            rvSearch.apply {
                adapter = searchAdapter

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        searchListHeaderShadow.visibility =
                            if (recyclerView.canScrollVertically(-1)) View.VISIBLE else View.GONE
                    }
                })
            }

            cgSearch.setOnCheckedChangeListener { chipGroup, checkedId ->
                if (checkedId == CategoryType.NONE_SELECTED.code) {
                    category = CategoryType.get(checkedId)
                    return@setOnCheckedChangeListener
                }


                for (i in 0 until chipGroup.childCount) {
                    val chip = chipGroup.getChildAt(i)
                    if (chip.id == checkedId) {
                        category = CategoryType.get(i)
                        Timber.e("$category")


                        when (category) {
                            CategoryType.CULTURE,
                            CategoryType.EXHIBITION,
                            CategoryType.TOUR,
                            CategoryType.SPORTS,
                            CategoryType.SHOPPING->
                                binding.tietSearch.visibility=View.GONE
                            CategoryType.MOVIE,
                            CategoryType.WAY_POINT,
                            CategoryType.CAFE->
                                binding.tietSearch.visibility=View.VISIBLE
                        }

                        return@setOnCheckedChangeListener
                    }
                }
            }

            vStartTime click {
                TimePickerDialogFragment.newInstance(
                    mode = TimePickerDialogFragment.MODE_START,
                    startTime = tietStartTime.text.toString(),
                    endTime = tietEndTime.text.toString()
                ).show(supportFragmentManager, null)
            }

            vEndTime click {
                TimePickerDialogFragment.newInstance(
                    mode = TimePickerDialogFragment.MODE_END,
                    startTime = tietStartTime.text.toString(),
                    endTime = tietEndTime.text.toString()
                ).show(supportFragmentManager, null)
            }

            btnSearch click {
                requestSearch()
            }

            ivSearchArrow click {
                changeBottomSheetState()
            }
        }
    }

    private fun initBottomSheetBehavior() {
        binding.llSearchBottomSheet.layoutParams.height = getHeightWithoutActionBarSize()
        bottomSheetBehavior = BottomSheetBehavior.from(binding.llSearchBottomSheet)
        bottomSheetBehavior?.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                /*ignored*/
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED ||
                    newState == BottomSheetBehavior.STATE_COLLAPSED
                ) {
                    binding.ivSearchArrow.animate().setDuration(300).rotationBy(180f).start()
                }
            }
        })
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbSearch)
        supportActionBar?.title = getString(R.string.path_actionbar_title)
    }

    private fun changeBottomSheetState() {
        bottomSheetBehavior?.run {
            state = if (state == BottomSheetBehavior.STATE_EXPANDED) {
                BottomSheetBehavior.STATE_COLLAPSED

            } else {
                BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun createSearchAdapter() =
        SearchAdapter().apply {
            onClickStartDetail = { model ->
                startActivityForResult<TourDetailActivity>(
                    DETAIL_REQUEST_CODE,
                    TourDetailActivity.EXTRA_ENTITY to model
                )
            }
            onClickNaverSearch = { model ->
                setResult(
                    Activity.RESULT_OK,
                    Intent().apply {
                        putExtra(TourDetailActivity.EXTRA_CATEGORY, model.categoryCode)
                        putExtra(
                            TourDetailActivity.EXTRA_ENTITY, model as Serializable
                        )
                    }
                )
                finish()
            }
        }

    private fun requestSearch() {
        try {
            validateSearch()
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        } catch (e: Exception) {
            toast(e.message ?: "")
        }

        when (category) {
            CategoryType.NONE_SELECTED -> {
                /*ignored*/
            }
            CategoryType.MOVIE -> {
                getNaverFindList(
                    query = binding.tietSearch.text.toString(),
                    display = "10",
                    start = "1",
                    sort = "random",
                    category = category
                )
            }
            CategoryType.WAY_POINT -> {
                getNaverFindList(
                    query = binding.tietSearch.text.toString(),
                    display = "10",
                    start = "1",
                    sort = "random",
                    category = category
                )
            }
            CategoryType.CAFE -> {
                getNaverFindList(
                    query = binding.tietSearch.text.toString(),
                    display = "10",
                    start = "1",
                    sort = "random",
                    category = category
                )
            }
            CategoryType.CULTURE -> {
                getLocationBaseTourList(
                    numOfRows = 30,
                    pageNo = 1,
                    contentTypeId = 14,
                    arrange = "E",
                    mapX = longitude.toString(),
                    mapY = latitude.toString(),
                    radius = 10000,
                    category = category
                )
            }
            CategoryType.EXHIBITION -> {
                getLocationBaseTourList(
                    numOfRows = 30,
                    pageNo = 1,
                    contentTypeId = 14,
                    arrange = "B",
                    mapX = longitude.toString(),
                    mapY = latitude.toString(),
                    radius = 10000,
                    category = category
                )
            }
            CategoryType.TOUR -> {
                getEventDetailsList(
                    numOfRows = 30,
                    pageNo = 1,
                    arrange = "P",
                    eventStartDate=chooseDate.toInt(),
                    eventEndDate= chooseDate.toInt(),
                    category = category)
            }
            CategoryType.SPORTS -> {
                getLocationBaseTourList(
                    numOfRows = 30,
                    pageNo = 1,
                    contentTypeId = 28,
                    arrange = "E",
                    mapX = longitude.toString(),
                    mapY = latitude.toString(),
                    radius = 10000,
                    category = category
                )
            }
            CategoryType.SHOPPING -> {
                getLocationBaseTourList(
                    numOfRows = 30,
                    pageNo = 1,
                    contentTypeId = 38,
                    arrange = "E",
                    mapX = longitude.toString(),
                    mapY = latitude.toString(),
                    radius = 10000,
                    category = category
                )
            }
        }
    }

    private fun validateSearch() {
        if (category == CategoryType.NONE_SELECTED) {
            throw IllegalArgumentException("카테고리를 선택해주세요")
        }
    }

    private fun getHeightWithoutActionBarSize(): Int {
        val point = Point()
        this@SearchActivity.windowManager.defaultDisplay.getSize(point)
        return point.y - dip(56)
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            return
        }
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                android.R.id.home -> {
                    changeBottomSheetState()
                    return true
                }
                else -> {
                    /*ignored*/
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == DETAIL_REQUEST_CODE) {
                setResult(Activity.RESULT_OK, data)
                finish()
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showProgressBar() {
        binding.pbTour.show()
    }

    private fun hideProgressBar() {
        binding.pbTour.hide()
    }

    private fun getGeoCoding(
        x: Double,
        y: Double,
        cat1: String,
        cat2: String,
        cat3: String,
        contentTypeId: String
    ) {
        NetworkModule.geoCodingApi.getGeoCoding(
            Authorization = BuildConfig.KAKAO_KEY,
            x = x,
            y = y
        ).enqueue(object : Callback<GeoCodingResponse> {
            override fun onFailure(call: Call<GeoCodingResponse>, t: Throwable) {
                hideProgressBar()
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<GeoCodingResponse>,
                response: Response<GeoCodingResponse>
            ) {
                // Log.e("테스트", response.body()?.documents?.get(0)?.region2depthName)
                val num =
                    when (response.body()?.documents?.get(0)?.region2depthName) {
                        "강남구" -> 1
                        "강동구" -> 2
                        "강북구" -> 3
                        "강서구" -> 4
                        "관악구" -> 5
                        "광진구" -> 6
                        "구로구" -> 7
                        "금천구" -> 8
                        "노원구" -> 9
                        "도봉구" -> 10
                        else -> 0
                    }
                getAreaBasedList(
                    1,
                    30,
                    "P",
                    cat1,
                    cat2,
                    cat3,
                    contentTypeId,
                    num.toString()
                )
            }

        })
    }

    private fun getNaverFindList(
        query: String,
        display: String,
        start: String,
        sort: String,
        category: CategoryType
    ) {
        NetworkModule.naverFindApi.getNaverFind(
            clientId = BuildConfig.NAVER_CLENT_ID,
            clientSecret = BuildConfig.NAVER_CLENT_SECRET,
            url = query,
            display = display,
            start = start,
            sort = sort
        ).enqueue(object : Callback<NaverFindResponse> {
            override fun onFailure(call: Call<NaverFindResponse>, t: Throwable) {
                hideProgressBar()
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<NaverFindResponse>,
                response: Response<NaverFindResponse>
            ) {
                response.body()?.let {
                    searchAdapter.replaceAll(
                        it.items.map { naver ->
                            val latlng =
                                Tm128(naver.mapx.toDouble(), naver.mapy.toDouble()).toLatLng()

                            NaverFindModel(
                                categoryCode = category.code,
                                startTime = getStartTime(),
                                endTime = getEndTime(),
                                content = naver.description,
                                latitude = latlng.latitude,
                                longitude = latlng.longitude,
                                title = naver.title,
                                address = naver.address
                            )
                        })
                }
            }
        })
    }

    private fun getEventDetailsList(
        numOfRows: Int,
        pageNo: Int,
        arrange: String,
        eventStartDate: Int,
        eventEndDate: Int,
        category: CategoryType
    ) {
        NetworkModule.eventDetailsApi.getEventDetail(
            serviceKey = URLDecoder.decode(BuildConfig.KMA_KEY, "utf-8"),
            numOfRows = numOfRows,
            pageNo = pageNo,
            MobileOS = "AND",
            MobileApp = "TARRAWA",
            arrange = arrange,
            listYN = "Y",
            areaCode = 1,
            eventStartDate = eventStartDate,
            eventEndDate = eventEndDate,
            _type = "json"
        ).enqueue(object : Callback<EventDetailsResponse> {
            override fun onFailure(call: Call<EventDetailsResponse>, t: Throwable) {
                hideProgressBar()
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<EventDetailsResponse>,
                response: Response<EventDetailsResponse>
            ) {
                response.body()?.let {

                    searchAdapter.replaceAll(
                        it.response.body.items.item
                            .map { tourItem ->
                                LocationTourModel(
                                    categoryCode = category.code,
                                    title = tourItem.title,
                                    address = tourItem.addr1,
                                    content = "",
                                    startTime = getStartTime(),
                                    endTime = getEndTime(),
                                    latitude = tourItem.mapx,
                                    longitude = tourItem.mapy.toDouble(),
                                    imgUrl = tourItem.firstimage,
                                    contentId = tourItem.contentid,
                                    startDate = tourItem.eventstartdate,
                                    endDate = tourItem.eventenddate
                                )
                            }
                    )
                }
            }
        })
    }

    /**
     *
     * @param numOfRows
     * @param pageNo
     * @param contentTypeId
     * @param arrange A=제목순, B=조회순, C=수정일순, D=생성일순, E=거리순
     * @param mapX
     * @param mapY
     * @param radius 거리 m
     */
    private fun getLocationBaseTourList(
        numOfRows: Int = 30,
        pageNo: Int,
        contentTypeId: Int,
        arrange: String,
        mapX: String,
        mapY: String,
        radius: Int,
        category: CategoryType
    ) {
        showProgressBar()

        NetworkModule.locationBaseTourApi.getLocationBaseTour(
            serviceKey = URLDecoder.decode(BuildConfig.KMA_KEY, "utf-8"),
            numOfRows = numOfRows,
            pageNo = pageNo,
            mobileOS = "AND",
            mobileApp = "TARRAWA",
            contentTypeId = contentTypeId,
            arrange = arrange,
            mapX = mapX,
            mapY = mapY,
            radius = radius,
            listYN = "Y",
            _type = "json"
        ).enqueue(object : Callback<LocationBaseTourResponse?> {
            override fun onFailure(call: Call<LocationBaseTourResponse?>, t: Throwable) {
                hideProgressBar()
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<LocationBaseTourResponse?>,
                response: Response<LocationBaseTourResponse?>
            ) {
                hideProgressBar()

                response.body()?.let {
                    val defaultPhoto =
                        "https://firebasestorage.googleapis.com/v0/b/ttarawa-aa23f.appspot.com/o/coming-soon-3080102_1920.png?alt=media&token=341113e2-81b7-4ec3-b024-a360f1deb625"
                    searchAdapter.replaceAll(
                        it.response.body.items.item
                            .sortedBy { it.dist }
                            .map { tourItem ->
                                LocationTourModel(
                                    title = tourItem.title,
                                    address = tourItem.addr1,
                                    content = "",
                                    startTime = getStartTime(),
                                    endTime = getEndTime(),
                                    latitude = tourItem.mapx,
                                    longitude = tourItem.mapy,
                                    imgUrl = tourItem.firstimage ?: defaultPhoto,
                                    contentId = tourItem.contentid,
                                    startDate = chooseDate.toInt(),
                                    endDate = 0,
                                    categoryCode = category.code
                                )
                            }
                    )
                }
            }
        })
    }

    private fun getAreaBasedList(
        pageNo: Int,
        numOfRows: Int,
        arrange: String,
        cat1: String,
        cat2: String,
        cat3: String,
        contentTypeId: String,
        sigunguCode: String
    ) {
        showProgressBar()
        NetworkModule.areaBasedListApi.getAreaBasedList(
            serviceKey = URLDecoder.decode(BuildConfig.KMA_KEY, "utf-8"),
            pageNo = pageNo,
            numOfRows = numOfRows,
            MobileApp = "TARRAWA",
            MobileOS = "AND",
            arrange = arrange,
            cat1 = cat1,
            cat2 = cat2,
            cat3 = cat3,
            contentTypeId = contentTypeId,
            sigunguCode = sigunguCode,
            areaCode = "1",
            listYN = "Y",
            _type = "json"
        ).enqueue(object : Callback<AreaBasedListResponse> {
            override fun onFailure(call: Call<AreaBasedListResponse>, t: Throwable) {
                hideProgressBar()
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<AreaBasedListResponse>,
                response: Response<AreaBasedListResponse>
            ) {
                hideProgressBar()
                response.body()?.let {
                    searchAdapter.replaceAll(
                        it.response.body.items.item
                            .map { tourItem ->
                                LocationTourModel(
                                    title = tourItem.title,
                                    address = tourItem.addr1,
                                    content = "",
                                    startTime = getStartTime(),
                                    endTime = getEndTime(),
                                    latitude = tourItem.mapx.toDouble(),
                                    longitude = tourItem.mapy,
                                    imgUrl = tourItem.firstimage,
                                    contentId = tourItem.contentid,
                                    startDate = 0,
                                    endDate = 0,
                                    categoryCode = category.code
                                )
                            }
                    )
                }
            }

        })
    }

    fun setTime(startTime: String, endTime: String) {
        bind {
            tietStartTime.setText(startTime)
            tietEndTime.setText(endTime)
        }
    }

    private fun getStartTime(): String {
        val time = binding.tietStartTime.text.toString().replace(":".toRegex(), "")

        return if (time.isEmpty()) {
            getCurrentDay("HHmm")
        } else {
            time
        }
    }

    private fun getEndTime(): String {
        val time = binding.tietEndTime.text.toString().replace(":".toRegex(), "")

        return if (time.isEmpty()) {
            getCurrentDay("HHmm")
        } else {
            time
        }
    }

    companion object {
        const val EXTRA_LON = "EXTRA_LON"
        const val EXTRA_LAT = "EXTRA_LAT"
        const val EXTRA_DATE = "EXTRA_DATE"
        const val DETAIL_REQUEST_CODE = 3000
    }
}

