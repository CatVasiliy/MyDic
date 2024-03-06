package com.catvasiliy.mydic.presentation.model.translation

data class UiTranslationSourceLanguage(
    val language: UiSourceLanguage,
    val isDetected: Boolean?,
    val autoLanguageCode: String?
)
