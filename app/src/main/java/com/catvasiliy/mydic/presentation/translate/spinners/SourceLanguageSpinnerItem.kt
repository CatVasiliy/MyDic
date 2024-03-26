package com.catvasiliy.mydic.presentation.translate.spinners

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage

sealed class SourceLanguageSpinnerItem(
    val language: UiLanguage?,
    @DrawableRes val drawableResId: Int,
    @StringRes val stringResId: Int
) {
    data object LanguageAuto : SourceLanguageSpinnerItem(
        language = null,
        drawableResId = R.drawable.language_icon_auto,
        stringResId = R.string.language_auto
    )

    class LanguageKnown(language: UiLanguage) : SourceLanguageSpinnerItem(
        language = language,
        drawableResId = language.drawableResId,
        stringResId = language.stringResId
    )
}
