package com.catvasiliy.mydic.presentation.translations_list.spinner

import androidx.annotation.StringRes
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage

sealed class SourceLanguageFilterSpinnerItem(@StringRes val stringResourceId: Int) {
    object LanguageAny : SourceLanguageFilterSpinnerItem(R.string.language_any)
    object LanguageUnknown : SourceLanguageFilterSpinnerItem(R.string.language_auto)
    data class LanguageKnown(val language: UiLanguage) : SourceLanguageFilterSpinnerItem(language.stringResourceId)
}