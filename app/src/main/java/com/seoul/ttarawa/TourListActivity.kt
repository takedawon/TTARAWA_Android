package com.seoul.ttarawa

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.entity.LocationTourModel
import com.seoul.ttarawa.data.remote.response.LocationBaseTourResponse
import com.seoul.ttarawa.databinding.ActivityTourListBinding
import com.seoul.ttarawa.ext.hide
import com.seoul.ttarawa.ext.show
import com.seoul.ttarawa.module.NetworkModule
import kotlinx.android.synthetic.main.activity_tour_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Url
import java.net.URLDecoder
import java.net.URLEncoder

class TourListActivity :
    BaseActivity<ActivityTourListBinding>(R.layout.activity_tour_list) {

    val mAdapter = LocationBaseTourAdapter()
    val items : ArrayList<LocationTourModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    override fun initView() {
        bind {
            getLocationBaseTourList(3,1,15,"A"
                ,"126.981611","37.568477",1000)

            recyclerView.adapter = mAdapter
            val lm = LinearLayoutManager(this@TourListActivity)
            binding.recyclerView.layoutManager = lm
            binding.recyclerView.setHasFixedSize(true)
        }
    }

    private fun getLocationBaseTourList(numOfRows:Int,pageNo:Int,contentTypeId:Int,
                                        arrange:String, mapX:String,mapY:String,
                                        radius:Int) {
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
        ).enqueue(object: Callback<LocationBaseTourResponse?> {
            override fun onFailure(call: Call<LocationBaseTourResponse?>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<LocationBaseTourResponse?>,
                response: Response<LocationBaseTourResponse?>
            ) {
                response.body()?.let {
                    val size = it.response.body.numOfRows
                    for (i in 0 until size) {
                        items.add(
                            LocationTourModel(
                                it.response.body.items.item[i].firstimage,
                                it.response.body.items.item[i].title,
                                it.response.body.items.item[i].addr1,
                                it.response.body.items.item[i].dist.toString()
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
