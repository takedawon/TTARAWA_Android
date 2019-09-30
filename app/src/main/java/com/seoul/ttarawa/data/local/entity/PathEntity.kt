package com.seoul.ttarawa.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.seoul.ttarawa.data.entity.Path

@Entity(tableName = "path_tb")
class PathEntity constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "share_yn")
    val shareYn: Boolean = false
) {
    fun toPath() =
        Path(
            id = id.toString(),
            title = title,
            date = date,
            shareYn = shareYn,
            nodes = emptyList()
        )
}