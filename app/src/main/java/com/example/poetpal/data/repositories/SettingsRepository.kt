package com.example.poetpal.data.repositories

import android.content.Context
import com.example.poetpal.data.database.SettingDao
import com.example.poetpal.data.database.asDbSetting
import com.example.poetpal.data.database.asDomainSetting
import com.example.poetpal.domain.Setting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SettingsRepository {
    fun getSettings(): Flow<Setting>

    suspend fun insertSettings(setting: Setting)

    suspend fun updateSettings(setting: Setting)
}

class LocalSettingRepository(private val settingDao: SettingDao, context: Context) :
    SettingsRepository {
    override fun getSettings(): Flow<Setting> {
        return settingDao.getSettings().map {
            it.asDomainSetting()
        }
    }

    override suspend fun insertSettings(setting: Setting) {
        settingDao.insert(setting.asDbSetting())
    }

    override suspend fun updateSettings(setting: Setting) {
        settingDao.update(setting.asDbSetting())
    }
}
