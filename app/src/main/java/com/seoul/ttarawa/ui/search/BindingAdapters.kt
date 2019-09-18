package com.seoul.ttarawa.ui.search

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.seoul.ttarawa.module.GlideApp

@BindingAdapter("photoUrl")
fun ImageView.setDrawable(url: String) {
    GlideApp.with(this)
        .load(url)
        .into(this)
}