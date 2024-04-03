package com.catvasiliy.mydic.presentation.translation_details

import com.catvasiliy.mydic.presentation.model.translation.UiTranslation

sealed interface TranslationDetailsState {
    data object Loading : TranslationDetailsState
    data class Translation(val translation: UiTranslation) : TranslationDetailsState
    data class MissingTranslation(val missingTranslation: UiTranslation) : TranslationDetailsState
}