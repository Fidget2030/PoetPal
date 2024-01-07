package com.example.poetpal.network.dataMuseApi

import retrofit2.http.GET
import retrofit2.http.Query

interface DataMuseApiService {
    @GET("words")
    suspend fun getSyllableCount(
        @Query("sp") word: String,
        @Query("qe") qe: String = "sp",
        @Query("md") md: String = "s",
        @Query("max") max: String = "1",
    ): List<DataMuseEntry>
}
