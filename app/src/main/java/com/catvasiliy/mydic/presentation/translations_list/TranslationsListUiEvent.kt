package com.catvasiliy.mydic.presentation.translations_list

sealed interface TranslationsListUiEvent {
    data object ShowUndoDeleteSnackbar : TranslationsListUiEvent
    data object ScrollListToTop : TranslationsListUiEvent
}