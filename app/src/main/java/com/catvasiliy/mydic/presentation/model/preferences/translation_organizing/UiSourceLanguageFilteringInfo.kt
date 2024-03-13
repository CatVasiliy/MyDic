package com.catvasiliy.mydic.presentation.model.preferences.translation_organizing

import com.catvasiliy.mydic.presentation.model.translation.UiLanguage

sealed interface UiSourceLanguageFilteringInfo {
    data object LanguageAny : UiSourceLanguageFilteringInfo
    data object LanguageUnknown : UiSourceLanguageFilteringInfo
    data class LanguageKnown(val language: UiLanguage) : UiSourceLanguageFilteringInfo
}