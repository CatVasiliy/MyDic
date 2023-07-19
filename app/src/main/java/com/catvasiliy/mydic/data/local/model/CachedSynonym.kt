package com.catvasiliy.mydic.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "synonym",
    foreignKeys = [
        ForeignKey(
            entity = CachedAlternativeTranslation::class,
            parentColumns = ["id"],
            childColumns = ["alternative_translation_id"],
            onDelete = CASCADE
        )
    ]
)
data class CachedSynonym(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "alternative_translation_id")
    val alternativeTranslationId: Long = 0,

    val synonymText: String
)
