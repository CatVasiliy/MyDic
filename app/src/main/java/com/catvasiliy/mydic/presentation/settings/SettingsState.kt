package com.catvasiliy.mydic.presentation.settings

import com.catvasiliy.mydic.domain.model.settings.TranslationSendingPreferences

data class SettingsState(
    val sendTranslationPreferences: TranslationSendingPreferences = TranslationSendingPreferences()
)