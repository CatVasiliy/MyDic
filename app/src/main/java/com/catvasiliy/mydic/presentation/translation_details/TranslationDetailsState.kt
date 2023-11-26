package com.catvasiliy.mydic.presentation.translation_details

import com.catvasiliy.mydic.domain.model.translation.Translation

data class TranslationDetailsState(
    val translation: Translation? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)
