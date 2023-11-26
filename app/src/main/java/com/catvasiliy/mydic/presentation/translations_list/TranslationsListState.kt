package com.catvasiliy.mydic.presentation.translations_list

import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.presentation.util.SortType
import com.catvasiliy.mydic.presentation.util.TranslationSort

data class TranslationsListState(
    val translations: List<Translation> = emptyList(),
    val sortInfo: TranslationSort = TranslationSort.Date(SortType.Descending),
    val searchQuery: String = "",
    val lastDeletedTranslation: Translation? = null
)