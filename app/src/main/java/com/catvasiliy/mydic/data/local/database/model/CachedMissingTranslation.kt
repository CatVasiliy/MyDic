package com.catvasiliy.mydic.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.catvasiliy.mydic.domain.model.translation.language.Language

@Entity(tableName = "missing_translation")
data class CachedMissingTranslation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val sourceText: String,
    val sourceLanguage: Language?,
    val targetLanguage: Language,
    val translatedAtMillis: Long
)
