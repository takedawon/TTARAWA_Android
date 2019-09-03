package com.seoul.ttarawa.ui.plan

import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityPlanBinding
import com.seoul.ttarawa.ext.addFragmentInActivity
import com.seoul.ttarawa.ui.calendar.CalendarFragment

/**
 * 플랜을 세우기 위한 액티비티
 * 시작시 달력이 보여진다
 */
class PlanActivity : BaseActivity<ActivityPlanBinding>(
    R.layout.activity_plan
) {

    override fun initView() {
        addFragmentInActivity(R.id.container_plan, CalendarFragment.newInstance())
    }
}
