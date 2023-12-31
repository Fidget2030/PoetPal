package com.example.poetpal.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.poetpal.domain.PoemMeta
import kotlinx.coroutines.flow.Flow

/**
 * Data access interface for the poem table in roomDb
 */
@Dao
interface PoemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: DbPoem)

    @Query("SELECT title,author,type FROM poems ORDER BY author ASC")
    fun getAllMeta(): Flow<List<PoemMeta>>

    @Query("SELECT * FROM poems ORDER BY RANDOM() LIMIT 1")
    fun getRandom(): Flow<DbPoem>

    @Query("SELECT * FROM poems WHERE title= :title")
    fun getByTitle(title: String): Flow<DbPoem>
}
