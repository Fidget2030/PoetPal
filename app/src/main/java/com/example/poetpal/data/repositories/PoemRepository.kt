package com.example.poetpal.data.repositories

import android.content.Context
import com.example.poetpal.data.database.PoemDao
import com.example.poetpal.data.database.asDbPoem
import com.example.poetpal.data.database.asDomainPoem
import com.example.poetpal.domain.Poem
import com.example.poetpal.domain.PoemMeta
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PoemRepository {
    fun getPoemMetas(): Flow<List<PoemMeta>>

    fun getPoemByTitle(title: String): Flow<Poem>

    fun getRandomPoem(): Flow<Poem>

    suspend fun addPoem(poem: Poem)
}

class LocalPoemRepository(private val poemDao: PoemDao, context: Context) : PoemRepository {
    override suspend fun addPoem(poem: Poem) {
        poemDao.insert(poem.asDbPoem())
    }

    override fun getRandomPoem(): Flow<Poem> {
        return poemDao.getRandom().map { it.asDomainPoem() }
    }

    override fun getPoemByTitle(title: String): Flow<Poem> {
        return poemDao.getByTitle(title).map { it.asDomainPoem() }
    }

    override fun getPoemMetas(): Flow<List<PoemMeta>> {
        return poemDao.getAllMeta()
    }
}
