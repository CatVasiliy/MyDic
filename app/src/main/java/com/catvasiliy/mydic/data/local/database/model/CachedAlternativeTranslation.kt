package com.catvasiliy.mydic.data.local.database.model

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "alternative_translation",
    foreignKeys = [
        ForeignKey(
            entity = CachedTranslation::class,
            parentColumns = ["id"],
            childColumns = ["translation_id"],
            onDelete = CASCADE
        )
    ]
)
data class CachedAlternativeTranslation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "translation_id")
    val translationId: Long,

    val translationText: String,
    val partOfSpeech: String
)

data class CachedAlternativeAggregate(
    @Embedded
    val alternativeTranslation: CachedAlternativeTranslation,

    @Relation(
        parentColumn = "id",
        entityColumn = "alternative_translation_id"
    )
    val synonyms: List<CachedSynonym>
)
