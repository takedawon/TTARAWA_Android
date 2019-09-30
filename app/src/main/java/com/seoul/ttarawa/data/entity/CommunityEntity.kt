package com.seoul.ttarawa.data.entity

import com.seoul.ttarawa.R
import com.seoul.ttarawa.data.local.entity.NodeEntity
import kotlin.random.Random

data class CommunityEntity(
    val id: String,
    val userName: String,
    val title: String,
    val date: String,
    val backgroundResId: Int = TEST_DATAS[Random.nextInt(TEST_DATAS.size)],
    var shareYn: Boolean,
    val nodes: List<NodeEntity>
)

private val TEST_DATAS = listOf(
    R.color.color_1,
    R.color.color_2,
    R.color.color_3,
    R.color.color_4,
    R.color.color_5,
    R.color.color_6,
    R.color.color_7,
    R.color.color_8,
    R.color.color_9,
    R.color.color_10,
    R.color.color_11,
    R.color.color_12,
    R.color.color_13,
    R.color.color_14,
    R.color.color_15,
    R.color.color_16,
    R.color.color_17,
    R.color.color_18,
    R.color.color_19,
    R.color.color_20,
    R.color.color_21,
    R.color.color_22,
    R.color.color_23,
    R.color.color_24,
    R.color.color_25,
    R.color.color_26
)