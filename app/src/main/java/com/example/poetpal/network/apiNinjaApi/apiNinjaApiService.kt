package com.example.poetpal.network.apiNinjaApi

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiNinjaApiService {
    @GET("thesaurus")
    suspend fun getRelated(
        @Query("word") word: String,
    ): ThesaurusEntry

    @GET("rhyme")
    suspend fun getRhyme(
        @Query("word") word: String,
    ): List<String>
}
