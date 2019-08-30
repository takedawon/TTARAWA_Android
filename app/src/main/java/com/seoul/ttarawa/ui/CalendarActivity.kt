package com.seoul.ttarawa.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.seoul.ttarawa.R
import kotlinx.android.synthetic.main.activity_calendar.*

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        btn_cal_back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity)
        }

        calendar_view.setOnDateChangeListener{ view, year, month, dayOfMonth ->
            val msg:String = year.toString() + "년" + (month+1).toString()+ "월" + dayOfMonth.toString() + "일을 선택하셨습니다."
            runOnUiThread {
                Toast.makeText(this,msg,Toast.LENGTH_SHORT).show() //날짜 터치시 알림표시로 나타내기
                val intent = Intent(this,PlanActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
