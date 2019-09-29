package com.seoul.ttarawa.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.seoul.ttarawa.data.entity.BaseSearchEntity
import com.seoul.ttarawa.data.entity.BaseSearchEntityImpl

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
    val latitude: Double = 0.0,
    @ColumnInfo(name = "longitude")
    val longitude: Double = 0.0,
    @ColumnInfo(name = "categoryCode")
    val categoryCode: Int = 0,
    @ColumnInfo(name = "startTime")
    val startTime: String = "",
    @ColumnInfo(name = "endTime")
    val endTime: String = "",
    @ColumnInfo(name = "markerYn")
    val markerYn: Boolean = false
) {
    fun toBaseSearchEntity(): BaseSearchEntity {
        return BaseSearchEntityImpl(
            categoryCode = categoryCode,
            startTime = startTime,
            endTime = endTime,
            latitude = latitude,
            longitude = longitude,
            title = title,
            address = address,
            content = content
        )
    }

    @ColumnInfo(name = "pathId")
    var pathId: Int = 0
}