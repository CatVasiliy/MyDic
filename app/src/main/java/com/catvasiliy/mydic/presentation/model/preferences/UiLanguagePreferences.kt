package com.catvasiliy.mydic.presentation.model.preferences

import com.catvasiliy.mydic.domain.model.preferences.LanguagePreferences
import com.catvasiliy.mydic.presentation.model.toUiLanguagePreferences
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage

data class UiLanguagePreferences(
    val defaultSourceLanguage: UiLanguage?,
    val defaultTargetLanguage: UiLanguage
) {
    companion object {
        fun getDefault(): UiLanguagePreferences =
            LanguagePreferences.getDefault().toUiLanguagePreferences()
    }
}
