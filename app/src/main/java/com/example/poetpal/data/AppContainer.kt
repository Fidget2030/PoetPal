package com.example.poetpal.data

import android.content.Context
import com.example.poetpal.data.database.PoetPalDb

interface AppContainer {
    val poemRepository: PoemRepository

    // val wordsRepository: WordsRepository
    val settingsRepository: SettingsRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    override val settingsRepository: SettingsRepository by lazy {
        LocalSettingRepository(
            PoetPalDb.getDatabase(context = context).settingDao(),
            context,
        )
    }

    override val poemRepository: PoemRepository by lazy {
        LocalPoemRepository(
            PoetPalDb.getDatabase(context = context).poemDao(),
            context,
        )
    }
}
