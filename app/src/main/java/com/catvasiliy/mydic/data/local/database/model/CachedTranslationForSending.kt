package com.catvasiliy.mydic.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translation_for_sending")
data class CachedTranslationForSending(
    @PrimaryKey(autoGenerate = false)
    val id: Long
)
