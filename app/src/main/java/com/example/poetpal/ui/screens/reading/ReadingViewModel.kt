package com.example.poetpal.ui.screens.reading

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.poetpal.PoetPalApplication
import com.example.poetpal.data.repositories.PoemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * viewModel for the reading screen
 * @param poemRepository see [PoemRepository]
 *
 * @property _selectedPoemState keeps track of the currently selected poem
 * @property selectedPoemState see [_selectedPoemState]
 * @property metaState Hot Flow keeping track of poems in the roomDb
 */
class ReadingViewModel(
    private val poemRepository: PoemRepository,
) : ViewModel() {
    private val _selectedPoemState = MutableStateFlow(SelectedState())
    val selectedPoemState: StateFlow<SelectedState> = _selectedPoemState.asStateFlow()
    lateinit var metaState: StateFlow<MetaState>

    init {
        getMetas()
    }

    private fun getMetas() {
        viewModelScope.launch {
            try {
                metaState =
                    poemRepository.getPoemMetas().map { MetaState(it) }
                        .stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(5_000L),
                            initialValue = MetaState(),
                        )
            } catch (e: Exception) {
                Log.e("getting metas", e.toString())
            }
        }
    }

    fun getPoem(title: String) {
        Log.i("readingVM", "start fetch selected poem")
        viewModelScope.launch {
            val poem = poemRepository.getPoemByTitle(title).stateIn(scope = viewModelScope).value
            _selectedPoemState.update { it.copy(selectedPoem = poem) }
            Log.d("getPoem", "got poem value")
        }
    }

    companion object {
        @Suppress("ktlint:standard:property-naming")
        private var Instance: ReadingViewModel? = null
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    if (Instance == null) {
                        val application = (this[APPLICATION_KEY] as PoetPalApplication)
                        val poemRepository = application.container.poemRepository
                        Instance =
                            ReadingViewModel(poemRepository = poemRepository)
                    }
                    Instance!!
                }
            }
    }
}
