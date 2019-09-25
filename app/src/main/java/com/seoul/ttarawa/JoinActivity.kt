package com.seoul.ttarawa

import android.os.Bundle
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityJoinBinding

class JoinActivity : BaseActivity<ActivityJoinBinding>(
    R.layout.activity_join
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {

    }

}
