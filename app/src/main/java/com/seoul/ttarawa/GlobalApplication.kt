package com.seoul.ttarawa

import android.app.Application
import com.facebook.stetho.Stetho
import com.google.firebase.FirebaseApp
import com.kakao.auth.*
import timber.log.Timber

class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        KakaoSDK.init(KakaoSDKAdapter())
        FirebaseApp.initializeApp(this)
        Stetho.initializeWithDefaults(this)

        // init timber
        Timber.plant(Timber.DebugTree())
        // init stetho
        Stetho.initializeWithDefaults(this)
    }

    private class KakaoSDKAdapter : KakaoAdapter() {
        /**
         * Session Config에 대해서는 default값들이 존재한다.
         * 필요한 상황에서만 override해서 사용하면 됨.
         * @return Session의 설정값.
         */
        override fun getSessionConfig(): ISessionConfig {
            return object : ISessionConfig {
                override fun getAuthTypes(): Array<AuthType> {
                    return arrayOf(AuthType.KAKAO_ACCOUNT)
                }

                override fun isUsingWebviewTimer(): Boolean {
                    return false
                }

                override fun isSecureMode(): Boolean {
                    return false
                }

                override fun getApprovalType(): ApprovalType {
                    return ApprovalType.INDIVIDUAL
                }

                override fun isSaveFormData(): Boolean {
                    return true
                }
            }
        }

        override fun getApplicationConfig(): IApplicationConfig {
            return IApplicationConfig { globalApplicationContext }
        }
    }

    companion object {

        @Volatile
        private var instance: GlobalApplication? = null
        val globalApplicationContext: GlobalApplication?
            get() {
                if (instance == null)
                    throw IllegalStateException("this application does not inherit com.kakao.GlobalApplication")
                return instance
            }
    }
}

