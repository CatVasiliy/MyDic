package com.catvasiliy.mydic.presentation.model.preferences.translation_organizing

import com.catvasiliy.mydic.presentation.model.translation.UiLanguage

sealed interface UiTargetLanguageFilteringInfo {
    object LanguageAny : UiTargetLanguageFilteringInfo
    data class LanguageKnown (val language: UiLanguage) : UiTargetLanguageFilteringInfo
}