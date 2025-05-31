package com.example.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.core.data.local.dao.RequestDao
import com.example.core.data.local.entity.RequestEntity

@Database(
    entities = [RequestEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RequestDatabase : RoomDatabase() {
    abstract val dao: RequestDao
}
