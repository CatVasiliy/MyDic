package com.catvasiliy.mydic.presentation.translations_list

import com.catvasiliy.mydic.domain.model.preferences.translation_sorting.TranslationSortingInfo
import com.catvasiliy.mydic.presentation.model.translation.SourceLanguageFilterInfo
import com.catvasiliy.mydic.presentation.model.translation.UiTranslation
import com.catvasiliy.mydic.presentation.model.translation.UiTranslationListItem

data class TranslationsListState(
    val translations: List<UiTranslationListItem> = emptyList(),
    val sortingInfo: TranslationSortingInfo? = null,
    val filterInfo: SourceLanguageFilterInfo? = null,
    val searchQuery: String = "",
    val lastDeletedTranslation: UiTranslation? = null
)