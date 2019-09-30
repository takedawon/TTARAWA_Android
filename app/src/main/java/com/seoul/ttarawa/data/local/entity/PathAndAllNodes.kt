package com.seoul.ttarawa.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.seoul.ttarawa.data.remote.leaf.PathLeaf

class PathAndAllNodes(

    @Embedded
    var path: PathEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "pathId"
    )
    var nodes: List<NodeEntity>
) {
    fun toPathLeaf() =
        PathLeaf(
            title = path.title,
            date = path.date,
            shareYn = !path.shareYn,
            nodes = nodes
        )
}