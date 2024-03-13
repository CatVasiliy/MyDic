package com.catvasiliy.mydic.domain.model.preferences.translation_organizing.filtering

import com.catvasiliy.mydic.domain.model.translation.language.Language
import kotlinx.serialization.Serializable

@Serializable
sealed interface SourceLanguageFilteringInfo {
    @Serializable object LanguageAny : SourceLanguageFilteringInfo
    @Serializable object LanguageUnknown : SourceLanguageFilteringInfo
    @Serializable data class LanguageKnown(val language: Language) : SourceLanguageFilteringInfo
}