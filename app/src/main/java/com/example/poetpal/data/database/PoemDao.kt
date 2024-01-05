package com.example.poetpal.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PoemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: dbPoem)

    @Query("SELECT title FROM poems")
    fun getAll(): Flow<List<String>>

    @Query("SELECT * FROM poems ORDER BY RANDOM() LIMIT 1")
    fun getRandom(): Flow<dbPoem>

    @Query("SELECT * FROM poems WHERE title= :title")
    fun getByTitle(title: String): Flow<dbPoem>
}
