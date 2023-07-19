package com.catvasiliy.mydic.presentation.translations_list

import com.catvasiliy.mydic.domain.model.SimpleTranslation
import com.catvasiliy.mydic.domain.model.Translation
import com.catvasiliy.mydic.presentation.util.SortType
import com.catvasiliy.mydic.presentation.util.TranslationSort

data class TranslationsListState(
    val translations: List<Translation>? = null,
    val sortInfo: TranslationSort = TranslationSort.Date(SortType.Descending),
    val searchResultTranslations: List<Translation>? = null
)