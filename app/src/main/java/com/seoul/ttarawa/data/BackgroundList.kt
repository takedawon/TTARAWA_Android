package com.seoul.ttarawa.data

import com.seoul.ttarawa.R
import kotlin.random.Random

val backgroundList = listOf(
    R.drawable.item_bg1,
    R.drawable.item_bg2,
    R.drawable.item_bg3,
    R.drawable.item_bg4,
    R.drawable.item_bg5,
    R.drawable.item_bg6,
    R.drawable.item_bg7,
    R.drawable.item_bg8,
    R.drawable.item_bg9,
    R.drawable.item_bg10,
    R.drawable.item_bg11,
    R.drawable.item_bg12,
    R.drawable.item_bg13,
    R.drawable.item_bg14,
    R.drawable.item_bg15,
    R.drawable.item_bg16,
    R.drawable.item_bg17,
    R.drawable.item_bg18,
    R.drawable.item_bg19,
    R.drawable.item_bg20
)

fun getBackgroundResId() = backgroundList[Random.nextInt(backgroundList.size)]