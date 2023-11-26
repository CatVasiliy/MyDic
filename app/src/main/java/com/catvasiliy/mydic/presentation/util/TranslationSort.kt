package com.catvasiliy.mydic.presentation.util

import com.catvasiliy.mydic.domain.model.translation.SimpleTranslation
import com.catvasiliy.mydic.domain.model.translation.Translation

sealed class TranslationSort(var sortType: SortType) {
    class Date(sortType: SortType) : TranslationSort(sortType)
    class SourceText(sortType: SortType) : TranslationSort(sortType)
    class TranslationText(sortType: SortType) : TranslationSort(sortType)
}

fun List<Translation>.sortedCustom(sortInfo: TranslationSort): List<Translation> {
    return when(sortInfo.sortType) {
        is SortType.Ascending -> this.sortedCustomAscending(sortInfo)
        is SortType.Descending -> this.sortedCustomDescending(sortInfo)
    }
}

private fun List<Translation>.sortedCustomAscending(
    sortInfo: TranslationSort
): List<Translation> {

    return when(sortInfo) {
        is TranslationSort.Date -> this.sortedBy { it.translatedAtMillis }
        is TranslationSort.SourceText -> this.sortedBy { it.sourceText }
        is TranslationSort.TranslationText -> this.sortedBy {
            if (it is SimpleTranslation) it.translationText else ""
        }
    }
}

private fun List<Translation>.sortedCustomDescending(
    sortInfo: TranslationSort
): List<Translation> {

    return when(sortInfo) {
        is TranslationSort.Date -> this.sortedByDescending { it.translatedAtMillis }
        is TranslationSort.SourceText -> this.sortedByDescending { it.sourceText }
        is TranslationSort.TranslationText -> this.sortedByDescending {
            if (it is SimpleTranslation) it.translationText else ""
        }
    }
}