package com.example.poetpal.network.dataMuseApi

import kotlinx.serialization.Serializable

@Serializable
data class DataMuseEntry(
    val word: String,
    val numSyllables: Int,
)
