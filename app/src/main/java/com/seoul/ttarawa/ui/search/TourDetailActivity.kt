package com.seoul.ttarawa.ui.search

import android.os.Bundle
import android.util.Log
import com.seoul.ttarawa.BuildConfig
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.remote.response.TourImageResponse
import com.seoul.ttarawa.databinding.ActivityTourDetailBinding
import com.seoul.ttarawa.module.NetworkModule
import retrofit2.Call
import retrofit2.Response
import java.net.URLDecoder
import javax.security.auth.callback.Callback

class TourDetailActivity : BaseActivity<ActivityTourDetailBinding>(
    R.layout.activity_tour_detail
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        val url: ArrayList<String> = ArrayList()
        url.add("http://tong.visitkorea.or.kr/cms/resource/04/2613904_image2_1.jpg")
        url.add("http://tong.visitkorea.or.kr/cms/resource/04/2613904_image2_1.jpg")
        val adapter = TourImageAdapter()
        bind {
            tourDetailViewPager.adapter = adapter
        }
        adapter.replaceAll(url)
    }

    private fun getTourImage(numOfRows:Int,
                             pageNo:Int,
                             contentId:Int,
                             imageYN:String,
                             subImageYN:String) {
        NetworkModule.tourImageApi.getTourImage(
            serviceKey = URLDecoder.decode(BuildConfig.KMA_KEY, "utf-8"),
            numOfRows = numOfRows,
            pageNo = pageNo,
            mobileOS="AND",
            mobileApp = "TARRAWA",
            contentId = contentId,
            imageYN=imageYN,
            subImageYN = subImageYN,
            _type = "json"
        ).enqueue(object : retrofit2.Callback<TourImageResponse> {
            override fun onFailure(call: Call<TourImageResponse>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<TourImageResponse>,
                response: Response<TourImageResponse>
            ) {

                response.body()?.let {
                    Log.e("테스트", it.response.body.toString());
                }
            }
        })
    }
}

