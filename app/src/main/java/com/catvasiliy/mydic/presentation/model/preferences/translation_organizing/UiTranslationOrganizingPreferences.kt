package com.catvasiliy.mydic.presentation.model.preferences.translation_organizing

import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.sorting.SortingOrder
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.sorting.TranslationSortingInfo

data class UiTranslationOrganizingPreferences(
    val sortingInfo: TranslationSortingInfo = TranslationSortingInfo.Date(SortingOrder.Descending),
    val sourceLanguageFilteringInfo: UiSourceLanguageFilteringInfo = UiSourceLanguageFilteringInfo.LanguageAny,
    val targetLanguageFilteringInfo: UiTargetLanguageFilteringInfo = UiTargetLanguageFilteringInfo.LanguageAny
)
