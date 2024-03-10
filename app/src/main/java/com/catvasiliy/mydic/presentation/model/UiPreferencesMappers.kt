package com.catvasiliy.mydic.presentation.model

import com.catvasiliy.mydic.domain.model.preferences.LanguagePreferences
import com.catvasiliy.mydic.presentation.model.preferences.UiLanguagePreferences

fun LanguagePreferences.toUiLanguagePreferences(): UiLanguagePreferences {
    return UiLanguagePreferences(
        defaultSourceLanguage = defaultSourceLanguage?.toUiLanguage(),
        defaultTargetLanguage = defaultTargetLanguage.toUiLanguageNotNull()
    )
}