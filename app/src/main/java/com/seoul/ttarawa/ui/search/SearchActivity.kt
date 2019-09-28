package com.seoul.ttarawa.ui.search

import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.entity.LocationTourModel
import com.seoul.ttarawa.data.remote.response.EventDetailsResponse
import com.seoul.ttarawa.data.remote.response.LocationBaseTourResponse
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
import java.net.URLDecoder

/**
 * 검색 화면
 * 조건을 추가하고, 검색을 한다
 * 검색 결과를 가지고 PathFragment 로 이동
 */
class SearchActivity : BaseActivity<ActivitySearchBinding>(
    R.layout.activity_search
) {

    private var chooseDate: String = ""

    private var category = CategoryType.NONE_SELECTED

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    private val searchAdapter by lazy { createSearchAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chooseDate = intent.getStringExtra(EXTRA_DATE) ?: getCurrentDay()

        initView()
    }

    override fun initView() {
        initActionBar()
        initBottomSheetBehavior()

        bind {
            rvSearch.adapter = searchAdapter

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
                getEventDetailsList(
                    numOfRows = 30,
                    pageNo = 1,
                    arrange = "P",
                    eventStartDate = chooseDate.toInt(),
                    eventEndDate = 20191023,
                    category = category
                )
            }
            CategoryType.WAY_POINT -> {
                getEventDetailsList(
                    numOfRows = 30,
                    pageNo = 1,
                    arrange = "P",
                    eventStartDate = chooseDate.toInt(),
                    eventEndDate = 20191023,
                    category = category
                )
            }
            CategoryType.CAFE -> {
                getEventDetailsList(
                    numOfRows = 30,
                    pageNo = 1,
                    arrange = "P",
                    eventStartDate = chooseDate.toInt(),
                    eventEndDate = 20191023,
                    category = category
                )
            }
            CategoryType.CULTURE -> {
                getEventDetailsList(
                    numOfRows = 30,
                    pageNo = 1,
                    arrange = "P",
                    eventStartDate = chooseDate.toInt(),
                    eventEndDate = 20191023,
                    category = category
                )
            }
            CategoryType.EXHIBITION -> {
                getEventDetailsList(
                    numOfRows = 30,
                    pageNo = 1,
                    arrange = "P",
                    eventStartDate = chooseDate.toInt(),
                    eventEndDate = 20191023,
                    category = category
                )
            }
            CategoryType.TOUR -> {
                getEventDetailsList(
                    numOfRows = 30,
                    pageNo = 1,
                    arrange = "P",
                    eventStartDate = chooseDate.toInt(),
                    eventEndDate = 20191023,
                    category = category
                )
            }
            CategoryType.SPORTS -> {
                getEventDetailsList(
                    numOfRows = 30,
                    pageNo = 1,
                    arrange = "P",
                    eventStartDate = chooseDate.toInt(),
                    eventEndDate = 20191023,
                    category = category
                )
            }
            CategoryType.SHOPPING -> {
                getEventDetailsList(
                    numOfRows = 30,
                    pageNo = 1,
                    arrange = "P",
                    eventStartDate = chooseDate.toInt(),
                    eventEndDate = 20191023,
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
        radius: Int
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
                    /*
                    val defaultPhoto =
                      "https://firebasestorage.googleapis.com/v0/b/ttarawa-aa23f.appspot.com/o/coming-soon-3080102_1920.png?alt=media&token=341113e2-81b7-4ec3-b024-a360f1deb625"

                  searchAdapter.replaceAll(
                      it.response.body.items.item
                          .sortedBy { it.dist }
                          .map { tourItem ->
                          LocationTourModel(
                              imgUrl = tourItem.firstimage ?: defaultPhoto,
                              title = tourItem.title,
                              address = tourItem.addr1,
                              distance = if (tourItem.dist >= 1000.0) {
                                  String.format("%.1f", (tourItem.dist / 1000.0)) + "km"
                              } else {
                                  tourItem.dist.toString() + "m"
                              },
                              contentId = tourItem.contentid,
                              mapX = tourItem.mapx,
                              mapY = tourItem.mapy
                          )
                      }
                  )
                  */
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

    private fun getStartTime() =
        try {
            binding.tietStartTime.text.toString().replace(":".toRegex(), "").toInt()
        } catch (e: NumberFormatException) {
            getCurrentDay("HHss").toInt()
        }


    private fun getEndTime() =
        try {
            binding.tietEndTime.text.toString().replace(":".toRegex(), "").toInt()
        } catch (e: NumberFormatException) {
            getCurrentDay("HHss").toInt()
        }

    companion object {
        const val EXTRA_DATE = "EXTRA_DATE"
        const val DETAIL_REQUEST_CODE = 3000
    }
}