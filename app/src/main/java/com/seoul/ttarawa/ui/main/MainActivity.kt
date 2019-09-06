package com.seoul.ttarawa.ui.main

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityMainBinding
import com.seoul.ttarawa.ext.addFragmentInActivity
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.ext.replaceFragmentInActivity
import com.seoul.ttarawa.ui.home.HomeFragment
import com.seoul.ttarawa.ui.plan.PlanActivity
import com.seoul.ttarawa.ui.setting.SettingFragment
import com.seoul.ttarawa.util.LocationUtil
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBinding>(
    R.layout.activity_main
) {
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initView() {
        bind {
            vm = mainViewModel

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

            fabMain click { startActivity<PlanActivity>() }

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
            false)

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

