package com.seoul.ttarawa.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

class PathAndAllNodes(

    @Embedded
    var path: PathEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "pathId"
    )
    var nodes: List<NodeEntity>
)