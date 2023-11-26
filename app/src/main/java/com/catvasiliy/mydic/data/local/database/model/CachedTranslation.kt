package com.catvasiliy.mydic.data.local.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "translation")
data class CachedTranslation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val translationText: String,
    val sourceText: String,
    val sourceTransliteration: String,
    val translatedAtMillis: Long
)

data class CachedTranslationAggregate(
    @Embedded
    val translation: CachedTranslation,

    @Relation(
        parentColumn = "id",
        entityColumn = "translation_id",
        entity = CachedAlternativeTranslation::class
    )
    val alternativeTranslations: List<CachedAlternativeAggregate>,

    @Relation(
        parentColumn = "id",
        entityColumn = "translation_id"
    )
    val definitions: List<CachedDefinition>,

    @Relation(
        parentColumn = "id",
        entityColumn = "translation_id"
    )
    val examples: List<CachedExample>
)
