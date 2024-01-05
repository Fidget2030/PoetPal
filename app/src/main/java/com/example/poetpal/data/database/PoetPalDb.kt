package com.example.poetpal.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [dbSetting::class, dbPoem::class], version = 1, exportSchema = false)
abstract class PoetPalDb : RoomDatabase() {
    abstract fun settingDao(): SettingDao
    abstract fun poemDao(): PoemDao

    companion object {
        @Suppress("ktlint:standard:property-naming")
        @Volatile
        private var Instance: PoetPalDb? = null

        fun getDatabase(context: Context): PoetPalDb {
            return Instance
                ?: synchronized(this) {
                    Room.databaseBuilder(context, PoetPalDb::class.java, "PoetPal_database")
                        .fallbackToDestructiveMigration()
                        .build()
                        .also { Instance = it }
                }
        }
    }
}
