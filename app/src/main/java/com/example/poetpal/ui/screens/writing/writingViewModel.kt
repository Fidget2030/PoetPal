package com.example.poetpal.ui.screens.writing

import android.util.Log
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
import com.example.poetpal.domain.Setting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WritingViewModel(
    private val settingRepository: SettingsRepository,
    private val poemRepository: PoemRepository,
    // private val wordsRepository: WordsRepository,
) : ViewModel() {
    private val _writeState = MutableStateFlow(WritingScreenState())
    val writeState: StateFlow<WritingScreenState> = _writeState.asStateFlow()
    private val _poemState = MutableStateFlow(PoemState())
    val poemState: StateFlow<PoemState> = _poemState.asStateFlow()

    init {
        Log.d("WritingVM", "init start")
        getSettings()
        Log.d("WritingVM", "init finish")
    }

    private fun getSettings() {
        viewModelScope.launch {
            Log.d("WritingVM", "fetching settings")
            try {
                val setting = settingRepository.getSettings().single()
                _writeState.update { it.copy(showTutorial = setting.limerickTutorial) }
            } catch (e: Exception) {
                Log.e("fetching settings", e.toString())
                settingRepository.updateSettings(Setting(true))
            }
        }
    }

    fun updateLine(
        line: String,
        lineNr: Int,
    ) {
        val newLines = poemState.value.lines.toMutableList()
        newLines[lineNr] = line
        _poemState.update { currentState -> currentState.copy(lines = newLines) }
    }

    fun setAuthor(author: String) {
        _poemState.update { it.copy(author = author) }
    }

    fun setTitle(title: String) {
        _poemState.update { it.copy(title = title) }
    }

    fun saveLimerick() {
        val title = poemState.value.title
        val text = poemState.value.lines.joinToString(separator = "\n")
        val author = poemState.value.author
        val type = "limerick"
        val poem = Poem(title, text, author, type)
        viewModelScope.launch {
            poemRepository.addPoem(poem)
        }
        _poemState.update {
            it.copy(
                lines = listOf("", "", "", "", ""),
                words = listOf(),
                author = "",
                title = "",
                type = "",
            )
        }
    }

    fun toggleSaveDialog() {
        val toggle = writeState.value.showSaveDialog
        _writeState.update { it.copy(showSaveDialog = !toggle) }
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
