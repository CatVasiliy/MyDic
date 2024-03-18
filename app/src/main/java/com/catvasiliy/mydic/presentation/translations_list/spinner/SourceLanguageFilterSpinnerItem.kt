package com.catvasiliy.mydic.presentation.translations_list.spinner

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiSourceLanguageFilteringInfo

sealed class SourceLanguageFilterSpinnerItem(
    @DrawableRes val drawableResId: Int,
    @StringRes val stringResId: Int,
    val filteringInfo: UiSourceLanguageFilteringInfo
) {
    data object LanguageAny : SourceLanguageFilterSpinnerItem(
        drawableResId = R.drawable.language_icon_any,
        stringResId = R.string.language_any,
        filteringInfo = UiSourceLanguageFilteringInfo.LanguageAny
    )

    data object LanguageUnknown : SourceLanguageFilterSpinnerItem(
        drawableResId = R.drawable.language_icon_unknown,
        stringResId = R.string.language_unknown,
        filteringInfo = UiSourceLanguageFilteringInfo.LanguageUnknown
    )

    class LanguageKnown(
        knownFilteringInfo: UiSourceLanguageFilteringInfo.LanguageKnown
    ) : SourceLanguageFilterSpinnerItem(
        drawableResId = knownFilteringInfo.language.drawableResId,
        stringResId = knownFilteringInfo.language.stringResId,
        filteringInfo = knownFilteringInfo
    )
}