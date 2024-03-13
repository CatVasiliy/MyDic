package com.catvasiliy.mydic.domain.model.preferences.translation_organizing

import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.filtering.SourceLanguageFilteringInfo
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.filtering.TargetLanguageFilteringInfo
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.sorting.SortingOrder
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.sorting.TranslationSortingInfo
import kotlinx.serialization.Serializable

@Serializable
data class TranslationOrganizingPreferences(
    val sortingInfo: TranslationSortingInfo = TranslationSortingInfo.Date(SortingOrder.Descending),
    val sourceLanguageFilteringInfo: SourceLanguageFilteringInfo = SourceLanguageFilteringInfo.LanguageAny,
    val targetLanguageFilteringInfo: TargetLanguageFilteringInfo = TargetLanguageFilteringInfo.LanguageAny
)