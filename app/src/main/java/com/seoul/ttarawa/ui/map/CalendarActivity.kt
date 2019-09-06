package com.seoul.ttarawa.ui.map

import android.os.Bundle
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityCalendarBinding
import com.seoul.ttarawa.ext.click
import org.jetbrains.anko.startActivity
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
                toast("${year}년 ${month + 1}월 ${dayOfMonth}일을 선택하셨습니다.")

                val date = "$year${month+1}$dayOfMonth"

                // 패스 액티비티로 이동 및 종료
                startActivity<PathActivity>(PathActivity.EXTRA_DATE to date)
                finish()
            }
        }
    }
}
