package com.seoul.ttarawa.ui.main

import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar

interface MainBottomAppBarListener {
    fun moveFabCenter()

    fun moveFabEnd()

    fun clearMenuBottomAppBar()

    fun replaceMenuBottomAppBar(@MenuRes menuId: Int)

    fun setOnMenuItemClickListener(listener: Toolbar.OnMenuItemClickListener)

}