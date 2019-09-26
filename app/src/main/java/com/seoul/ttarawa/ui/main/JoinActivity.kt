package com.seoul.ttarawa.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityJoinBinding
import org.jetbrains.anko.toast
import java.util.regex.Pattern


class JoinActivity : BaseActivity<ActivityJoinBinding>(
    R.layout.activity_join
) {
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        bind {
            btnLogin.setOnClickListener {
                val id = txtInputJoinEmail.text.toString()
                val pw = txtInputJoinPw.text.toString()
                val nickname = txtInputJoinNick.text.toString()
                if (id.isEmpty() || pw.isEmpty() || nickname.isEmpty()) {
                    toast("입력 정보가 비어있지 않은지 확인해주세요.")
                } else {
                    auth.createUserWithEmailAndPassword(id, pw)
                        .addOnCompleteListener(this@JoinActivity) { task ->
                            if (task.isSuccessful) {
                                toast("회원가입이 완료되었습니다.")
                                val user = auth.currentUser
                                user?.let {
                                    ref.child(user.uid).child("email").setValue(id)
                                    ref.child(user.uid).child("nickname").setValue(nickname)
                                }
                                finish()
                            } else {
                                toast("이미 있는 아이디거나 정보를 다시 확인해주세요.")
                            }
                        }
                }
            }
        }
    }

    override fun initView() {
        bind {
            auth = FirebaseAuth.getInstance()
            ref = FirebaseDatabase.getInstance().getReference("USER")
            setEventTextwatcher(layoutTextInputEmail, txtInputJoinEmail)
            setEventTextwatcher(layoutTextInputPw, txtInputJoinPw)
            setEventTextwatcher(layoutTextInputNick, txtInputJoinNick)
        }
    }

    private fun setEventTextwatcher(view: TextInputLayout, editText: TextInputEditText) {
        val watcher = object : TextWatcher { // 아이디
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
                bind {
                    if (view != layoutTextInputPw) {
                        if (isTextCheck(s.toString())) // 특수문자가 있으면 true
                            view.helperText = "공백은 입력할 수 없습니다."
                        else
                            view.helperText = ""
                    } else {
                        if (txtInputJoinPw.length() < 6)
                            view.helperText = "비밀번호는 6자리 이상이어야 합니다."
                        else
                            view.helperText = ""
                    }

                    if (view == layoutTextInputNick) {
                        ref.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                for (data in p0.children) {
                                    if (data.child("nickname").value!! == txtInputJoinNick.text.toString()) {
                                        view.helperText = "같은 이름의 닉네임이 존재합니다."
                                        btnLogin.isClickable = false
                                        break
                                    } else
                                        btnLogin.isClickable = true
                                }
                            }

                            override fun onCancelled(p0: DatabaseError) {
                            }
                        })
                    }
                }
                if (s.toString().length > 20)
                    view.helperText = "20자 이상은 입력할 수 없습니다."
            }

        }
        bind {
            editText.addTextChangedListener(watcher)
        }
    }

    private fun isTextCheck(text: String): Boolean { // 특수문자가 있으면 true
        val pattern = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-@.]*$"
        return !Pattern.matches(pattern, text)
    }

}
