package com.seoul.ttarawa.ui.map

import android.os.Bundle
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityPathBinding
import net.daum.mf.map.api.MapView

/**
 * 지도 + 경로
 */
class PathActivity : BaseActivity<ActivityPathBinding>(
    R.layout.activity_path
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        bind {
            rlPathMap.addView(MapView(this@PathActivity).apply {
                isHDMapTileEnabled = true
            })
        }
    }

    companion object {
        const val EXTRA_DATE = "EXTRA_DATE"
    }
}
