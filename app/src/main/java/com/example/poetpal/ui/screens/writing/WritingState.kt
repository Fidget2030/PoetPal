package com.example.poetpal.ui.screens.writing

import com.example.poetpal.domain.Word

data class WritingScreenState(
    val showTutorial: Boolean = true,
    val showSaveDialog: Boolean = false,
)

data class PoemState(
    val lines: List<String> = listOf("", "", "", "", ""),
    val words: List<List<Word>> = listOf(),
    val author: String = "",
    val title: String = "",
    val type: String = "",
)
