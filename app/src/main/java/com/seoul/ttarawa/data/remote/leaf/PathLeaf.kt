package com.seoul.ttarawa.data.remote.leaf

import com.seoul.ttarawa.data.entity.Path
import com.seoul.ttarawa.data.local.entity.NodeEntity

data class PathLeaf(
    val title: String = "",
    val date: String = "",
    val shareYn: Boolean = false,
    val nodes: List<NodeEntity> = emptyList()
) {
    fun toPath(id: String) =
        Path(
            id = id,
            title = title,
            date = date,
            shareYn = shareYn
        )
}