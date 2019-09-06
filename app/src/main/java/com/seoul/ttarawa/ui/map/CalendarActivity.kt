package com.seoul.ttarawa.ui.map

import android.os.Bundle
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityCalendarBinding
import com.seoul.ttarawa.ext.click
import org.jetbrains.anko.toast

/**
 * 달력 화면
 * 날짜 선택 시 검색 화면으로 이동
 */
class CalendarActivity : BaseActivity<ActivityCalendarBinding>(
    R.layout.activity_calendar
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    override fun initView() {
        bind {
            btnCalBack click {
                onBackPressed()
                // overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity)
            }

            cvCal.setOnDateChangeListener { view, year, month, dayOfMonth ->
                val msg: String =
                    year.toString() + "년" + (month + 1).toString() + "월" + dayOfMonth.toString() + "일을 선택하셨습니다."
                toast(msg)

                // todo 패스 액티비티 이동
            }
        }
    }
}
