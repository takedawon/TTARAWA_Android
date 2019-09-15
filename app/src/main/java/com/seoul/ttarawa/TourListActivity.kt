package com.seoul.ttarawa

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.entity.LocationTourModel
import com.seoul.ttarawa.data.remote.response.LocationBaseTourResponse
import com.seoul.ttarawa.databinding.ActivityTourListBinding
import com.seoul.ttarawa.ext.hide
import com.seoul.ttarawa.ext.show
import com.seoul.ttarawa.module.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLDecoder

class TourListActivity :
    BaseActivity<ActivityTourListBinding>(R.layout.activity_tour_list) {

    val mAdapter = LocationBaseTourAdapter()
    val items: ArrayList<LocationTourModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    override fun initView() {
        bind {
            getLocationBaseTourList(
                30, 1, 15, "B"
                , "126.981611", "37.568477", 5000
            )
            // arrange : A=제목순, B=조회순, C=수정일순, D=생성일순, E=거리순
            // radius : 거리 m
            recyclerView.adapter = mAdapter
            val lm = LinearLayoutManager(this@TourListActivity)
            binding.recyclerView.layoutManager = lm
            binding.recyclerView.setHasFixedSize(true)
        }
    }

    private fun getLocationBaseTourList(
        numOfRows: Int, pageNo: Int, contentTypeId: Int,
        arrange: String, mapX: String, mapY: String,
        radius: Int
    ) {
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
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<LocationBaseTourResponse?>,
                response: Response<LocationBaseTourResponse?>
            ) {
                response.body()?.let {
                    val size = it.response.body.numOfRows
                    val defaultPhoto =
                        "https://firebasestorage.googleapis.com/v0/b/ttarawa-aa23f.appspot.com/o/coming-soon-3080102_1920.png?alt=media&token=341113e2-81b7-4ec3-b024-a360f1deb625"
                    val tourItems = it.response.body.items
                    for (i in 0 until size) {
                        var distance: String = if (tourItems.item[i].dist >= 1000.0) {
                            String.format("%.1f", (tourItems.item[i].dist / 1000.0)) + "km"
                        } else
                            tourItems.item[i].dist.toString() + "m"

                        items.add(
                            LocationTourModel(
                                tourItems.item[i].firstimage
                                    ?: defaultPhoto,
                                tourItems.item[i].title,
                                tourItems.item[i].addr1,
                                distance
                            )
                        )
                    }
                    mAdapter.addItem(items)
                }
            }
        })
    }

    private fun showProgressBar() {
        binding.pbTour.show()
    }

    private fun hideProgressBar() {
        binding.pbTour.hide()
    }
}
