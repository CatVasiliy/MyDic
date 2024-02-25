package com.catvasiliy.mydic.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.catvasiliy.mydic.domain.model.translation.language.SourceLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TargetLanguage

@Entity(tableName = "missing_translation")
data class CachedMissingTranslation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val sourceText: String,
    val sourceLanguage: SourceLanguage,
    val targetLanguage: TargetLanguage,
    val translatedAtMillis: Long
)
