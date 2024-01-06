package com.example.poetpal.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.poetpal.domain.Poem
import com.example.poetpal.domain.Setting
import com.example.poetpal.domain.Word

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

@Suppress("ktlint:standard:class-naming")
@Entity(tableName = "poems")
data class dbPoem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val text: String,
    val author: String,
    val type: String?,
)

fun dbPoem.asDomainPoem(): Poem {
    return Poem(
        this.title,
        this.text,
        this.author,
        this.type,
    )
}

fun Poem.asDbPoem(): dbPoem {
    return dbPoem(
        id = 0,
        title = this.title,
        text = this.text,
        author = this.author,
        type = this.type,
    )
}

@Suppress("ktlint:standard:class-naming")
@Entity(tableName = "words")
data class dbWord(
    @PrimaryKey
    val word: String,
    val syllables: String,
    val rhymes: String,
    val related: String,
)

fun dbWord.asDomainWord(): Word {
    val syllableList = syllables.split(",")
    val rhymeList = rhymes.split(",")
    val relatedList = related.split(",")
    return Word(word, syllableList, rhymeList, relatedList)
}

fun Word.asdbWord(): dbWord {
    val syllableString = syllables.joinToString(",")
    val rhymeString = rhymes.joinToString(",")
    val relatedString = related.joinToString(",")
    return dbWord(word, syllableString, rhymeString, relatedString)
}
