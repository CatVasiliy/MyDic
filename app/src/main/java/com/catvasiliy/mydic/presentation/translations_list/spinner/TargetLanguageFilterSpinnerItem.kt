package com.catvasiliy.mydic.presentation.translations_list.spinner

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiTargetLanguageFilteringInfo

sealed class TargetLanguageFilterSpinnerItem(
    @DrawableRes val drawableResId: Int,
    @StringRes val stringResId: Int,
    val filteringInfo: UiTargetLanguageFilteringInfo
) {
    data object LanguageAny : TargetLanguageFilterSpinnerItem(
        drawableResId = R.drawable.language_icon_any,
        stringResId = R.string.language_any,
        filteringInfo = UiTargetLanguageFilteringInfo.LanguageAny
    )

    class LanguageKnown(
        knownFilteringInfo: UiTargetLanguageFilteringInfo.LanguageKnown
    ) : TargetLanguageFilterSpinnerItem(
        drawableResId = knownFilteringInfo.language.drawableResId,
        stringResId = knownFilteringInfo.language.stringResId,
        filteringInfo = knownFilteringInfo
    )
}