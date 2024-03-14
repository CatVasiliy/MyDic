package com.catvasiliy.mydic.presentation.model.translation

import com.catvasiliy.mydic.domain.model.translation.AlternativeTranslation
import com.catvasiliy.mydic.domain.model.translation.Definition
import com.catvasiliy.mydic.domain.model.translation.Example

data class UiTranslation(
    val id: Long,
    val sourceText: String,
    val translationText: String?,
    val sourceLanguage: UiExtendedLanguage?,
    val targetLanguage: UiLanguage,
    val sourceTransliteration: String?,
    val translatedAtMillis: Long,
    val alternativeTranslations: List<AlternativeTranslation>,
    val definitions: List<Definition>,
    val examples: List<Example>
) {
    val isMissingTranslation: Boolean
        get() = translationText == null

    val sourceLanguageCode: String?
        get() = when (sourceLanguage) {
            null -> null
            is UiExtendedLanguage.Unknown -> sourceLanguage.languageCode
            is UiExtendedLanguage.Known -> sourceLanguage.language.code
        }
}