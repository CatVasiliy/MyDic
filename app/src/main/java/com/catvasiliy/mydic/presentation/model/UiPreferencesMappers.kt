package com.catvasiliy.mydic.presentation.model

import com.catvasiliy.mydic.domain.model.preferences.LanguagePreferences
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.TranslationOrganizingPreferences
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.filtering.SourceLanguageFilteringInfo
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.filtering.TargetLanguageFilteringInfo
import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationSendingInterval
import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationSendingPreferences
import com.catvasiliy.mydic.presentation.model.preferences.UiLanguagePreferences
import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiSourceLanguageFilteringInfo
import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiTargetLanguageFilteringInfo
import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiTranslationOrganizingPreferences
import com.catvasiliy.mydic.presentation.model.preferences.translation_sending.UiTranslationSendingInterval
import com.catvasiliy.mydic.presentation.model.preferences.translation_sending.UiTranslationSendingPreferences

fun TranslationOrganizingPreferences.toUiTranslationOrganizingPreferences(): UiTranslationOrganizingPreferences {
    return UiTranslationOrganizingPreferences(
        sortingInfo = sortingInfo,
        sourceLanguageFilteringInfo = sourceLanguageFilteringInfo.toUiSourceLanguageFilteringInfo(),
        targetLanguageFilteringInfo = targetLanguageFilteringInfo.toUiTargetLanguageFilteringInfo()
    )
}

private fun SourceLanguageFilteringInfo.toUiSourceLanguageFilteringInfo(): UiSourceLanguageFilteringInfo {
    return when (this) {
        is SourceLanguageFilteringInfo.LanguageAny ->
            UiSourceLanguageFilteringInfo.LanguageAny
        is SourceLanguageFilteringInfo.LanguageUnknown ->
            UiSourceLanguageFilteringInfo.LanguageUnknown
        is SourceLanguageFilteringInfo.LanguageKnown ->
            UiSourceLanguageFilteringInfo.LanguageKnown(language.toUiLanguageNotNull())
    }
}

private fun TargetLanguageFilteringInfo.toUiTargetLanguageFilteringInfo(): UiTargetLanguageFilteringInfo {
    return when (this) {
        is TargetLanguageFilteringInfo.LanguageAny ->
            UiTargetLanguageFilteringInfo.LanguageAny
        is TargetLanguageFilteringInfo.LanguageKnown ->
            UiTargetLanguageFilteringInfo.LanguageKnown(language.toUiLanguageNotNull())
    }
}

fun UiSourceLanguageFilteringInfo.toSourceLanguageFilteringInfo(): SourceLanguageFilteringInfo {
    return when(this) {
        is UiSourceLanguageFilteringInfo.LanguageAny ->
            SourceLanguageFilteringInfo.LanguageAny
        is UiSourceLanguageFilteringInfo.LanguageUnknown ->
            SourceLanguageFilteringInfo.LanguageUnknown
        is UiSourceLanguageFilteringInfo.LanguageKnown ->
            SourceLanguageFilteringInfo.LanguageKnown(language.toLanguageNotNull())
    }
}

fun UiTargetLanguageFilteringInfo.toTargetLanguageFilteringInfo(): TargetLanguageFilteringInfo {
    return when (this) {
        is UiTargetLanguageFilteringInfo.LanguageAny ->
            TargetLanguageFilteringInfo.LanguageAny
        is UiTargetLanguageFilteringInfo.LanguageKnown ->
            TargetLanguageFilteringInfo.LanguageKnown(language.toLanguageNotNull())
    }
}

fun LanguagePreferences.toUiLanguagePreferences(): UiLanguagePreferences {
    return UiLanguagePreferences(
        defaultSourceLanguage = defaultSourceLanguage?.toUiLanguage(),
        defaultTargetLanguage = defaultTargetLanguage.toUiLanguageNotNull()
    )
}

fun TranslationSendingPreferences.toUiTranslationSendingPreferences(): UiTranslationSendingPreferences {
    return UiTranslationSendingPreferences(
        isSendingEnabled = isSendingEnabled,
        sendingInterval = sendingInterval.toUiTranslationSendingInterval()
    )
}

fun TranslationSendingInterval.toUiTranslationSendingInterval(): UiTranslationSendingInterval {
    return UiTranslationSendingInterval.valueOf(name)
}

fun UiTranslationSendingPreferences.toTranslationSendingPreferences(): TranslationSendingPreferences {
    return TranslationSendingPreferences(
        isSendingEnabled = isSendingEnabled,
        sendingInterval = sendingInterval.toTranslationSendingInterval()
    )
}

fun UiTranslationSendingInterval.toTranslationSendingInterval(): TranslationSendingInterval {
    return TranslationSendingInterval.valueOf(name)
}