package com.catvasiliy.mydic.presentation.util

sealed class SortType {
    object Ascending : SortType()
    object Descending : SortType()
}
