package com.seoul.ttarawa.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.seoul.ttarawa.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    val home = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_layout, home)
            .commitAllowingStateLoss() // 첫 화면 지정 HomeFragment

        bottom_navigation_bar.setOnNavigationItemSelectedListener(this)


        /*
         * 해시키 값 출력 해주는 코드
         * 주석처리. 필요 시 해제해서 사용할 것.
         * try {
            val info = packageManager.getPackageInfo("com.seoul.ttarawa", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val str = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.d("KeyHash:", str)
                Toast.makeText(this, str, Toast.LENGTH_LONG).show()
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        */
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.bottom_home -> {
                val homeFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_layout, homeFragment).commit()
            }
            R.id.bottom_menu -> {
                val menuFragment = MenuFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_layout, menuFragment).commit()
            }
            R.id.bottom_info -> {
                val infoFragment = InfoFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_layout, infoFragment).commit()
            }
        }
        return true
    }
}

