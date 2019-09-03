package com.seoul.ttarawa.ui.main

import android.os.Bundle
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityMainBinding
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBinding>(
    R.layout.activity_main
) {
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        bind {
            vm = mainViewModel
            lifecycleOwner = this@MainActivity

            with(babMain) {
                setNavigationOnClickListener {}

                replaceMenu(R.menu.menu_main_setting)
                setOnMenuItemClickListener {
                    if (it.itemId == R.id.menu_setting) {
                        toast("setting")
                        return@setOnMenuItemClickListener true
                    }
                    return@setOnMenuItemClickListener false
                }
            }
        }
    }
}

