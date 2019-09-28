package com.seoul.ttarawa.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityNewsBinding

class NewsActivity : BaseActivity<ActivityNewsBinding>(
    R.layout.activity_news
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        bind {
            btnNewsBack.setOnClickListener {
                finish()
            }
        }
    }
}
