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
    val sourceTransliteration: String?,
    val translatedAtMillis: Long,
    val alternativeTranslations: List<AlternativeTranslation> = emptyList(),
    val definitions: List<Definition> = emptyList(),
    val examples: List<Example> = emptyList()
) {

    val isMissingTranslation: Boolean
        get() = translationText == null

    val sourceLanguageCode: String
        // Missing Translation only have sourceLanguage.language, other fields undefined (null)
        get() = if (isMissingTranslation) {
            sourceLanguage.language.code
        } else if (sourceLanguage.language == SourceLanguage.AUTO) {
            requireNotNull(sourceLanguage.autoLanguageCode)
        } else {
            sourceLanguage.language.code
        }

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
                sourceTransliteration = null,
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
                sourceTransliteration = null,
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
                sourceLanguage = TranslationSourceLanguage(
                    language = sourceLanguage,
                    isDetected = null,
                    autoLanguageCode = null
                ),
                targetLanguage = targetLanguage,
                sourceTransliteration = null,
                translatedAtMillis = Date().time
            )
        }
    }
}