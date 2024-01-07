package com.example.poetpal.data.repositories

import android.content.Context
import android.util.Log
import com.example.poetpal.data.database.WordDao
import com.example.poetpal.data.database.asDbWord
import com.example.poetpal.data.database.asDomainWord
import com.example.poetpal.domain.Word
import com.example.poetpal.network.apiNinjaApi.ApiNinjaApiService
import com.example.poetpal.network.dataMuseApi.DataMuseApiService
import com.example.poetpal.network.mirriamWebsterApi.WebsterApiService

interface WordsRepository {
    suspend fun getWord(word: String): Word

    suspend fun nukeWords()

    suspend fun getNrOfSyllables(word: String): Int

    suspend fun getAlternatives(word: String): List<String>
}

class CachingWordsRepository(
    private val wordDao: WordDao,
    private val websterApiService: WebsterApiService,
    private val apiNinjaApiService: ApiNinjaApiService,
    private val dataMuseApiService: DataMuseApiService,
    context: Context,
) : WordsRepository {
    override suspend fun getWord(word: String): Word {
        val filteredWord = word.filter { it.isLetter() }.lowercase()
        return if (wordDao.wordExists(filteredWord) > 0) {
            wordDao.getWord(filteredWord.lowercase()).asDomainWord()
        } else {
            Log.i("WORDREPO", "MAKING NETWORK CALLS")
            val foundWords = websterApiService.getWordFromWebster(filteredWord)
            val syllables = foundWords[0].hwi.hw.split("*")
            val thesaurus = apiNinjaApiService.getRelated(filteredWord)
            val rhymes = apiNinjaApiService.getRhyme(filteredWord).take(5)
            val synonyms = thesaurus.synonyms.take(5)
            val antonyms = thesaurus.antonyms.take(5)
            val response = Word(filteredWord, syllables, rhymes, synonyms, antonyms)
            wordDao.insert(response.asDbWord())
            response
        }
    }

    override suspend fun getAlternatives(word: String): List<String> {
        Log.i("WORDREPO", "MAKING NETWORK CALLS")
        return websterApiService.getAlternatives(word)
    }

    override suspend fun getNrOfSyllables(word: String): Int {
        return dataMuseApiService.getSyllableCount(word)[0].numSyllables
    }

    override suspend fun nukeWords() {
        wordDao.nukeWords()
    }
}
