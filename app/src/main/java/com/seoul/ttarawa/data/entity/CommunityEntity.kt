package com.seoul.ttarawa.data.entity

import com.seoul.ttarawa.data.getBackgroundResId
import com.seoul.ttarawa.data.local.entity.NodeEntity

data class CommunityEntity(
    val id: String,
    val userName: String,
    val title: String,
    val date: String,
    val backgroundResId: Int = getBackgroundResId(),
    var shareYn: Boolean,
    val nodes: List<NodeEntity>
)