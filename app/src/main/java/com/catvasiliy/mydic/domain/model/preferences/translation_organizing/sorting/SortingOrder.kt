package com.catvasiliy.mydic.domain.model.preferences.translation_organizing.sorting

import kotlinx.serialization.Serializable

@Serializable
sealed interface SortingOrder {
    @Serializable data object Ascending : SortingOrder
    @Serializable data object Descending : SortingOrder
}
