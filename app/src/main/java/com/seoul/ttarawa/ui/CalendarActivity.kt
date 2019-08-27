package com.seoul.ttarawa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.seoul.ttarawa.R
import kotlinx.android.synthetic.main.activity_calendar.*

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        btn_cal_back.setOnClickListener(View.OnClickListener {
            finish()
            overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity)
        })
    }
}
