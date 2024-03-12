package com.catvasiliy.mydic.presentation.model.preferences

import com.catvasiliy.mydic.presentation.model.translation.UiLanguage

sealed interface UiSourceLanguageFilteringInfo {
    object LanguageAny : UiSourceLanguageFilteringInfo
    object LanguageUnknown : UiSourceLanguageFilteringInfo
    data class LanguageKnown(val language: UiLanguage) : UiSourceLanguageFilteringInfo
}