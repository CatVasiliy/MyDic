package com.catvasiliy.mydic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.catvasiliy.mydic.data.local.model.*

@Database(
    entities = [
        CachedTranslation::class,
        CachedAlternativeTranslation::class,
        CachedSynonym::class,
        CachedDefinition::class,
        CachedExample::class,
        CachedMissingTranslation::class
    ],
    version = 1
)
abstract class TranslationDatabase : RoomDatabase() {
    abstract fun translationDao(): TranslationDao
}