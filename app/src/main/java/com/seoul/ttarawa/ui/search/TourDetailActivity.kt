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
        url.add("https://avatars2.githubusercontent.com/u/44185071?s=460&v=4")

        val adapter = TourImageAdapter()
        bind {
            tourDetailViewPager.adapter = adapter
        }
        adapter.replaceAll(url)
    }
}
