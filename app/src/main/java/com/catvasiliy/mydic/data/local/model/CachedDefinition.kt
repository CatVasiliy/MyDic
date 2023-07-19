package com.catvasiliy.mydic.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "definition",
    foreignKeys = [
        ForeignKey(
            entity = CachedTranslation::class,
            parentColumns = ["id"],
            childColumns = ["translation_id"],
            onDelete = CASCADE
        )
    ]
)
data class CachedDefinition(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "translation_id")
    val translationId: Long,

    val definitionText: String,
    val partOfSpeech: String,
    val exampleText: String
)
