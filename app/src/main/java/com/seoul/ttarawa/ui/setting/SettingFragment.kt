package com.seoul.ttarawa.ui.setting

import android.os.Bundle
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.util.exception.KakaoException
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseFragment
import com.seoul.ttarawa.databinding.FragmentSettingBinding
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber

class SettingFragment : BaseFragment<FragmentSettingBinding>(
    R.layout.fragment_setting
) {

    private lateinit var session: SessionCallback

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        session = SessionCallback()
        Session.getCurrentSession().addCallback(session)
        Session.getCurrentSession().checkAndImplicitOpen()
    }

    override fun initView() {
    }

    override fun onDestroyView() {
        Session.getCurrentSession().removeCallback(session)
        super.onDestroyView()
    }

    private inner class SessionCallback : ISessionCallback {

        override fun onSessionOpened() {
            toast("onSessionOpened()")
        }

        override fun onSessionOpenFailed(e: KakaoException?) {
            if (e != null) {
                Timber.e(e)
            }
        }
    }

    companion object {
        fun newInstance(): SettingFragment {
            return SettingFragment()
        }
    }
}