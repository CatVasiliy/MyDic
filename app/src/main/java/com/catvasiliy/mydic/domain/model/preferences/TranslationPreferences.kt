package com.catvasiliy.mydic.domain.model.preferences

import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationSendingPreferences
import com.catvasiliy.mydic.domain.model.preferences.translation_sorting.SortingOrder
import com.catvasiliy.mydic.domain.model.preferences.translation_sorting.TranslationSortingInfo
import kotlinx.serialization.Serializable

@Serializable
data class TranslationPreferences(
    val languagePreferences: LanguagePreferences = LanguagePreferences(),
    val sortingInfo: TranslationSortingInfo = TranslationSortingInfo.Date(SortingOrder.Descending),
    val translationSendingPreferences: TranslationSendingPreferences = TranslationSendingPreferences()
)
