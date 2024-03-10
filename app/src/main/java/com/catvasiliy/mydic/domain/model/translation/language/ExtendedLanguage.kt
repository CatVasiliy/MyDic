package com.catvasiliy.mydic.domain.model.translation.language

sealed interface ExtendedLanguage {
    data class Known(val language: Language, val isDetected: Boolean = false) : ExtendedLanguage
    data class Unknown(val languageCode: String) : ExtendedLanguage
}