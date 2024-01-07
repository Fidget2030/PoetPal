package com.example.poetpal.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data access interface for the word table in roomDb
 */
@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: DbWord)

    @Query("DELETE FROM words")
    suspend fun nukeWords()

    @Query("Select * FROM words WHERE word= :word")
    fun getWord(word: String): DbWord

    @Query("Select count() from words where word= :word")
    fun wordExists(word: String): Int
}
