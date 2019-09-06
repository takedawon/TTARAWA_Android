package com.seoul.ttarawa.ui.main

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityMainBinding
import com.seoul.ttarawa.ext.addFragmentInActivity
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.ext.replaceFragmentInActivity
import com.seoul.ttarawa.ui.main.home.HomeFragment
import com.seoul.ttarawa.ui.map.CalendarActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class MainActivity : BaseActivity<ActivityMainBinding>(
    R.layout.activity_main
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    override fun initView() {
        bind {
            with(babMain) {
                setNavigationOnClickListener {
                    openNavigationMenu()
                }

                replaceMenu(R.menu.menu_main_setting)
                setOnMenuItemClickListener {
                    if (it.itemId == R.id.menu_setting) {
                        replaceFragmentInActivity(R.id.container_main, SettingFragment.newInstance())
                        return@setOnMenuItemClickListener true
                    }
                    return@setOnMenuItemClickListener false
                }
            }

            fabMain click { startActivity<CalendarActivity>() }

        }

        addHomeFragment()
    }

    /**
     * 좌측 햄버거 버튼 클릭
     */
    private fun openNavigationMenu() {
        val dialog: BottomSheetDialog
        val bottomNavigation = layoutInflater.inflate(
            R.layout.layout_main_navigation,
            null,
            false
        )

        dialog = BottomSheetDialog(this@MainActivity).apply {
            setContentView(bottomNavigation)
        }
        dialog.show()

        bottomNavigation.findViewById<NavigationView>(R.id.navigation_main_menu).apply {
            setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.bottom_home -> {
                        toast("bottom_home")
                    }
                    R.id.bottom_info -> {
                        toast("bottom_info")
                    }
                    R.id.bottom_menu -> {
                        toast("bottom_menu")
                    }
                }
                dialog.dismiss()
                return@setNavigationItemSelectedListener false
            }
        }
    }

    private fun addHomeFragment() {
        addFragmentInActivity(R.id.container_main, HomeFragment.newInstance())
    }
}

