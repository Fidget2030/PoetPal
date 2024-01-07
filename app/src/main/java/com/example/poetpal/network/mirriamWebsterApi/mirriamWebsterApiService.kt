package com.example.poetpal.network.mirriamWebsterApi

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val KEY = "98b1d333-3901-4542-8e91-016089505557"

interface WebsterApiService {
    @GET("{word}")
    suspend fun getWordFromWebster(
        @Path("word") word: String,
        @Query("key") key: String = KEY,
    ): List<ApiWebsterEntry>

    @GET("{word}")
    suspend fun getAlternatives(
        @Path("word") word: String,
        @Query("key") key: String = KEY,
    ): List<String>
}
