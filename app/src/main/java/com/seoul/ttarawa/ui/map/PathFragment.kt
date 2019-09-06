package com.seoul.ttarawa.ui.map

import android.os.Bundle
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseFragment
import com.seoul.ttarawa.databinding.FragmentPathBinding
import net.daum.mf.map.api.MapView

class PathFragment : BaseFragment<FragmentPathBinding>(
    R.layout.fragment_path
) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
    }

    override fun initView() {
        bind {
            rlPathMap.addView(MapView(activity).apply {
                isHDMapTileEnabled = true
            })
        }
    }

    companion object {
        fun newInstance(): PathFragment {
            return PathFragment()
        }
    }
}
