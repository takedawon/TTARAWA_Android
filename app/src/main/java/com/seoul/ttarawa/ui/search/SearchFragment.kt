package com.seoul.ttarawa.ui.search

import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseFragment
import com.seoul.ttarawa.databinding.FragmentSearchBinding
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.ext.replaceInFragment
import com.seoul.ttarawa.ui.path.PathFragment

/**
 * 검색 화면
 * 조건을 추가하고, 검색을 한다
 * 검색 결과를 가지고 PathFragment 로 이동
 */
class SearchFragment : BaseFragment<FragmentSearchBinding>(
    R.layout.fragment_search
) {
    override fun initView() {
        bind {
            filterIcon click {
                replaceInFragment(R.id.container_plan, PathFragment.newInstance())
            }
        }
    }

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}