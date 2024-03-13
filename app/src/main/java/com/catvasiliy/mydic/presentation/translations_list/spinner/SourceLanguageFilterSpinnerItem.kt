package com.catvasiliy.mydic.presentation.translations_list.spinner

import androidx.annotation.StringRes
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiSourceLanguageFilteringInfo

sealed class SourceLanguageFilterSpinnerItem(
    @StringRes val stringResourceId: Int,
    val filteringInfo: UiSourceLanguageFilteringInfo
) {
    data object LanguageAny : SourceLanguageFilterSpinnerItem(
        stringResourceId = R.string.language_any,
        filteringInfo = UiSourceLanguageFilteringInfo.LanguageAny
    )

    data object LanguageUnknown : SourceLanguageFilterSpinnerItem(
        stringResourceId = R.string.language_auto,
        filteringInfo = UiSourceLanguageFilteringInfo.LanguageUnknown
    )

    class LanguageKnown(
        knownFilteringInfo: UiSourceLanguageFilteringInfo.LanguageKnown
    ) : SourceLanguageFilterSpinnerItem(
        stringResourceId = knownFilteringInfo.language.stringResourceId,
        filteringInfo = knownFilteringInfo
    )
}