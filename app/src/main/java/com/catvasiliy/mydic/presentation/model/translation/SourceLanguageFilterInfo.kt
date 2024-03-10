package com.catvasiliy.mydic.presentation.model.translation

sealed interface SourceLanguageFilterInfo {
    class LanguageKnown(val language: UiLanguage) : SourceLanguageFilterInfo
    object LanguageUnknown : SourceLanguageFilterInfo
}