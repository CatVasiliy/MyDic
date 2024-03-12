package com.catvasiliy.mydic.presentation.translations_list

import com.catvasiliy.mydic.domain.model.preferences.translation_sorting.SortingOrder
import com.catvasiliy.mydic.domain.model.preferences.translation_sorting.TranslationSortingInfo
import com.catvasiliy.mydic.presentation.model.preferences.UiSourceLanguageFilteringInfo
import com.catvasiliy.mydic.presentation.model.translation.UiTranslation
import com.catvasiliy.mydic.presentation.model.translation.UiTranslationListItem

data class TranslationsListState(
    val translations: List<UiTranslationListItem> = emptyList(),
    val sortingInfo: TranslationSortingInfo = TranslationSortingInfo.Date(SortingOrder.Descending),
    val filterInfo: UiSourceLanguageFilteringInfo = UiSourceLanguageFilteringInfo.LanguageAny,
    val searchQuery: String = "",
    val lastDeletedTranslation: UiTranslation? = null
)