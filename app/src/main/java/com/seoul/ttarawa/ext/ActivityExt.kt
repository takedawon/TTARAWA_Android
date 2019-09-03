package com.seoul.ttarawa.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

fun AppCompatActivity.addFragmentInActivity(containerId: Int, fragment: Fragment) {
    supportFragmentManager.commit {
        add(containerId, fragment)
    }
}

fun AppCompatActivity.replaceFragmentInActivity(containerId: Int, fragment: Fragment) {
    supportFragmentManager.commit {
        replace(containerId, fragment)
        addToBackStack(null)
    }
}