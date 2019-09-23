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
import com.seoul.ttarawa.ext.hide
import com.seoul.ttarawa.ext.show
import com.seoul.ttarawa.module.NetworkModule
import org.jetbrains.anko.dip
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLDecoder

/**
 * 검색 화면
 * 조건을 추가하고, 검색을 한다
 * 검색 결과를 가지고 PathFragment 로 이동
 */
class SearchActivity : BaseActivity<ActivitySearchBinding>(
    R.layout.activity_search
) {

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    private val searchAdapter = SearchAdapter(this@SearchActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

        getEventDetailsList(
            numOfRows = 30,
            pageNo = 1,
            arrange = "P",
            eventStartDate = 20190923,
            eventEndDate = 20191023
        )
        /* getLocationBaseTourList(
            pageNo = 1,
            contentTypeId = 15,
            arrange = "B",
            mapX = "126.981611",
            mapY = "37.568477",
            radius = 5000
        )*/
    }

    override fun initView() {
        bind {
            setSupportActionBar(tbSearch)
            supportActionBar?.title = getString(R.string.path_actionbar_title)

            llSearchBottomSheet.layoutParams.height = getHeightWithoutActionBarSize()
            bottomSheetBehavior = BottomSheetBehavior.from(binding.llSearchBottomSheet)

            rvSearch.adapter = searchAdapter
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
                                    mapY = tourItem.mapy
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
}