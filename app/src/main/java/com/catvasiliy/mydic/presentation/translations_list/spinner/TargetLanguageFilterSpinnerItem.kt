package com.catvasiliy.mydic.presentation.translations_list.spinner

import androidx.annotation.StringRes
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiTargetLanguageFilteringInfo

sealed class TargetLanguageFilterSpinnerItem(
    @StringRes val stringResourceId: Int,
    val filteringInfo: UiTargetLanguageFilteringInfo
) {
    data object LanguageAny : TargetLanguageFilterSpinnerItem(
        stringResourceId = R.string.language_any,
        filteringInfo = UiTargetLanguageFilteringInfo.LanguageAny
    )

    class LanguageKnown(
        knownFilteringInfo: UiTargetLanguageFilteringInfo.LanguageKnown
    ) : TargetLanguageFilterSpinnerItem(
        stringResourceId = knownFilteringInfo.language.stringResourceId,
        filteringInfo = knownFilteringInfo
    )
}