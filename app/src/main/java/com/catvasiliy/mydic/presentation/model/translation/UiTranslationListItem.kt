package com.catvasiliy.mydic.presentation.model.translation

data class UiTranslationListItem(
    val id: Long,
    val sourceText: String,
    val translationText: String?,
    val sourceLanguage: UiExtendedLanguage?,
    val targetLanguage: UiLanguage,
    val translatedAtMillis: Long
) {
    val isMissingTranslation: Boolean
        get() = translationText == null
}