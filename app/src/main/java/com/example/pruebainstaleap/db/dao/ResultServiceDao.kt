package com.example.pruebainstaleap.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pruebainstaleap.db.model.ResultService


@Dao
interface ResultServiceDao {
    @Query("SELECT * FROM ResultService")
    suspend fun getResultService(): List<ResultService>

    @Query("SELECT * FROM ResultService WHERE id=:id")
    suspend fun getResultServiceById(id: Int): ResultService

    @Query("DELETE FROM ResultService WHERE id=:id")
    suspend fun deleteResultServiceById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setResultService(resultService: ResultService): Long
}
