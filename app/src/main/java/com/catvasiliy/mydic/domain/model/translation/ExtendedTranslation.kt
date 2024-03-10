package com.catvasiliy.mydic.domain.model.translation

import com.catvasiliy.mydic.domain.model.translation.language.Language
import com.catvasiliy.mydic.domain.model.translation.language.ExtendedLanguage

data class ExtendedTranslation private constructor(
    override val id: Long,
    override val sourceText: String,
    val translationText: String,
    val sourceLanguage: ExtendedLanguage,
    override val targetLanguage: Language,
    val sourceTransliteration: String?,
    override val translatedAtMillis: Long,
    val alternativeTranslations: List<AlternativeTranslation> = emptyList(),
    val definitions: List<Definition> = emptyList(),
    val examples: List<Example> = emptyList()
) : Translation() {

    companion object {

        fun createSimpleTranslation(
            id: Long = 0,
            sourceText: String,
            translationText: String,
            sourceLanguage: ExtendedLanguage,
            targetLanguage: Language,
            translatedAtMillis: Long
        ): ExtendedTranslation {
            return ExtendedTranslation(
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
            sourceLanguage: ExtendedLanguage,
            targetLanguage: Language,
            sourceTransliteration: String?,
            translatedAtMillis: Long,
            alternativeTranslations: List<AlternativeTranslation>,
            definitions: List<Definition>,
            examples: List<Example>
        ): ExtendedTranslation {
            return ExtendedTranslation(
                id = id,
                sourceText = sourceText,
                translationText = translationText,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                sourceTransliteration = sourceTransliteration,
                translatedAtMillis = translatedAtMillis,
                alternativeTranslations = alternativeTranslations,
                definitions = definitions,
                examples = examples
            )
        }
    }
}