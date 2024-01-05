package com.example.poetpal.ui.screens.writing

import com.example.poetpal.domain.Word

data class WritingState(
    val showTutorial: Boolean = true,
    val lines: List<String> = listOf("", "", "", "", ""),
    val words: List<List<Word>?> = listOf(null, null, null, null, null),
)
