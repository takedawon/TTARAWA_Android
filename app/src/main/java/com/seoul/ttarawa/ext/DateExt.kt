package com.seoul.ttarawa.ext

import timber.log.Timber
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

fun String.simpleDateFormat(original: String = "yyyyMMddHHmmss", format: String = "yyMMdd"): String {

    val dateFormat = SimpleDateFormat(original, Locale.getDefault())
    val simpleFormat = SimpleDateFormat(format, Locale.getDefault())

    return try {
        simpleFormat.format(dateFormat.parse(this))
    } catch (e: Exception) {
        Timber.w(e)
        this
    }
}