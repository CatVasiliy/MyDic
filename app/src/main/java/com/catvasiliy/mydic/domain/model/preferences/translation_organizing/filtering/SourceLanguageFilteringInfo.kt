package com.catvasiliy.mydic.domain.model.preferences.translation_organizing.filtering

import com.catvasiliy.mydic.domain.model.translation.language.Language
import kotlinx.serialization.Serializable

@Serializable
sealed interface SourceLanguageFilteringInfo {
    @Serializable data object LanguageAny : SourceLanguageFilteringInfo
    @Serializable data object LanguageUnknown : SourceLanguageFilteringInfo
    @Serializable data class LanguageKnown(val language: Language) : SourceLanguageFilteringInfo
}