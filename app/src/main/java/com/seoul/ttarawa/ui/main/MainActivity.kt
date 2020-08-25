package com.seoul.ttarawa.ui.main

import android.os.Bundle
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityMainBinding
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.ui.calendar.CalendarActivity
import com.seoul.ttarawa.ui.main.community.CommunityActivity
import com.seoul.ttarawa.ui.main.home.HomeFragment
import com.seoul.ttarawa.ui.main.setting.SettingFragment
import com.seoul.ttarawa.ui.mypath.MyPathActivity
import org.jetbrains.anko.startActivity
import timber.log.Timber


class MainActivity :
    BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private var lastClickTimeMenuSetting: Long = 0
    private lateinit var homeFragment: HomeFragment
    private lateinit var settingFragment: SettingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        bind {
            babMain.setNavigationOnClickListener { openNavigationMenu() }
            setOnMenuItemClickListener()

            fabMain click { startActivity<CalendarActivity>() }
        }

        initFragment()
    }

    private fun initFragment() {
        homeFragment = HomeFragment.newInstance()
        settingFragment = SettingFragment.newInstance()

        addFragment(homeFragment, MainFragmentTags.HOME, false)
    }

    private fun addFragment(
        fragment: Fragment,
        mainFragmentTags: MainFragmentTags,
        saveBackStack: Boolean = true
    ) {
        supportFragmentManager.commit {
            applyTransition(this, mainFragmentTags)
            changeOptionBabMain(mainFragmentTags)

            add(R.id.container_main, fragment, mainFragmentTags.name)

            if (saveBackStack) {
                addToBackStack(null)
            }

            if (fragment !is HomeFragment) {
                hide(homeFragment)
            }
        }
    }

    /**
     * 트랜잭션 추가
     */
    private fun applyTransition(
        fragmentTransaction: FragmentTransaction,
        mainFragmentTags: MainFragmentTags
    ) {
        fragmentTransaction.apply {
            when (mainFragmentTags) {
                MainFragmentTags.HOME -> {
                    setCustomAnimations(
                        0,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_left
                    )
                }
                MainFragmentTags.SETTING -> {
                    setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                }
            }
        }
    }

    /**
     * bottom app bar 옵션 수정
     */
    private fun changeOptionBabMain(mainFragmentTags: MainFragmentTags) {
        when (mainFragmentTags) {
            MainFragmentTags.HOME -> {
                replaceMenuBottomAppBar(R.menu.menu_main_setting)
                moveFabCenter()
            }
            MainFragmentTags.SETTING -> {
                clearMenuBottomAppBar()
                moveFabEnd()
            }
        }
    }

    override fun onBackPressed() {
        // 맨위 프래그먼트가 설정 일 때 홈 옵션으로 변경
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container_main)

        if (currentFragment?.tag ?: "" == MainFragmentTags.SETTING.name) {
            changeOptionBabMain(MainFragmentTags.HOME)
        }
        super.onBackPressed()
    }

    private fun moveFabCenter() {
        binding.babMain.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
    }

    private fun moveFabEnd() {
        binding.babMain.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
    }

    private fun clearMenuBottomAppBar() {
        binding.babMain.menu.clear()
    }

    private fun replaceMenuBottomAppBar(@MenuRes menuId: Int) {
        binding.babMain.replaceMenu(menuId)
    }

    private fun setOnMenuItemClickListener() {
        binding.babMain.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { menu ->
            if (menu.itemId == R.id.menu_setting) {
                val currentClickTimeMenuSetting = System.currentTimeMillis()

                Timber.e("$currentClickTimeMenuSetting $lastClickTimeMenuSetting")

                if (lastClickTimeMenuSetting + 1500 <= currentClickTimeMenuSetting) {

                    lastClickTimeMenuSetting = currentClickTimeMenuSetting

                    addFragment(settingFragment, MainFragmentTags.SETTING)
                    return@OnMenuItemClickListener true
                }
            }
            return@OnMenuItemClickListener false
        })
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

        bottomNavigation.findViewById<ConstraintLayout>(R.id.ll_navi_my_path).setOnClickListener {
            startActivity<MyPathActivity>()
            dialog.dismiss()
        }

        bottomNavigation.findViewById<ConstraintLayout>(R.id.ll_navi_community).setOnClickListener {
            startActivity<CommunityActivity>()
            dialog.dismiss()
        }
    }
}


