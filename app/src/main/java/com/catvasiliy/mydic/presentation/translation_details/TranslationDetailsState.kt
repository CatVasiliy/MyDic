package com.catvasiliy.mydic.presentation.translation_details

import com.catvasiliy.mydic.presentation.model.translation.UiTranslation

data class TranslationDetailsState(
    val translation: UiTranslation? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)
