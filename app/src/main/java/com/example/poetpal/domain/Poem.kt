package com.example.poetpal.domain

data class PoemMeta(
    var title: String,
    var author: String,
    var type: String,
)

data class Poem(
    var title: String,
    var text: String,
    var author: String,
    var type: String?,
)
