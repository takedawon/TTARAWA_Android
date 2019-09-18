package com.seoul.ttarawa.ui.search

import android.os.Bundle
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseFragment
import com.seoul.ttarawa.databinding.ItemTourImageBinding

class TourImageFragment : BaseFragment<ItemTourImageBinding>
    (R.layout.item_tour_image) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    override fun initView() {
        bind {
            if (arguments != null) {
                val arg = arguments
                if (arg != null) {
                    imgTourDetailImage.setImageResource(arg.getInt("imgRes"))
                }
            }
        }
    }
}