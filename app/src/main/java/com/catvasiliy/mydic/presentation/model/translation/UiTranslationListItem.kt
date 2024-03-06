package com.catvasiliy.mydic.presentation.model.translation

data class UiTranslationListItem(
    val id: Long,
    val sourceText: String,
    val translationText: String?,
    val sourceLanguage: UiTranslationSourceLanguage,
    val targetLanguage: UiTargetLanguage,
    val translatedAtMillis: Long
) {
    val isMissingTranslation: Boolean
        get() = translationText == null
}