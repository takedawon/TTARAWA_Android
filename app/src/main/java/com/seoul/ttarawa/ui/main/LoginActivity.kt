package com.seoul.ttarawa.ui.main

import android.os.Bundle
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>(
    R.layout.activity_login
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {

    }

}
