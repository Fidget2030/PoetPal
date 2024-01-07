package com.example.poetpal.data

import android.content.Context
import com.example.poetpal.data.database.PoetPalDb
import com.example.poetpal.data.repositories.CachingWordsRepository
import com.example.poetpal.data.repositories.LocalPoemRepository
import com.example.poetpal.data.repositories.LocalSettingRepository
import com.example.poetpal.data.repositories.PoemRepository
import com.example.poetpal.data.repositories.SettingsRepository
import com.example.poetpal.data.repositories.WordsRepository
import com.example.poetpal.network.apiNinjaApi.ApiNinjaApiService
import com.example.poetpal.network.dataMuseApi.DataMuseApiService
import com.example.poetpal.network.interceptors.ApiNinjaHeaderInterceptor
import com.example.poetpal.network.interceptors.NetworkConnectionInterceptor
import com.example.poetpal.network.mirriamWebsterApi.WebsterApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

private const val WEBSTER_BASE_URL =
    "https://www.dictionaryapi.com/api/v3/references/collegiate/json/"
private const val APININJA_BASE_URL =
    "https://api.api-ninjas.com/v1/"
private const val DATAMUSE_BASE_URL =
    "https://api.datamuse.com/"

/**
 * Interface providing app-wide access to the repositories
 */
interface AppContainer {
    val poemRepository: PoemRepository

    val wordsRepository: WordsRepository
    val settingsRepository: SettingsRepository
}

/***
 * Default implementation of [AppContainer]
 * binds the created repositories to the fields defined in the interface
 *
 * @param context parameter which defines the context
 *  wherein the container is accessible
 *
 * @property networkCheck Interceptor which checks internet access
 *
 *  ‎
 *
 * **see also:** [NetworkConnectionInterceptor]
 *
 * @property [apiNinjaHeaderInterceptor] Interceptor which adds the ninja api key to the request header
 *
 *  ‎
 *
 * **see also:** [ApiNinjaHeaderInterceptor]
 *
 * @property [defaultClient] default http client used for websterApi
 *
 * and DatamuseApi
 *
 * ‎
 *
 * uses [networkCheck]
 *
 * @property[ninjaClient] http client used for apiNinja
 *
 * It passes the api key in the header instead of as a query
 *
 *  ‎
 *
 * uses [networkCheck], [apiNinjaHeaderInterceptor]
 *
 * @property [json] A customized Json instance that ignores unknown keys in json objects,
 *
 * instead of throwing an error.
 *
 *  ‎
 *
 * useful for APIs that return JSON objects with a large number of redundant fields
 *
 * @property [websterRetrofit] A retrofit instance to communicate with the webster Api
 * @property [apiNinjaRetrofit] A retrofit instance to communicate with apiNinjaApi
 * @property [dataMuseRetrofit] A retrofit instance to communicate with dataMuseApi
 *
 * @property [websterApiService] this apiServices is used to fetch the syllables from words.
 *
 *  ‎
 *
 *  **MAX 1 000 CALLS/DAY**
 *
 *  ‎
 *
 * **see also:** [Merriam-Webster's Collegiate® Dictionary](https://dictionaryapi.com/products/api-collegiate-dictionary)
 *
 * @property [apiNinjaApiService] this apiService is used to fetch rhymes,
 *
 * synonyms and antonyms
 *
 *  ‎
 *
 * **MAX 10 000 CALLS/MONTH**
 *
 *  ‎
 *
 * **see also:** [API Ninjas](https://api-ninjas.com/api)
 *
 * @property[dataMuseApiService] this apiService is used to count the syllables in a word,
 *
 * serving as a backup for words not found through [websterApiService]
 *
 * It has many other features
 *
 *  ‎
 *
 * **UNLIMITED CALLS, NO KEY**
 *
 *  ‎
 *
 * **see also:** [Datamuse API](https://www.datamuse.com/api/)
 */
class DefaultAppContainer(private val context: Context) : AppContainer {
    private val networkCheck = NetworkConnectionInterceptor(context)
    private val apiNinjaHeaderInterceptor = ApiNinjaHeaderInterceptor(context)

    private val defaultClient =
        OkHttpClient.Builder()
            .addInterceptor(networkCheck)
            .build()
    private val ninjaClient =
        OkHttpClient.Builder()
            .addInterceptor(networkCheck)
            .addInterceptor(apiNinjaHeaderInterceptor)
            .build()

    private val json = Json { ignoreUnknownKeys = true }

    private val websterRetrofit =
        Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(WEBSTER_BASE_URL)
            .client(defaultClient)
            .build()
    private val apiNinjaRetrofit =
        Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(APININJA_BASE_URL)
            .client(ninjaClient)
            .build()

    private val dataMuseRetrofit =
        Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(DATAMUSE_BASE_URL)
            .client(defaultClient)
            .build()

    private val websterApiService: WebsterApiService by lazy {
        websterRetrofit.create(WebsterApiService::class.java)
    }
    private val apiNinjaApiService: ApiNinjaApiService by lazy {
        apiNinjaRetrofit.create(ApiNinjaApiService::class.java)
    }
    private val dataMuseApiService: DataMuseApiService by lazy {
        dataMuseRetrofit.create(DataMuseApiService::class.java)
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
            websterApiService,
            apiNinjaApiService,
            dataMuseApiService,
            context,
        )
    }
}
