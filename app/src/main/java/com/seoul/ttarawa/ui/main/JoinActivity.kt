package com.seoul.ttarawa.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityJoinBinding
import org.jetbrains.anko.toast
import java.util.regex.Pattern


class JoinActivity : BaseActivity<ActivityJoinBinding>(
    R.layout.activity_join
) {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        val ref = FirebaseDatabase.getInstance().getReference("USER")
        bind {
            btnLogin.setOnClickListener {
                val id= txtInputJoinEmail.text.toString()
                val pw= txtInputJoinPw.text.toString()
                val nickname= txtInputJoinNick.text.toString()
                auth.createUserWithEmailAndPassword(id, pw)
                    .addOnCompleteListener(this@JoinActivity) { task ->
                        if (task.isSuccessful) {
                            toast("회원가입이 완료되었습니다.")
                            val user = auth.currentUser
                            user?.let {
                                ref.child(user.uid).child("email").setValue(id)
                                ref.child(user.uid).child("nickname").setValue(nickname)
                            }
                            val intent = Intent(this@JoinActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            toast("이미 있는 아이디거나 정보를 다시 확인해주세요.")
                        }
                    }
            }
        }
    }

    override fun initView() {
        bind {
            auth = FirebaseAuth.getInstance()
            setEventTextwatcher(layoutTextInputEmail, txtInputJoinEmail)
            setEventTextwatcher(layoutTextInputPw, txtInputJoinPw)
            setEventTextwatcher(layoutTextInputNick, txtInputJoinNick)
        }
    }

    private fun setEventTextwatcher(view:TextInputLayout, editText: TextInputEditText) {
        val watcher= object:TextWatcher { // 아이디
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(view != binding.layoutTextInputPw)
                    if(isTextCheck(s.toString())) // 특수문자가 있으면 true
                        view.helperText="공백은 입력할 수 없습니다."
                    else
                        view.helperText=""

                    if(s.toString().length>20)
                        view.helperText="20자 이상은 입력할 수 없습니다."
            }

        }
        bind {
            editText.addTextChangedListener(watcher)
        }
    }

    private fun isTextCheck(text:String) : Boolean { // 특수문자가 있으면 true
        val pattern = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-@.]*$"
        return !Pattern.matches(pattern, text)
    }

}
