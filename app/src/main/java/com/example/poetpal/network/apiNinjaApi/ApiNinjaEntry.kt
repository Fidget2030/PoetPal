package com.example.poetpal.network.apiNinjaApi

import kotlinx.serialization.Serializable

@Serializable
data class ThesaurusEntry(
    val word: String,
    val synonyms: List<String>,
    val antonyms: List<String>,
)
