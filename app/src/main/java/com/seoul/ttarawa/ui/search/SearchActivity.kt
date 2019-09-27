package com.seoul.ttarawa.ui.search

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
import com.seoul.ttarawa.ext.*
import com.seoul.ttarawa.module.NetworkModule
import com.seoul.ttarawa.ui.search.timepicker.TimePickerDialogFragment
import org.jetbrains.anko.dip
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.IllegalArgumentException
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

    private val searchAdapter = SearchAdapter(this@SearchActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chooseDate = intent.getStringExtra(EXTRA_DATE) ?: getCurrentDay()

        initView()
    }

    override fun initView() {
        bind {
            setSupportActionBar(tbSearch)
            supportActionBar?.title = getString(R.string.path_actionbar_title)

            rvSearch.adapter = searchAdapter

            llSearchBottomSheet.layoutParams.height = getHeightWithoutActionBarSize()
            bottomSheetBehavior = BottomSheetBehavior.from(binding.llSearchBottomSheet)

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


        }
    }

    private fun requestSearch() {
        try {
            validateSearch()
        } catch(e: Exception) {
            toast(e.message ?: "")
        }

        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

        when (category) {
            CategoryType.NONE_SELECTED -> {
                /*ignored*/
            }
            CategoryType.MOVIE -> {

            }
            CategoryType.WAY_POINT -> {

            }
            CategoryType.CAFE -> {

            }
            CategoryType.CULTURE -> {
                getEventDetailsList(
                    numOfRows = 30,
                    pageNo = 1,
                    arrange = "P",
                    eventStartDate = chooseDate.toInt(),
                    eventEndDate = 20191023
                )
            }
            CategoryType.EXHIBITION -> {

            }
            CategoryType.TOUR -> {

            }
            CategoryType.SPORTS -> {

            }
            CategoryType.SHOPPING -> {

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
                    bottomSheetBehavior?.run {
                        state = if (state == BottomSheetBehavior.STATE_EXPANDED) {
                            BottomSheetBehavior.STATE_COLLAPSED

                        } else {
                            BottomSheetBehavior.STATE_EXPANDED
                        }
                        return true
                    }
                }
                else -> {
                    /*ignored*/
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showProgressBar() {
        binding.pbTour.show()
    }

    private fun hideProgressBar() {
        binding.pbTour.hide()
    }

    private fun getEventDetailsList(
        numOfRows:Int,
        pageNo: Int,
        arrange:String,
        eventStartDate:Int,
        eventEndDate:Int
    ) {
        NetworkModule.eventDetailsApi.getEventDetail(
            serviceKey = URLDecoder.decode(BuildConfig.KMA_KEY, "utf-8"),
            numOfRows=numOfRows,
            pageNo=pageNo,
            MobileOS="AND",
            MobileApp="TARRAWA",
            arrange=arrange,
            listYN="Y",
            areaCode = 1,
            eventStartDate=eventStartDate,
            eventEndDate=eventEndDate,
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
                                    imgUrl = tourItem.firstimage,
                                    title = tourItem.title,
                                    address = tourItem.addr1,
                                    contentID = tourItem.contentid,
                                    mapX = tourItem.mapx,
                                    mapY = tourItem.mapy,
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
                              contentID = tourItem.contentid,
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

    companion object {
        const val EXTRA_DATE = "EXTRA_DATE"
    }
}