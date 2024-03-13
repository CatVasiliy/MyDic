package com.catvasiliy.mydic.presentation.translations_list.translation_organizing

import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiSourceLanguageFilteringInfo
import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiTargetLanguageFilteringInfo
import com.catvasiliy.mydic.presentation.model.translation.UiExtendedLanguage
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage
import com.catvasiliy.mydic.presentation.model.translation.UiTranslationListItem

fun List<UiTranslationListItem>.filterBySourceTextContains(
    query: String
): List<UiTranslationListItem> {
    return if (query.isNotBlank()) {
        filter { it.sourceText.contains(query) }
    } else {
        this
    }
}

fun List<UiTranslationListItem>.filterBySourceLanguage(
    filteringInfo: UiSourceLanguageFilteringInfo
): List<UiTranslationListItem> {
    return when (filteringInfo) {
        is UiSourceLanguageFilteringInfo.LanguageAny -> this
        is UiSourceLanguageFilteringInfo.LanguageUnknown -> this.filterByUnknownSourceLanguage()
        is UiSourceLanguageFilteringInfo.LanguageKnown -> this.filterByKnownSourceLanguage(filteringInfo.language)
    }
}

private fun List<UiTranslationListItem>.filterByKnownSourceLanguage(
    sourceLanguage: UiLanguage
): List<UiTranslationListItem> {
    return filter { translation ->
        if (translation.sourceLanguage is UiExtendedLanguage.Known) {
            translation.sourceLanguage.language == sourceLanguage
        } else {
            false
        }
    }
}

private fun List<UiTranslationListItem>.filterByUnknownSourceLanguage(): List<UiTranslationListItem> {
    return filter { translation ->
        translation.sourceLanguage !is UiExtendedLanguage.Known
    }
}

fun List<UiTranslationListItem>.filterByTargetLanguage(
    filteringInfo: UiTargetLanguageFilteringInfo
): List<UiTranslationListItem> {
    return when(filteringInfo) {
        is UiTargetLanguageFilteringInfo.LanguageAny -> this
        is UiTargetLanguageFilteringInfo.LanguageKnown -> this.filter { it.targetLanguage == filteringInfo.language }
    }
}

