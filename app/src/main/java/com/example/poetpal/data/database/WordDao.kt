package com.example.poetpal.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: dbWord)

    @Query("Select * FROM words WHERE word= :word")
    fun getWord(word: String): dbWord

    @Query("Select count() from words where word= :word")
    fun wordExists(word: String): Int
}
