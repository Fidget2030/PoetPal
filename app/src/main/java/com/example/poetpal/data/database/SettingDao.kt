package com.example.poetpal.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access interface for the Settings table in roomDB
 */
@Dao
interface SettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: DbSetting)

    @Query("SELECT * from settings WHERE name= :name")
    fun getSettings(name: String = "Settings"): Flow<DbSetting>

    @Update
    suspend fun update(item: DbSetting)

    @Delete
    suspend fun delete(item: DbSetting)
}
