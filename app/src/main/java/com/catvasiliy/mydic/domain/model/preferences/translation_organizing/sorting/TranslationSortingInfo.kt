package com.catvasiliy.mydic.domain.model.preferences.translation_organizing.sorting

import kotlinx.serialization.Serializable

@Serializable
sealed class TranslationSortingInfo {

    abstract val sortingOrder: SortingOrder

    @Serializable
    data class Date(override val sortingOrder: SortingOrder) : TranslationSortingInfo()

    @Serializable
    data class SourceText(override val sortingOrder: SortingOrder) : TranslationSortingInfo()

    @Serializable
    data class TranslationText(override val sortingOrder: SortingOrder) : TranslationSortingInfo()

    fun copyChangeSortingOrder(sortingOrder: SortingOrder): TranslationSortingInfo =
        when (this) {
            is Date -> this.copy(sortingOrder = sortingOrder)
            is SourceText -> this.copy(sortingOrder = sortingOrder)
            is TranslationText -> this.copy(sortingOrder = sortingOrder)
        }
}