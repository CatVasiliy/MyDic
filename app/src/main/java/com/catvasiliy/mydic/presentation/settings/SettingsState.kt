package com.catvasiliy.mydic.presentation.settings

import com.catvasiliy.mydic.domain.model.settings.SendTranslationPreferences

data class SettingsState(
    val sendTranslationPreferences: SendTranslationPreferences = SendTranslationPreferences()
)