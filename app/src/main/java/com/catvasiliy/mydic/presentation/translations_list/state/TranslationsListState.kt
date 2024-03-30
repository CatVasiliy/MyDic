package com.catvasiliy.mydic.presentation.translations_list.state

import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiTranslationOrganizingPreferences
import com.catvasiliy.mydic.presentation.model.translation.UiTranslation
import com.catvasiliy.mydic.presentation.model.translation.UiTranslationListItem

data class TranslationsListState(
    val translations: List<UiTranslationListItem> = emptyList(),
    val listVisibility: TranslationsListVisibility = TranslationsListVisibility.Visible,
    val organizingPreferences: UiTranslationOrganizingPreferences = UiTranslationOrganizingPreferences.getDefault(),
    val searchQuery: String = "",
    val lastDeletedTranslation: UiTranslation? = null
)