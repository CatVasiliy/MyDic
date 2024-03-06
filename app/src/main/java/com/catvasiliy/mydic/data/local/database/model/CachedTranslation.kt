package com.catvasiliy.mydic.data.local.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.catvasiliy.mydic.domain.model.translation.language.SourceLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TargetLanguage

@Entity(tableName = "translation")
data class CachedTranslation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val sourceText: String,
    val translationText: String,
    val sourceLanguage: SourceLanguage,
    val isLanguageDetected: Boolean,
    val autoSourceLanguageCode: String?,
    val targetLanguage: TargetLanguage,
    val sourceTransliteration: String?,
    val translatedAtMillis: Long
)

data class CachedTranslationAggregate(
    @Embedded
    val baseTranslation: CachedTranslation,

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
