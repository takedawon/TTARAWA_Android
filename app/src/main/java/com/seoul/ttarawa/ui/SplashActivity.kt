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


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MainActivity::class.java)            // 실제 사용할 메인 액티비티

        val handler = Handler()
        handler.postDelayed(Runnable {
            startActivity(intent)
            finish() }, 1200)
    }
}
