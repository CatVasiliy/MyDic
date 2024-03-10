package com.catvasiliy.mydic.presentation.model.translation

sealed interface UiExtendedLanguage {
    data class Known(val language: UiLanguage, val isDetected: Boolean? = null) : UiExtendedLanguage
    data class Unknown(val languageCode: String) : UiExtendedLanguage
}
