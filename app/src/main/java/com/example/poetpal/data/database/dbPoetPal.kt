package com.example.poetpal.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.poetpal.domain.Setting

@Suppress("ktlint:standard:class-naming")
@Entity(tableName = "settings")
data class dbSetting(
    @PrimaryKey
    val name: String = "Settings",
    val limerickTutorial: Boolean = false,
)

fun dbSetting.asDomainSetting(): Setting {
    return Setting(this.limerickTutorial)
}

fun Setting.asDbSetting(): dbSetting {
    return dbSetting("Settings", this.limerickTutorial)
}
