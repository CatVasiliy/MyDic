package com.catvasiliy.mydic.presentation.util

import com.catvasiliy.mydic.domain.model.preferences.translation_sorting.SortingOrder
import com.catvasiliy.mydic.domain.model.preferences.translation_sorting.TranslationSortingInfo
import com.catvasiliy.mydic.presentation.model.translation.UiTranslationListItem

fun List<UiTranslationListItem>.sortedCustom(
    sortingInfo: TranslationSortingInfo
): List<UiTranslationListItem> {

    return when(sortingInfo.sortingOrder) {
        is SortingOrder.Ascending -> this.sortedCustomAscending(sortingInfo)
        is SortingOrder.Descending -> this.sortedCustomDescending(sortingInfo)
    }
}

private fun List<UiTranslationListItem>.sortedCustomAscending(
    sortingInfo: TranslationSortingInfo
): List<UiTranslationListItem> {

    return when(sortingInfo) {
        is TranslationSortingInfo.Date -> this.sortedBy { it.translatedAtMillis }
        is TranslationSortingInfo.SourceText -> this.sortedBy { it.sourceText }
        is TranslationSortingInfo.TranslationText -> this.sortedBy { it.translationText }
    }
}

private fun List<UiTranslationListItem>.sortedCustomDescending(
    sortingInfo: TranslationSortingInfo
): List<UiTranslationListItem> {

    return when(sortingInfo) {
        is TranslationSortingInfo.Date -> this.sortedByDescending { it.translatedAtMillis }
        is TranslationSortingInfo.SourceText -> this.sortedByDescending { it.sourceText }
        is TranslationSortingInfo.TranslationText -> this.sortedByDescending { it.translationText }
    }
}