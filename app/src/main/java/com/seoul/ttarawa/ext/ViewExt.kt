package com.seoul.ttarawa.ext

import android.app.Service
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.BindingAdapter

/**
 * setOnClickListener() 를 확장함수를 사용하여 간결하게 사용
 */
infix fun View?.click(block: (View) -> Unit) = this?.setOnClickListener(block)

@BindingAdapter(value = ["isVisible"])
fun View.isVisible(visible: Boolean?) {
    this.visibility = visible?.let {
        if (visible) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    } ?: View.INVISIBLE
}

@BindingAdapter(value = ["isNotGone"])
fun View.isGone(visible: Boolean?) {
    this.visibility = visible?.let {
        if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    } ?: View.GONE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

@BindingAdapter(value = ["isEnabled"])
fun View.isEnable(enable: Boolean?) {
    this.isEnabled = enable ?: true
}

fun View.showKeyboard() {
    val imm = this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager

    imm.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    val imm = this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager

    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

