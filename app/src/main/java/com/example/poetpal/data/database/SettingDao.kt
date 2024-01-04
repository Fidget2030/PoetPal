package com.example.poetpal.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: dbSetting)

    @Query("SELECT * from settings WHERE name= :name")
    fun getSettings(name: String = "Settings"): Flow<dbSetting>

    @Update
    suspend fun update(item: dbSetting)

    @Delete
    suspend fun delete(item: dbSetting)
}
