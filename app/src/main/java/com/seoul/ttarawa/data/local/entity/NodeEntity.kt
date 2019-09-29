package com.seoul.ttarawa.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "node_tb",
    foreignKeys = [
        ForeignKey(
            entity = PathEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("pathId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class NodeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "nodeId")
    val nodeId: Int = 0,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "content")
    val content: String = "",
    @ColumnInfo(name = "address")
    val address: String = "",
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "categoryCode")
    val categoryCode: Int = 0,
    @ColumnInfo(name = "startTime")
    val startTime: String = "",
    @ColumnInfo(name = "endTime")
    val endTime: String = "",
    @ColumnInfo(name = "markerYn")
    val markerYn: Boolean = false
) {
    @ColumnInfo(name = "pathId")
    var pathId: Int = 0
}