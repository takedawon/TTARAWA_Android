package com.seoul.ttarawa.ui.main.setting

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseFragment
import com.seoul.ttarawa.databinding.FragmentSettingBinding
import com.seoul.ttarawa.ui.main.LoginActivity
import com.seoul.ttarawa.ui.main.MainActivity
import com.seoul.ttarawa.ui.main.news.NewsActivity
import org.jetbrains.anko.support.v4.toast


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SettingFragment : BaseFragment<FragmentSettingBinding>(
    R.layout.fragment_setting
) {
    private lateinit var database: FirebaseDatabase
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

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) { // LoginActivity
                updateUI(data!!.getStringExtra("uid"))
            } else if (requestCode == 2000) { // 이미지 가져오기
                file = data!!.data // 선택한 사진 file에 대입
            }
        }
    }


    override fun onDestroy() {
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
                MaterialAlertDialogBuilder(requireContext())
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
                        MaterialAlertDialogBuilder(requireContext())
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


    override fun onStop() {
        Glide.with(this@SettingFragment).pauseRequests()
        super.onStop()
    }

    companion object {
        fun newInstance(): SettingFragment {
            return SettingFragment()
        }
    }
}