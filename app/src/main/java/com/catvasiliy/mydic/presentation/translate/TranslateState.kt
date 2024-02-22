package com.catvasiliy.mydic.presentation.translate

import com.catvasiliy.mydic.domain.model.preferences.LanguagePreferences

data class TranslateState(
    val languagePreferences: LanguagePreferences = LanguagePreferences()
)
