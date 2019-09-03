package com.seoul.ttarawa.ui.map

import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseFragment
import com.seoul.ttarawa.databinding.FragmentMapBinding
import net.daum.mf.map.api.MapView

/**
 * 경로가 나오는 맵과 별도로 맵을 보여주는 화면
 * 제거될 수도 있음
 */
class MapFragment : BaseFragment<FragmentMapBinding>(
    R.layout.fragment_map
) {
    override fun initView() {
        bind {
            rlMap.addView(MapView(activity).apply {
                isHDMapTileEnabled = true
            })
        }
    }

    companion object {
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }
}
