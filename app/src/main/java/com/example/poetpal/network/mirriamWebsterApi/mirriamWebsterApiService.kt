package com.example.poetpal.network.mirriamWebsterApi

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SyllableApiService {
    @GET("{word}")
    suspend fun getWordFromWebster(
        @Path("word") word: String,
        @Query("key") key: String,
    ): List<ApiWebsterEntry>
}
