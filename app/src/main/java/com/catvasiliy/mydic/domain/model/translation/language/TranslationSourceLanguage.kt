package com.catvasiliy.mydic.domain.model.translation.language

data class TranslationSourceLanguage(
    val language: SourceLanguage,
    val isDetected: Boolean?,
    val autoLanguageCode: String?
)