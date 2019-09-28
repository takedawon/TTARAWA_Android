package com.seoul.ttarawa.ext

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDay(format: String = "yyyyMMdd"): String {
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    return simpleDateFormat.format(Calendar.getInstance().time)
}

fun Int.getTextFormattedTime() =
    if (this < 10) {
        "0$this"
    } else {
        "$this"
    }