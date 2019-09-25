package com.seoul.ttarawa.ui.main

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.log.Logger
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseFragment
import com.seoul.ttarawa.databinding.FragmentSettingBinding
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber


class SettingFragment : BaseFragment<FragmentSettingBinding>(
    R.layout.fragment_setting
) {
    private lateinit var database: FirebaseDatabase
    private lateinit var session: SessionCallback

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()

        session = SessionCallback()
        if(!Session.getCurrentSession().isOpenable) {
            Session.getCurrentSession().addCallback(session)
            Session.getCurrentSession().checkAndImplicitOpen()
        }

        binding.button.setOnClickListener {
            onClickUnlink()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(session)
    }

    override fun initView() {
        database = FirebaseDatabase.getInstance()
    }

   private fun onClickUnlink() {
        val appendMessage = getString(R.string.com_kakao_confirm_unlink)
        AlertDialog.Builder(activity)
        .setMessage(appendMessage)
        .setPositiveButton(getString(R.string.com_kakao_ok_button)
        ) { dialog, _ ->
            UserManagement.requestUnlink(object:UnLinkResponseCallback() {
        override fun onFailure(errorResult:ErrorResult?) {
            Logger.e(errorResult!!.toString())
        }

        override fun onSessionClosed(errorResult:ErrorResult) {
            //redirectLoginActivity()
        }

        override fun onNotSignedUp() {
            //redirectSignupActivity()
        }

        override fun onSuccess(userId:Long?) {
            //redirectLoginActivity()
        }
    })
    dialog.dismiss()
}
    .setNegativeButton(getString(R.string.com_kakao_cancel_button)
    ) { dialog, _ -> dialog.dismiss() }.show()

        }

    override fun onDestroyView() {
        Session.getCurrentSession().removeCallback(session)
        super.onDestroyView()
}
/*

 */
    private inner class SessionCallback : ISessionCallback {

        override fun onSessionOpened() {
            toast("onSessionOpened()")
            requestMe()
        }

        override fun onSessionOpenFailed(e: KakaoException?) {
            if (e != null) {
                Timber.e(e)

            }
            toast("onSessionOpenFailed()")
        }

        fun requestMe() {
            UserManagement.requestMe(object : MeResponseCallback() {
                // 세션 오픈 실패, 세션이 삭제된 경우
                override fun onSessionClosed(errorResult: ErrorResult) {
                    Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.errorMessage)
                }

                //회원가입이 안되어 있는 경우
                override fun onNotSignedUp() {
                    Log.e("SessionCallback :: ", "onNotSignedUp")
                }

                //사용자 정보 요청 성공 : 사용자 정보를 리턴
                override fun onSuccess(userProfile: UserProfile) {
                    val myRef = database.getReference("USER")
                    val id = userProfile.id.toString()
                    var swit=true
                    val postListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for(a in dataSnapshot.children)
                                if(a.key==id)
                                    swit=false

                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }
                    }
                    val nickname = userProfile.nickname
                    val profileImage=userProfile.profileImagePath
                    val thumbnailImage = userProfile.thumbnailImagePath
                    myRef.addValueEventListener(postListener)
                    if(swit)
                        myRef.child(id).setValue(KakaoUserMember(nickname,profileImage,thumbnailImage))
                }

                //사용자 정보 요청 실패
                override fun onFailure(errorResult: ErrorResult?) {
                    Log.e("SessionCallback :: ", "onFailure : " + errorResult!!.errorMessage)
                }

            })
        }
    }

    companion object {
        fun newInstance(): SettingFragment {
            return SettingFragment()
        }
    }
}

data class KakaoUserMember(
    var nickname: String? = "",
    var profileImage: String? = "",
    var thumbnailImage: String? = ""
)