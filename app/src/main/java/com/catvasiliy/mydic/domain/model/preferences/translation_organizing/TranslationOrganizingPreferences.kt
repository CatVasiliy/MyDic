package com.catvasiliy.mydic.domain.model.preferences.translation_organizing

import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.filtering.SourceLanguageFilteringInfo
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.filtering.TargetLanguageFilteringInfo
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.sorting.SortingOrder
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.sorting.TranslationSortingInfo
import kotlinx.serialization.Serializable

@Serializable
data class TranslationOrganizingPreferences(
    val sortingInfo: TranslationSortingInfo,
    val sourceLanguageFilteringInfo: SourceLanguageFilteringInfo,
    val targetLanguageFilteringInfo: TargetLanguageFilteringInfo
) {

    val isDefault: Boolean
        get() = this == getDefault()

    companion object {
        fun getDefault(): TranslationOrganizingPreferences =
            TranslationOrganizingPreferences(
                sortingInfo = TranslationSortingInfo.Date(SortingOrder.Descending),
                sourceLanguageFilteringInfo = SourceLanguageFilteringInfo.LanguageAny,
                targetLanguageFilteringInfo = TargetLanguageFilteringInfo.LanguageAny
            )
    }
}