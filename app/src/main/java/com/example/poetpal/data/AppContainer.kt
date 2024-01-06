package com.example.poetpal.data

import android.content.Context
import com.example.poetpal.data.database.PoetPalDb
import com.example.poetpal.network.NetworkConnectionInterceptor
import com.example.poetpal.network.mirriamWebsterApi.SyllableApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

private const val BASE_URL = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/"

interface AppContainer {
    val poemRepository: PoemRepository

    val wordsRepository: WordsRepository
    val settingsRepository: SettingsRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val networkCheck = NetworkConnectionInterceptor(context)
    private val client =
        OkHttpClient.Builder()
            .addInterceptor(networkCheck)
            .build()
    val json = Json { ignoreUnknownKeys = true }
    private val websterRetrofit =
        Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .client(client)
            .build()

    private val syllableService: SyllableApiService by lazy {
        websterRetrofit.create(SyllableApiService::class.java)
    }
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
    override val wordsRepository: WordsRepository by lazy {
        CachingWordsRepository(
            PoetPalDb.getDatabase(context = context).wordDao(),
            syllableService,
            context,
        )
    }
}
