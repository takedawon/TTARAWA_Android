package com.seoul.ttarawa.ui.main

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
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
import com.seoul.ttarawa.ui.main.news.NewsActivity
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SettingFragment : BaseFragment<FragmentSettingBinding>(
    R.layout.fragment_setting
) {
    private lateinit var database: FirebaseDatabase
    private lateinit var session: SessionCallback
    private lateinit var myRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var file: Uri? = null
    private var state: Boolean = true

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val currentUser = auth.currentUser
        myRef = database.getReference("USER")
        if (currentUser != null)
            updateUI(currentUser.uid)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()

        session = SessionCallback()
        if (!Session.getCurrentSession().isOpenable) {
            Session.getCurrentSession().addCallback(session)
            Session.getCurrentSession().checkAndImplicitOpen()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        } else if (resultCode == RESULT_OK) {
            if (requestCode == 1000) { // LoginActivity
                updateUI(data!!.getStringExtra("uid"))
            } else if (requestCode == 2000) { // 이미지 가져오기
                file = data!!.data // 선택한 사진 file에 대입
            }
        }
    }

    override fun onDestroy() {
        Session.getCurrentSession().removeCallback(session)
        super.onDestroy()
    }

    override fun initView() {
        bind {

            btnSettingBack.setOnClickListener { (activity as? MainActivity)?.onBackPressed() }

            btnLoginJoin.setOnClickListener {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivityForResult(intent, 1000)
            }

            btnLoginLogout.setOnClickListener {
                MaterialAlertDialogBuilder(context)
                    .setTitle("로그아웃 하시겠습니까?")
                    .setPositiveButton("네") { _, _ ->
                        auth.signOut()
                        setLogoutView()
                        toast("로그아웃 되었습니다.")
                    }
                    .setNegativeButton("아니오", null)
                    .show()
            }

            txtSettingNews.setOnClickListener {
                val intent = Intent(context, NewsActivity::class.java)
                startActivity(intent)
            }

            btnLoginJoinAfter.setOnClickListener {
                // 프로필 편집 변수 state 기본값=true
                if (state) {
                    txtProfileAfterEdit.visibility = View.VISIBLE // 프로필 "편집" 텍스트 보이게하기
                    btnLoginJoinAfter.setBackgroundColor(Color.parseColor("#0E2FFF"))
                    imgProfileAfter.setOnClickListener {
                        // 이미지 누를시 편집창
                        MaterialAlertDialogBuilder(context)
                            .setTitle("프로필 사진을 변경하시겠습니까?")
                            .setPositiveButton("네") { _, _ ->
                                val intent = Intent()
                                intent.type = "image/*"
                                intent.action = Intent.ACTION_GET_CONTENT
                                startActivityForResult(intent, 2000)
                            }
                            .setNegativeButton("아니오", null)
                            .show()
                    }
                    btnLoginJoinAfter.text = "완료" // 버튼 텍스트 수정
                    state = false
                } else {
                    if (file != null) { // 프로필 사진을 변경했을 경우
                        val storageRef = FirebaseStorage.getInstance().reference
                        val user = auth.currentUser!!.uid
                        val riversRef = storageRef.child("profileImage/$user")
                        val uploadTask = riversRef.putFile(file!!) // 파일 업로드

                        uploadTask.addOnFailureListener {
                        }.addOnSuccessListener {
                            riversRef.downloadUrl.addOnSuccessListener {
                                val ref = database.getReference("USER")
                                ref.child(user).child("profileImage").setValue(it.toString())
                            }
                        }.addOnCompleteListener {
                            it.getResult()
                        }
                    }
                    txtProfileAfterEdit.visibility = View.INVISIBLE // 프로필 "편집" 텍스트 안 보이게하기
                    btnLoginJoinAfter.setBackgroundColor(Color.parseColor("#D14D77"))
                    imgProfileAfter.isClickable = false
                    btnLoginJoinAfter.text = "프로필 편집"
                    state = true
                }
            }
        }
    }

    private fun setLogoutView() {
        bind {
            btnLoginLogout.visibility = View.GONE
            layoutLoginAfter.visibility = View.INVISIBLE
            layoutLoginBefore.visibility = View.VISIBLE
        }
    }

    private fun updateUI(data: String) {
        showProgressBar()
        myRef.child(data).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val nick = p0.child("nickname").value.toString()
                    val email = p0.child("email").value.toString()
                    val profileUrl = p0.child("profileImage").value.toString()

                    try {
                        bind {
                            txtSettingInfoAfter.text = nick + "님 환영합니다!"
                            txtLoginEmail.text = email
                            if (file == null) {
                                Glide.with(this@SettingFragment)
                                    .load(profileUrl)
                                    .centerCrop()
                                    .apply(RequestOptions().circleCrop())
                                    .into(imgProfileAfter)
                            } else {
                                Glide.with(this@SettingFragment)
                                    .load(file)
                                    .centerCrop()
                                    .apply(RequestOptions().circleCrop())
                                    .into(imgProfileAfter)
                            }
                        }
                        hideProgressBar()
                    } catch (e: NullPointerException) {
                        /*ignored*/
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
    }

    private fun showProgressBar() {
        bind {
            layoutLoginBefore.visibility = View.INVISIBLE
            layoutLoginAfter.visibility = View.INVISIBLE
            pbLoginAfter.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        bind {
            layoutLoginAfter.visibility = View.VISIBLE
            btnLoginLogout.visibility = View.VISIBLE
            pbLoginAfter.visibility = View.INVISIBLE
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

    override fun onStop() {
        Glide.with(this@SettingFragment).pauseRequests()
        super.onStop()
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