package com.catvasiliy.mydic.presentation.model.preferences

import com.catvasiliy.mydic.presentation.model.translation.UiSourceLanguage
import com.catvasiliy.mydic.presentation.model.translation.UiTargetLanguage

data class UiLanguagePreferences(
    val defaultSourceLanguage: UiSourceLanguage = UiSourceLanguage.AUTO,
    val defaultTargetLanguage: UiTargetLanguage = UiTargetLanguage.ENGLISH
)
