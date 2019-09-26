package com.seoul.ttarawa.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.kakao.usermgmt.StringSet.email
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityLoginBinding
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity<ActivityLoginBinding>(
    R.layout.activity_login
) {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        bind {
            binding.btnLoginBack.setOnClickListener {
                finish()
            }
            binding.txtLoginJoin.setOnClickListener {
                val intent = Intent(applicationContext, JoinActivity::class.java)
                startActivityForResult(intent, 2000)
            }
            binding.btnLogin.setOnClickListener {
                if (edtInputLoginEmail.text!!.isEmpty() || edtInputLoginPw.text!!.isEmpty())
                    toast("입력정보를 다시 확인해주세요.")
                else {
                    val email = edtInputLoginEmail.text.toString()
                    val pw = edtInputLoginPw.text.toString()
                    setFirebaseLogin(email,pw)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2000) { // JoinActivity
                val id = data!!.getStringExtra("id")
                val pw = data!!.getStringExtra("pw")
                setFirebaseLogin(id,pw)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initView() {
        auth = FirebaseAuth.getInstance()

    }

    private fun setFirebaseLogin(id:String, pw:String) {
        bind {
            auth.signInWithEmailAndPassword(id, pw)
                .addOnCompleteListener(this@LoginActivity) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser!!
                        toast("로그인에 성공하셨습니다.")
                        val intent = Intent()
                        intent.putExtra("uid", user.uid)
                        setResult(RESULT_OK, intent)
                        finish()
                    } else {
                        toast("아이디나 비밀번호가 맞지 않습니다.")
                    }
                }
        }
    }
}
