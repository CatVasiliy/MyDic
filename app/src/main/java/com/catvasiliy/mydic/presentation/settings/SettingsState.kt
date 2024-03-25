package com.catvasiliy.mydic.presentation.settings

import com.catvasiliy.mydic.presentation.model.preferences.translation_sending.UiTranslationSendingPreferences

data class SettingsState(
    val sendingPreferences: UiTranslationSendingPreferences = UiTranslationSendingPreferences.getDefault()
)