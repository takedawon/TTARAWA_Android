package com.seoul.ttarawa.ui.search

import android.os.Bundle
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityTourDetailBinding

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
}
