package com.example.poetpal.ui.screens.writing

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.poetpal.PoetPalApplication
import com.example.poetpal.data.repositories.PoemRepository
import com.example.poetpal.data.repositories.SettingsRepository
import com.example.poetpal.data.repositories.WordsRepository
import com.example.poetpal.domain.Poem
import com.example.poetpal.domain.Setting
import com.example.poetpal.domain.Word
import com.example.poetpal.ui.utils.aMeter
import com.example.poetpal.ui.utils.bMeter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.ceil

/**
 * ViewModel for the writing screen
 *
 *
 * @property [settingRepository] [SettingsRepository]
 * @property [poemRepository] [PoemRepository]
 * @property [wordsRepository] [WordsRepository]
 * @property[debounceJob]
 * variable used to hold a debounced function
 *
 * @property[writeState]
 * keep track of triggers and booleans dealing with the layout of the page
 * @property [_writeState] see [writeState]
 * @property[poemState]keeps track of the poem being written
 * @property[_poemState] see [poemState]
 *
 * @constructor binds all repositories,
 *
 * initializes the model by fetching any settings stored in roomDb
 */
class WritingViewModel(
    private val settingRepository: SettingsRepository,
    private val poemRepository: PoemRepository,
    private val wordsRepository: WordsRepository,
) : ViewModel() {
    private var debounceJob: Job? = null

    private val _writeState = MutableStateFlow(WritingScreenState())
    val writeState: StateFlow<WritingScreenState> = _writeState.asStateFlow()
    private val _poemState = MutableStateFlow(PoemState())
    val poemState: StateFlow<PoemState> = _poemState.asStateFlow()

    init {
        getSettings()
        //  viewModelScope.launch { wordsRepository.nukeWords() }
    }

    /**
     * Fetches any settings from the roomDb if they exist.
     *
     * Initializes them if they don't.
     *
     * Runs in viewModelScope
     */
    private fun getSettings() {
        viewModelScope.launch {
            Log.d("WritingVM", "fetching settings")
            try {
                val setting = settingRepository.getSettings().single()
                _writeState.update { it.copy(showTutorial = setting.limerickTutorial) }
            } catch (e: Exception) {
                Log.e("fetching settings", e.toString())
                settingRepository.insertSettings(Setting(true))
            }
        }
    }

    /**
     * Updates [PoemState.lines] when new text is added to the poem
     * @param line updated line
     * @param lineNr index in [PoemState.lines]
     */
    fun updateLine(
        line: String,
        lineNr: Int,
    ) {
        updateSyllablesDebounced(line, lineNr)
        val newLines = poemState.value.lines.toMutableList()
        newLines[lineNr] = line

        _poemState.update { currentState -> currentState.copy(lines = newLines) }
    }

    /**
     * Updates the [PoemState.syllables] when new lines are added
     *
     * This function is debounced to reduce load on the database and api calls
     * @param line: the line that has changed
     * @param lineNr: index in [PoemState.syllables]
     */
    private fun updateSyllablesDebounced(
        line: String,
        lineNr: Int,
    ) {
        debounceJob?.cancel()
        debounceJob =
            viewModelScope.launch(Dispatchers.IO) {
                delay(500)

                val syllables = getSyllables(line)
                val lineLength = syllables.flatten().size
                Log.d("DEBOUNCE_DEBUG", "$lineLength")
                val wordList = poemState.value.syllables.toMutableList()
                val padding = addPadding(lineLength, lineNr)
                val paddedSyllables = syllables + padding
                wordList[lineNr] = paddedSyllables

                _poemState.update {
                    it.copy(syllables = wordList)
                }
                annotateSyllables()
            }
    }

    private fun addPadding(
        lineLength: Int,
        lineNr: Int,
    ): List<List<String>> {
        return if (lineNr in listOf(0, 1, 4) && lineLength < 8) {
            aMeter.slice(lineLength + 1 until aMeter.size - 1)
        } else if (lineNr in listOf(2, 3) && lineLength < 5) {
            bMeter.slice(lineLength + 1 until bMeter.size - 1)
        } else {
            listOf(listOf())
        }
    }

    /**
     * Called when the syllables need to be updated
     *
     * Breaks a line of words up into its syllables
     * @param line: the line to break up
     */
    private suspend fun getSyllables(line: String): List<List<String>> {
        var strings = line.split(" ")
        strings = strings.filter { it.isNotBlank() } // remove extra spaces
        val words =
            strings.map { item ->
                run {
                    try {
                        wordsRepository.getWord(item)
                    } catch (e: Exception) {
                        if (!writeState.value.unknownWords.contains(item)) {
                            saveAlternatives(item)
                        }
                        syllableSplitter(item)
                    }
                }
            }
        return words
            .map { it.syllables }
    }

    /**
     * Called when a word is not found through the webster api or roomDb
     *
     * gets the number of syllables through dataMuse Api and splits the word accordingly
     * into approximate correct syllables
     */
    private suspend fun syllableSplitter(item: String): Word {
        val num = wordsRepository.getNrOfSyllables(item).toDouble()
        val size = ceil(item.length.div(num)).toInt()
        val syllables = item.chunked(size)
        return Word(item, syllables, listOf(), listOf(), listOf())
    }

    /**
     * Called when a word is not found through the webster api or roomDb
     *
     * Saves the alternatives provided by the webster api
     * and the original word in [writeState],
     * also sets the newUnknown flag
     * @param word: the unknown word
     */
    private suspend fun saveAlternatives(word: String) {
        clearUnknownAndAlts()
        val alternatives =
            writeState.value.alternatives.toMutableList()
        val unknowns = writeState.value.unknownWords.toMutableList()
        val alternates = wordsRepository.getAlternatives(word).take(3)
        alternatives.add(alternates)
        unknowns.add(word)
        _writeState.update {
            it.copy(alternatives = alternatives, unknownWords = unknowns, newUnknown = true)
        }
    }

    /**
     * clears out unknown words that are no longer being used from [writeState]
     */
    private fun clearUnknownAndAlts() {
        val lines = poemState.value.lines.joinToString(" ")
        val unknowns = writeState.value.unknownWords.toMutableList()
        val alternatives = writeState.value.alternatives.toMutableList()
        unknowns.forEachIndexed { i, word ->
            run {
                if (!lines.contains(word, true)) {
                    unknowns.removeAt(i)
                    alternatives.removeAt(i)
                }
            }
        }
        _writeState.update { it.copy(unknownWords = unknowns, alternatives = alternatives) }
    }

    /**
     * annotates the syllables to show meter
     * line 1,2,5 8-11 syllables
     * line 3,4: 5-7 syllables
     */

    private fun annotateSyllables() { // this one is a beauty, if I may say so myself :)
        val syllables = poemState.value.syllables
        val annotatedStrings: MutableList<AnnotatedString> = mutableListOf()
        val lineLengths = syllables.map { it.flatten().size }
        val syllableLists =
            syllables.map { line ->
                line.map { syllable -> syllable.joinToString(",*,") }.joinToString(", ,").split(",")
            }
        syllableLists.forEachIndexed {
                lineNr,
                line,
            ->
            annotatedStrings.add(
                buildAnnotatedString {
                    if (lineNr in listOf(0, 1, 4)) {
                        line
                            .forEachIndexed { i, symbol ->
                                run {
                                    val maxLength = 20
                                    if ((
                                            lineLengths[lineNr] > 9 && i in
                                                listOf(
                                                    4,
                                                    10,
                                                    16,
                                                )
                                        ) || (
                                            lineLengths[lineNr] < 10 && i in
                                                listOf(
                                                    2,
                                                    8,
                                                    14,
                                                )
                                        )
                                    ) {
                                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append(
                                                symbol,
                                            )
                                        }
                                    } else if (i > maxLength) {
                                        withStyle(SpanStyle(color = Color.Red)) {
                                            append(
                                                symbol,
                                            )
                                        }
                                    } else {
                                        append(symbol)
                                    }
                                }
                            }
                    } else {
                        line.forEachIndexed { i, symbol ->
                            run {
                                val maxLength = 12
                                if ((
                                        lineLengths[lineNr] > 6 && i in
                                            listOf(
                                                4,
                                                10,
                                            )
                                    ) || (lineLengths[lineNr] < 7 && i in listOf(2, 8))
                                ) {
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(
                                            symbol,
                                        )
                                    }
                                } else if (i > maxLength) {
                                    withStyle(SpanStyle(color = Color.Red)) {
                                        append(
                                            symbol,
                                        )
                                    }
                                } else {
                                    append(symbol)
                                }
                            }
                        }
                    }
                },
            )
        }
        _poemState.update { it.copy(annotatedLines = annotatedStrings) }
    }

    /**
     * builds the message informing a unknown word was detected, and proposing alternatives.
     *
     *see [saveAlternatives]
     */
    fun snackBarMessage(): String {
        // TODO: create an "addToDictionary" dialog and add action button to snackBar
        val sb = StringBuilder()

        val unknowns = writeState.value.unknownWords
        val alts = writeState.value.alternatives
        unknowns.forEachIndexed { i, word -> sb.append("$word is unknown.\nDid you mean: ${alts[i]} \n") }
        return sb.toString()
    }

    /**
     * sets the author of the poem
     * @param author: Author of the poem
     */
    fun setAuthor(author: String) {
        _poemState.update { it.copy(author = author) }
    }

    /**
     * Sets the title of the poem
     * @param title: Title of the poem
     */
    fun setTitle(title: String) {
        _poemState.update { it.copy(title = title) }
    }

    /**
     * saves the written poem as a limerick to roomDb
     */
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
                syllables = listOf(),
                annotatedLines =
                    listOf(
                        AnnotatedString(""),
                        AnnotatedString(""),
                        AnnotatedString(""),
                        AnnotatedString(""),
                        AnnotatedString(""),
                    ),
                author = "",
                title = "",
                type = "",
            )
        }
        _writeState.update {
            it.copy(
                unknownWords = listOf(),
                alternatives = listOf(),
            )
        }
    }

    /**
     * shows/hides the saving dialog
     */
    fun toggleSaveDialog() {
        val toggle = writeState.value.showSaveDialog
        _writeState.update { it.copy(showSaveDialog = !toggle) }
    }

    /**
     * shows/hides the snackBar
     */
    fun toggleSnackBar() {
        val toggle = writeState.value.newUnknown
        _writeState.update { it.copy(newUnknown = !toggle) }
    }

    /**
     * companion object holding the factory to initialize the viewmodel
     * it fetches the repositories bound to the applicationcontainer via the context,
     * and passes them to the viewmodel
     */
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
                        val wordsRepository = application.container.wordsRepository
                        Instance =
                            WritingViewModel(
                                settingRepository = settingRepository,
                                poemRepository = poemRepository,
                                wordsRepository = wordsRepository,
                            )
                    }
                    Instance!!
                }
            }
    }
}
