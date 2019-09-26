package com.seoul.ttarawa.ui.main

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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
import kotlinx.android.synthetic.main.fragment_setting.*
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber


class SettingFragment : BaseFragment<FragmentSettingBinding>(
    R.layout.fragment_setting
) {
    private lateinit var database: FirebaseDatabase
    private lateinit var session: SessionCallback
    private lateinit var myRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        myRef = database.getReference("USER")

        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser.uid)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()

        session = SessionCallback()
        if (!Session.getCurrentSession().isOpenable) {
            Session.getCurrentSession().addCallback(session)
            Session.getCurrentSession().checkAndImplicitOpen()
        }

        binding.btnLoginJoin.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivityForResult(intent, 1000)
        }

        binding.btnLoginLogout.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("로그아웃 하시겠습니까?")
                .setPositiveButton("네") { _, _ ->
                    auth.signOut()
                    setLogoutView()
                    toast("로그아웃 되었습니다.")
                }
                .setNegativeButton("아니오",null)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        } else if (resultCode == RESULT_OK) {
            if (requestCode == 1000) { // LoginActivity
                updateUI(data!!.getStringExtra("uid"))
            }
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

    private fun setLogoutView() {
        bind {
            btnLoginLogout.visibility = View.GONE
            layoutLoginAfter.visibility = View.GONE
            layoutLoginBefore.visibility = View.VISIBLE
        }
    }

    private fun updateUI(data: String) {
        myRef.child(data).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val nick = p0.child("nickname").value.toString()
                    val profileUrl = p0.child("profileImage").value.toString()

                    bind {
                        layoutLoginAfter.visibility = View.VISIBLE
                        layoutLoginBefore.visibility = View.GONE
                        txtSettingInfoAfter.text = nick + "님 환영합니다!"
                        Glide.with(this@SettingFragment)
                            .load(profileUrl)
                            .centerCrop()
                            .apply(RequestOptions().circleCrop())
                            .into(img_profile_after)
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })

        bind {
            btnLoginLogout.visibility = View.VISIBLE
            layoutLoginAfter.visibility = View.VISIBLE
            layoutLoginBefore.visibility = View.GONE
        }
    }

    private fun onClickUnlink() {
        val appendMessage = getString(R.string.com_kakao_confirm_unlink)
        AlertDialog.Builder(activity)
            .setMessage(appendMessage)
            .setPositiveButton(
                getString(R.string.com_kakao_ok_button)
            ) { dialog, _ ->
                UserManagement.requestUnlink(object : UnLinkResponseCallback() {
                    override fun onFailure(errorResult: ErrorResult?) {
                        Logger.e(errorResult!!.toString())
                    }

                    override fun onSessionClosed(errorResult: ErrorResult) {
                        //redirectLoginActivity()
                    }

                    override fun onNotSignedUp() {
                        //redirectSignupActivity()
                    }

                    override fun onSuccess(userId: Long?) {
                        //redirectLoginActivity()
                    }
                })
                dialog.dismiss()
            }
            .setNegativeButton(
                getString(R.string.com_kakao_cancel_button)
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
                    val id = userProfile.id.toString()
                    var swit = true
                    val postListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (a in dataSnapshot.children)
                                if (a.key == id)
                                    swit = false

                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }
                    }
                    val nickname = userProfile.nickname
                    val profileImage = userProfile.profileImagePath
                    val thumbnailImage = userProfile.thumbnailImagePath
                    myRef.addValueEventListener(postListener)
                    if (swit)
                        myRef.child(id).setValue(
                            KakaoUserMember(
                                nickname,
                                profileImage,
                                thumbnailImage
                            )
                        )
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