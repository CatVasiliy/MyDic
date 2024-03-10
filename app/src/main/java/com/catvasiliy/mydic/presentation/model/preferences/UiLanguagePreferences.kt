package com.catvasiliy.mydic.presentation.model.preferences

import com.catvasiliy.mydic.presentation.model.translation.UiLanguage

data class UiLanguagePreferences(
    val defaultSourceLanguage: UiLanguage? = null,
    val defaultTargetLanguage: UiLanguage = UiLanguage.ENGLISH
)
