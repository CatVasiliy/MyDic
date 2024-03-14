package com.catvasiliy.mydic.presentation.settings

import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationSendingPreferences

data class SettingsState(
    val sendingPreferences: TranslationSendingPreferences = TranslationSendingPreferences.getDefault()
)