package com.seoul.ttarawa.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seoul.ttarawa.data.local.dao.*
import com.seoul.ttarawa.data.local.entity.*

@Database(
    entities = [
        NodeEntity::class,
        PathEntity::class
    ],
    version = 1
)
abstract class LocalDataBase : RoomDatabase() {

    abstract fun getNodeDao(): NodeDao

    abstract fun getPathDao(): PathDao
}
