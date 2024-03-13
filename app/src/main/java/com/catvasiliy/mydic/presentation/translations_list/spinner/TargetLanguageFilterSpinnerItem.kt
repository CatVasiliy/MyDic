package com.catvasiliy.mydic.presentation.translations_list.spinner

import androidx.annotation.StringRes
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage

sealed class TargetLanguageFilterSpinnerItem(@StringRes val stringResourceId: Int) {
    object LanguageAny : TargetLanguageFilterSpinnerItem(R.string.language_any)
    data class LanguageKnown(val language: UiLanguage) : TargetLanguageFilterSpinnerItem(language.stringResourceId)
}