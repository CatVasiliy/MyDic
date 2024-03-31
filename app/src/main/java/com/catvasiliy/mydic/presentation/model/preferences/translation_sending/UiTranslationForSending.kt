package com.catvasiliy.mydic.presentation.model.preferences.translation_sending

import com.catvasiliy.mydic.presentation.model.translation.UiLanguage

data class UiTranslationForSending(
    val id: Long,
    val sourceText: String,
    val translationText: String,
    val sourceLanguage: UiLanguage?
)
