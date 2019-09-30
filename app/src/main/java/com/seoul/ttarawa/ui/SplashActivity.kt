package com.seoul.ttarawa.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.seoul.ttarawa.ui.main.MainActivity
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler
import com.seoul.ttarawa.ui.onboarding.OnBoardingActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent =
            if (true) {
                Intent(this, OnBoardingActivity::class.java)
        } else {
                Intent(this, MainActivity::class.java)
        }

        val handler = Handler()
        handler.postDelayed(Runnable {
            startActivity(intent)
            finish() }, 500)
    }
}
