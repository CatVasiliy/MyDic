package com.catvasiliy.mydic.domain.model.translation

import com.catvasiliy.mydic.domain.model.translation.language.SourceLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TargetLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TranslationSourceLanguage
import java.util.Date

data class Translation private constructor(
    val id: Long = 0,
    val sourceText: String,
    val translationText: String?,
    val sourceLanguage: TranslationSourceLanguage,
    val targetLanguage: TargetLanguage,
    val sourceTransliteration: String? = null,
    val translatedAtMillis: Long,
    val alternativeTranslations: List<AlternativeTranslation> = emptyList(),
    val definitions: List<Definition> = emptyList(),
    val examples: List<Example> = emptyList()
) {

    val isMissingTranslation: Boolean
        get() = translationText == null

    companion object {

        fun createSimpleTranslation(
            id: Long = 0,
            sourceText: String,
            translationText: String,
            sourceLanguage: TranslationSourceLanguage,
            targetLanguage: TargetLanguage,
            translatedAtMillis: Long
        ): Translation {
            return Translation(
                id = id,
                sourceText = sourceText,
                translationText = translationText,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                translatedAtMillis = translatedAtMillis
            )
        }

        fun createExtendedTranslation(
            id: Long = 0,
            sourceText: String,
            translationText: String,
            sourceLanguage: TranslationSourceLanguage,
            targetLanguage: TargetLanguage,
            sourceTransliteration: String?,
            translatedAtMillis: Long,
            alternativeTranslations: List<AlternativeTranslation>,
            definitions: List<Definition>,
            examples: List<Example>
        ): Translation {
            return Translation(
                id = id,
                sourceText = sourceText,
                translationText = translationText,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                sourceTransliteration = sourceTransliteration,
                translatedAtMillis = translatedAtMillis,
                alternativeTranslations = alternativeTranslations,
                definitions =definitions,
                examples = examples
            )
        }

        fun createMissingTranslation(
            id: Long = 0,
            sourceText: String,
            sourceLanguage: TranslationSourceLanguage,
            targetLanguage: TargetLanguage,
            translatedAtMillis: Long
        ): Translation {
            return Translation(
                id = id,
                sourceText = sourceText,
                translationText = null,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                translatedAtMillis = translatedAtMillis
            )
        }

        fun createMissingTranslation(
            id: Long = 0,
            sourceText: String,
            sourceLanguage: SourceLanguage,
            targetLanguage: TargetLanguage,
        ): Translation {
            return Translation(
                id = id,
                sourceText = sourceText,
                translationText = null,
                sourceLanguage = TranslationSourceLanguage(sourceLanguage),
                targetLanguage = targetLanguage,
                translatedAtMillis = Date().time
            )
        }
    }
}