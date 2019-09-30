package com.seoul.ttarawa.ui.onboarding

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.commit
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityOnboardingBinding
import com.seoul.ttarawa.ui.main.MainActivity
import org.jetbrains.anko.startActivity

class OnBoardingActivity : BaseActivity<ActivityOnboardingBinding>(
    R.layout.activity_onboarding
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    override fun initView() {
        initTransparentStatusBar()

        val onboardingFragment = PaperOnboardingFragment.newInstance(
            arrayListOf(
                // 경로 생성
                PaperOnboardingPage(
                    "경로 만들기",
                    "즐거운 데이트/나들이를 위해 경로를 생성하고 저장할 수 있습니다.",
                    Color.parseColor("#678FB4"),
                    R.drawable.onboarding_ic_placeholder,
                    R.drawable.onboarding_ic_car1
                ),
                // 경로를 저장
                PaperOnboardingPage(
                    "내 경로 목록",
                    "생성한 경로를 저장하고 내 경로 목록에서 다시 볼 수 있습니다.",
                    Color.parseColor("#65B0B4"),
                    R.drawable.onboarding_ic_folder,
                    R.drawable.onboarding_ic_car2
                ),
                // 경로를 공유
                PaperOnboardingPage(
                    "공유하기",
                    "다른 회원들이 공유한 경로를 보거나 직접 공유 할 수 있습니다.",
                    Color.parseColor("#9B90BC"),
                    R.drawable.onboarding_ic_analytics,
                    R.drawable.onboarding_ic_car3
                )
            )
        )

        onboardingFragment.setOnRightOutListener {
            startActivity<MainActivity>()               // 실제 사용할 메인 액티비티
            finish()
        }

        supportFragmentManager.commit {
            add(R.id.container_onboarding, onboardingFragment)
        }


    }

    private fun initTransparentStatusBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // 스테이터스바 글자 색 어둡게 변경, 23 이상
        var flag = window.decorView.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flag = flag or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.decorView.systemUiVisibility = flag
        window.statusBarColor = Color.TRANSPARENT
    }


}