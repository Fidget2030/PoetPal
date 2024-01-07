package com.example.poetpal.ui.screens.writing

import androidx.compose.ui.text.AnnotatedString
import com.example.poetpal.ui.utils.aMeter
import com.example.poetpal.ui.utils.bMeter

data class WritingScreenState(
    val showTutorial: Boolean = true,
    val showSaveDialog: Boolean = false,
    val unknownWords: List<String> = listOf(),
    val alternatives: List<List<String>> = listOf(),
    val newUnknown: Boolean = false,
)

data class PoemState(
    val lines: List<String> = listOf("", "", "", "", ""),
    val syllables: List<List<List<String>>> = listOf(aMeter, aMeter, bMeter, bMeter, aMeter),
    val annotatedLines: List<AnnotatedString> =
        listOf(
            AnnotatedString(""),
            AnnotatedString(""),
            AnnotatedString(""),
            AnnotatedString(""),
            AnnotatedString(""),
        ),
    val author: String = "",
    val title: String = "",
    val type: String = "",
)
