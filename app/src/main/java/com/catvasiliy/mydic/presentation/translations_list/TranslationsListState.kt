package com.catvasiliy.mydic.presentation.translations_list

import com.catvasiliy.mydic.presentation.model.translation.SourceLanguageFilterInfo
import com.catvasiliy.mydic.presentation.model.translation.UiTranslation
import com.catvasiliy.mydic.presentation.model.translation.UiTranslationListItem
import com.catvasiliy.mydic.presentation.util.SortType
import com.catvasiliy.mydic.presentation.util.TranslationSort

data class TranslationsListState(
    val translations: List<UiTranslationListItem> = emptyList(),
    val sortInfo: TranslationSort = TranslationSort.Date(SortType.Descending),
    val filterInfo: SourceLanguageFilterInfo? = null,
    val searchQuery: String = "",
    val lastDeletedTranslation: UiTranslation? = null
)