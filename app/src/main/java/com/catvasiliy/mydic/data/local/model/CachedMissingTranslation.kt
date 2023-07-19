package com.catvasiliy.mydic.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "missing_translation")
data class CachedMissingTranslation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val sourceText: String,
    val translatedAtMillis: Long
)
