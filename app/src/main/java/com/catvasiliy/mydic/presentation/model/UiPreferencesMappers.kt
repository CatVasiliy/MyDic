package com.catvasiliy.mydic.presentation.model

import com.catvasiliy.mydic.domain.model.preferences.LanguagePreferences
import com.catvasiliy.mydic.domain.model.preferences.translation_sorting.SourceLanguageFilteringInfo
import com.catvasiliy.mydic.presentation.model.preferences.UiLanguagePreferences
import com.catvasiliy.mydic.presentation.model.preferences.UiSourceLanguageFilteringInfo

fun SourceLanguageFilteringInfo.toUiSourceLanguageFilteringInfo(): UiSourceLanguageFilteringInfo {
    return when (this) {
        is SourceLanguageFilteringInfo.LanguageAny ->
            UiSourceLanguageFilteringInfo.LanguageAny
        is SourceLanguageFilteringInfo.LanguageUnknown ->
            UiSourceLanguageFilteringInfo.LanguageUnknown
        is SourceLanguageFilteringInfo.LanguageKnown ->
            UiSourceLanguageFilteringInfo.LanguageKnown(language.toUiLanguageNotNull())
    }
}

fun UiSourceLanguageFilteringInfo.toSourceLanguageFilteringInfo(): SourceLanguageFilteringInfo {
    return when(this) {
        is UiSourceLanguageFilteringInfo.LanguageAny ->
            SourceLanguageFilteringInfo.LanguageAny
        is UiSourceLanguageFilteringInfo.LanguageUnknown ->
            SourceLanguageFilteringInfo.LanguageUnknown
        is UiSourceLanguageFilteringInfo.LanguageKnown ->
            SourceLanguageFilteringInfo.LanguageKnown(language.toLanguageNotNull())
    }
}

fun LanguagePreferences.toUiLanguagePreferences(): UiLanguagePreferences {
    return UiLanguagePreferences(
        defaultSourceLanguage = defaultSourceLanguage?.toUiLanguage(),
        defaultTargetLanguage = defaultTargetLanguage.toUiLanguageNotNull()
    )
}