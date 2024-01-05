package com.example.poetpal.domain

data class Word(
    var word: String,
    var syllables: List<String>,
    var normallyStressed: List<Int>,
    var rhymes: List<Word>,
)
