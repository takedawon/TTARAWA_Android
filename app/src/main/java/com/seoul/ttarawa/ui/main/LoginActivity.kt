package com.seoul.ttarawa.ui.main

import android.content.Intent
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
        binding.txtLoginJoin.setOnClickListener {
            val intent = Intent(applicationContext, JoinActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initView() {

    }

}
