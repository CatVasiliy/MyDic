package com.catvasiliy.mydic.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.catvasiliy.mydic.data.local.database.model.CachedAlternativeTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedDefinition
import com.catvasiliy.mydic.data.local.database.model.CachedExample
import com.catvasiliy.mydic.data.local.database.model.CachedMissingTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedSynonym
import com.catvasiliy.mydic.data.local.database.model.CachedTranslation

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