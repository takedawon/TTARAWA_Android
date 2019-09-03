package com.seoul.ttarawa.ui.home

import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseFragment
import com.seoul.ttarawa.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    R.layout.fragment_home
) {
    override fun initView() {

    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}