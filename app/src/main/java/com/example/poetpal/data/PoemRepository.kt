package com.example.poetpal.data

import com.example.poetpal.domain.Poem

interface PoemRepository {
    fun getRandomPoem(): Poem
}
