package com.seoul.ttarawa.ui.calendar

import android.os.Bundle
import android.os.SystemClock
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityCalendarBinding
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.ext.getTextFormattedTime
import com.seoul.ttarawa.ui.path.PathActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*

/**
 * 달력 화면
 * 날짜 선택 시 검색 화면으로 이동
 */
class CalendarActivity : BaseActivity<ActivityCalendarBinding>(
    R.layout.activity_calendar
) {
    private var mLastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    override fun initView() {
        bind {
            btnCalBack click { onBackPressed() }

            btnCalBack click {
                onBackPressed()
                // overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity)
            }

            val today = Calendar.getInstance()
            cvCal.date = today.timeInMillis
            cvCal.minDate = today.time.time

            cvCal.setOnDateChangeListener { _, year, month, dayOfMonth ->
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return@setOnDateChangeListener
                }
                mLastClickTime = SystemClock.elapsedRealtime()
                val chooseCalendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    roll(Calendar.MINUTE, +2)
                }

                if (isBeforeDay(chooseCalendar)) {
                    toast("이전 날짜는 선택이 불가능합니다.")
                    return@setOnDateChangeListener
                }

                toast("${year}년 ${month + 1}월 ${dayOfMonth}일을 선택하셨습니다.")
                val date =
                    "$year${(month + 1).getTextFormattedTime()}${dayOfMonth.getTextFormattedTime()}"

                // 패스 액티비티로 이동 및 종료
                startActivity<PathActivity>(PathActivity.EXTRA_DATE to date)
                finish()
            }
        }
    }

    private fun isBeforeDay(chooseCalendar: Calendar): Boolean {
        val current = Calendar.getInstance()
        return chooseCalendar.before(current)
    }
}
