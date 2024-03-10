package com.catvasiliy.mydic.presentation.util

import com.catvasiliy.mydic.presentation.model.translation.UiTranslationListItem

sealed class TranslationSort(var sortType: SortType) {
    class Date(sortType: SortType) : TranslationSort(sortType)
    class SourceText(sortType: SortType) : TranslationSort(sortType)
    class TranslationText(sortType: SortType) : TranslationSort(sortType)
}

fun List<UiTranslationListItem>.sortedCustom(sortInfo: TranslationSort): List<UiTranslationListItem> {
    return when(sortInfo.sortType) {
        is SortType.Ascending -> this.sortedCustomAscending(sortInfo)
        is SortType.Descending -> this.sortedCustomDescending(sortInfo)
    }
}

private fun List<UiTranslationListItem>.sortedCustomAscending(
    sortInfo: TranslationSort
): List<UiTranslationListItem> {

    return when(sortInfo) {
        is TranslationSort.Date -> this.sortedBy { it.translatedAtMillis }
        is TranslationSort.SourceText -> this.sortedBy { it.sourceText }
        is TranslationSort.TranslationText -> this.sortedBy { it.translationText }
    }
}

private fun List<UiTranslationListItem>.sortedCustomDescending(
    sortInfo: TranslationSort
): List<UiTranslationListItem> {

    return when(sortInfo) {
        is TranslationSort.Date -> this.sortedByDescending { it.translatedAtMillis }
        is TranslationSort.SourceText -> this.sortedByDescending { it.sourceText }
        is TranslationSort.TranslationText -> this.sortedByDescending { it.translationText }
    }
}