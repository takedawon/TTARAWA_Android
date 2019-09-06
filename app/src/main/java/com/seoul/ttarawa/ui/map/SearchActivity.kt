package com.seoul.ttarawa.ui.map

import android.os.Bundle
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivitySearchBinding
import com.seoul.ttarawa.ext.click

/**
 * 검색 화면
 * 조건을 추가하고, 검색을 한다
 * 검색 결과를 가지고 PathFragment 로 이동
 */
class SearchActivity : BaseActivity<ActivitySearchBinding>(
    R.layout.activity_search
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        bind {
            filterIcon click {
            }
        }
    }
}