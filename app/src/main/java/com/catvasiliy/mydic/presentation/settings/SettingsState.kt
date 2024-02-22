package com.catvasiliy.mydic.presentation.settings

import com.catvasiliy.mydic.domain.model.preferences.TranslationSendingPreferences

data class SettingsState(
    val translationSendingPreferences: TranslationSendingPreferences = TranslationSendingPreferences()
)