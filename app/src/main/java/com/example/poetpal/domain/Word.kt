package com.example.poetpal.domain

data class Word(
    var word: String,
    var syllables: List<String>,
    var rhymes: List<String>,
    var related: List<String>,
)
