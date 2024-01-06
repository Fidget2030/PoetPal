package com.example.poetpal.data

import android.content.Context
import android.util.Log
import com.example.poetpal.data.database.WordDao
import com.example.poetpal.data.database.asDomainWord
import com.example.poetpal.domain.Word
import com.example.poetpal.network.mirriamWebsterApi.SyllableApiService

private const val KEY = "98b1d333-3901-4542-8e91-016089505557"

interface WordsRepository {
    suspend fun getWord(word: String): Word
}

class CachingWordsRepository(
    private val wordDao: WordDao,
    private val syllableApiService: SyllableApiService,
    context: Context,
) : WordsRepository {
    override suspend fun getWord(word: String): Word {
        if (wordDao.wordExists(word) > 0) {
            return wordDao.getWord(word).asDomainWord()
        } else {
            val response = syllableApiService.getWordFromWebster(word, KEY)
            Log.d("getSyllables", response[0].hwi.hw)
            val syllables = response[0].hwi.hw.split("*")
            return Word(word, syllables, listOf(""), listOf(""))
        }
    }
}
