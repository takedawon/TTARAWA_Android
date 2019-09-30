package com.seoul.ttarawa.data.entity

import com.seoul.ttarawa.data.local.entity.NodeEntity
import com.seoul.ttarawa.data.local.entity.PathEntity
import com.seoul.ttarawa.data.remote.leaf.PathLeaf

data class Path(
    val id: String,
    val title: String,
    val date: String,
    var shareYn: Boolean,
    val nodes: List<NodeEntity>
) {
    fun toPathEntity() =
        PathEntity(
            id = id.toInt(),
            title = title,
            date = date,
            shareYn = shareYn
        )
}