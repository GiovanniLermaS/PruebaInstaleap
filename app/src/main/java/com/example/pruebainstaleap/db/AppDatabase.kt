package com.example.pruebainstaleap.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pruebainstaleap.db.dao.ResultServiceDao
import com.example.pruebainstaleap.db.model.ResultService

@Database(
    entities = [ResultService::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun resultServiceDao(): ResultServiceDao
}
