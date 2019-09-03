package com.seoul.ttarawa.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.seoul.ttarawa.R

fun Fragment.addInFragment(containerId: Int, fragment: Fragment) {
    activity?.supportFragmentManager?.commit {
        add(containerId, fragment)
    }
}

fun Fragment.replaceInFragment(containerId: Int, fragment: Fragment) {
    activity?.supportFragmentManager?.commit {
        setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        replace(containerId, fragment)
        addToBackStack(null)
    }
}