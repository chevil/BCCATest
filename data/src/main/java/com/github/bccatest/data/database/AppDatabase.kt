package com.github.bccatest.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.bccatest.data.dao.AlbumDao
import com.github.bccatest.data.model.AlbumEntity

@Database(
    entities = [AlbumEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}
