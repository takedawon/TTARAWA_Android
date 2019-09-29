package com.seoul.ttarawa.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.seoul.ttarawa.data.local.entity.PathAndAllNodes
import com.seoul.ttarawa.data.local.entity.PathEntity

@Dao
abstract class PathDao: BaseDao<PathEntity> {

    @Transaction
    @Query("SELECT * FROM path_tb")
    abstract fun getPathWithNodes(): List<PathAndAllNodes>

    @Query("SELECT * FROM path_tb ORDER BY id DESC LIMIT 1")
    abstract fun getLastPathId(): Int

    @Query("SELECT * FROM path_tb")
    abstract fun getPathList(): List<PathEntity>
}