package com.catvasiliy.mydic.presentation.model.translation

import com.catvasiliy.mydic.domain.model.translation.AlternativeTranslation
import com.catvasiliy.mydic.domain.model.translation.Definition
import com.catvasiliy.mydic.domain.model.translation.Example

data class UiTranslation(
    val id: Long,
    val sourceText: String,
    val translationText: String?,
    val sourceLanguage: UiTranslationSourceLanguage,
    val targetLanguage: UiTargetLanguage,
    val sourceTransliteration: String?,
    val translatedAtMillis: Long,
    val alternativeTranslations: List<AlternativeTranslation>,
    val definitions: List<Definition>,
    val examples: List<Example>
) {
    val isMissingTranslation: Boolean
        get() = translationText == null

    val sourceLanguageCode: String
        // Missing Translation only have sourceLanguage.language, other fields undefined (null)
        get() = if (isMissingTranslation) {
            sourceLanguage.language.code
        } else if (sourceLanguage.language == UiSourceLanguage.AUTO) {
            requireNotNull(sourceLanguage.autoLanguageCode)
        } else {
            sourceLanguage.language.code
        }
}