package com.example.poetpal.ui.screens.writing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.poetpal.PoetPalApplication
import com.example.poetpal.data.PoemRepository
import com.example.poetpal.data.SettingsRepository
import com.example.poetpal.domain.Poem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WritingViewModel(
    private val settingRepository: SettingsRepository,
    private val poemRepository: PoemRepository,
    // private val wordsRepository: WordsRepository,
) : ViewModel() {
    private val _writeState = MutableStateFlow(WritingState())
    val writeState: StateFlow<WritingState> = _writeState.asStateFlow()

    fun updateLine(
        line: String,
        lineNr: Int,
    ) {
        val newLines = writeState.value.lines.toMutableList()
        newLines[lineNr] = line
        _writeState.update { currentState -> currentState.copy(lines = newLines) }
    }

    fun saveLimerick() {
        val text = writeState.value.lines.joinToString(separator = "\n")
        val author = "anonymous"
        val type = "limerick"
        val title = "test"
        val poem = Poem(title, text, author, type)
        viewModelScope.launch {
            poemRepository.addPoem(poem)
        }
    }

    companion object {
        @Suppress("ktlint:standard:property-naming")
        private var Instance: WritingViewModel? = null
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    if (Instance == null) {
                        val application = (this[APPLICATION_KEY] as PoetPalApplication)
                        val settingRepository = application.container.settingsRepository
                        val poemRepository = application.container.poemRepository
                        Instance =
                            WritingViewModel(
                                settingRepository = settingRepository,
                                poemRepository = poemRepository,
                            )
                    }
                    Instance!!
                }
            }
    }
}
