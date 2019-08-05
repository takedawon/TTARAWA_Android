package com.seoul.ttarawa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.kakao.util.exception.KakaoException
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.auth.Session.getCurrentSession
import com.kakao.util.helper.log.Logger
import android.R




class LoginActivity : AppCompatActivity() {
    private var callback: SessionCallback? = null

    /**
     * 로그인 버튼을 클릭 했을시 access token을 요청하도록 설정한다.
     *
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.seoul.ttarawa.R.layout.activity_login)

        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().checkAndImplicitOpen()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    private inner class SessionCallback : ISessionCallback {

        override fun onSessionOpened() {
            redirectSignupActivity()
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Logger.e(exception)
            }
        }
    }

    protected fun redirectSignupActivity() {
        val intent = Intent(this, TestActivity::class.java)
        startActivity(intent)
        finish()
    }
}
