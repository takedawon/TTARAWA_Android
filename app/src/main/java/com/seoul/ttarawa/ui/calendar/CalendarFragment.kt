package com.seoul.ttarawa.ui.calendar

import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseFragment
import com.seoul.ttarawa.databinding.FragmentCalendarBinding
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.ext.replaceInFragment
import com.seoul.ttarawa.ui.search.SearchFragment
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 달력 화면
 * 날짜 선택 시 검색 화면으로 이동
 */
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(
    R.layout.fragment_calendar
) {

    private val calViewModel: CalendarViewModel by viewModel()

    override fun initView() {
        bind {
            btnCalBack click {
                activity?.onBackPressed()
                // overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity)
            }

            cvCal.setOnDateChangeListener { view, year, month, dayOfMonth ->
                val msg: String =
                    year.toString() + "년" + (month + 1).toString() + "월" + dayOfMonth.toString() + "일을 선택하셨습니다."
                toast(msg)

                replaceInFragment(R.id.container_plan, SearchFragment.newInstance())
            }
        }
    }

    companion object {
        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }
}
