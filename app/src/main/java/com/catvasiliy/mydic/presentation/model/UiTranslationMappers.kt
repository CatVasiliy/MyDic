package com.catvasiliy.mydic.presentation.model

import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.domain.model.translation.MissingTranslation
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.model.translation.language.ExtendedLanguage
import com.catvasiliy.mydic.domain.model.translation.language.Language
import com.catvasiliy.mydic.presentation.model.translation.UiExtendedLanguage
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage
import com.catvasiliy.mydic.presentation.model.translation.UiTranslation
import com.catvasiliy.mydic.presentation.model.translation.UiTranslationListItem

fun Translation.toUiTranslationListItem(): UiTranslationListItem {
    return when (this) {
        is ExtendedTranslation -> this.toUiTranslationListItem()
        is MissingTranslation -> this.toUiTranslationListItem()
    }
}

fun Translation.toUiTranslation(): UiTranslation {
    return when (this) {
        is ExtendedTranslation -> this.toUiTranslation()
        is MissingTranslation -> this.toUiTranslation()
    }
}

fun UiTranslation.toTranslation(): Translation {
    return if (isMissingTranslation) {
        this.toMissingTranslation()
    } else {
        this.toExtendedTranslation()
    }
}

fun Language.toUiLanguage(): UiLanguage? {
    return UiLanguage.fromCode(code)
}

fun UiLanguage.toLanguage(): Language? {
    return Language.fromCode(code)
}

fun Language.toUiLanguageNotNull(): UiLanguage {
    return UiLanguage.fromCodeNotNull(code)
}

fun UiLanguage.toLanguageNotNull(): Language {
    return Language.fromCodeNotNull(code)
}

private fun ExtendedTranslation.toUiTranslationListItem(): UiTranslationListItem {
    return UiTranslationListItem(
        id = id,
        sourceText = sourceText,
        translationText = translationText,
        sourceLanguage = sourceLanguage.toUiTranslationSourceLanguage(),
        targetLanguage = targetLanguage.toUiLanguageNotNull(),
        translatedAtMillis = translatedAtMillis
    )
}

private fun MissingTranslation.toUiTranslationListItem(): UiTranslationListItem {
    return UiTranslationListItem(
        id = id,
        sourceText = sourceText,
        translationText = null,
        sourceLanguage = if (sourceLanguage != null) UiExtendedLanguage.Known(sourceLanguage.toUiLanguageNotNull()) else null,
        targetLanguage = targetLanguage.toUiLanguageNotNull(),
        translatedAtMillis = translatedAtMillis
    )
}

private fun ExtendedTranslation.toUiTranslation(): UiTranslation {
    return UiTranslation(
        id = id,
        sourceText = sourceText,
        translationText = translationText,
        sourceLanguage = sourceLanguage.toUiTranslationSourceLanguage(),
        targetLanguage = targetLanguage.toUiLanguageNotNull(),
        sourceTransliteration = sourceTransliteration,
        translatedAtMillis = translatedAtMillis,
        alternativeTranslations = alternativeTranslations,
        definitions = definitions,
        examples = examples
    )
}

private fun UiTranslation.toExtendedTranslation(): ExtendedTranslation {
    return ExtendedTranslation.createExtendedTranslation(
        id = id,
        sourceText = sourceText,
        translationText = translationText
            ?: throw IllegalStateException("translationText cannot be null in ExtendedTranslation."),
        sourceLanguage = sourceLanguage?.toTranslationSourceLanguage()
            ?: throw IllegalStateException("sourceLanguage cannot be null in ExtendedTranslation."),
        targetLanguage = targetLanguage.toLanguageNotNull(),
        sourceTransliteration = sourceTransliteration,
        translatedAtMillis = translatedAtMillis,
        alternativeTranslations = alternativeTranslations,
        definitions = definitions,
        examples = examples
    )
}

private fun MissingTranslation.toUiTranslation(): UiTranslation {
    return UiTranslation(
        id = id,
        sourceText = sourceText,
        translationText = null,
        sourceLanguage = if (sourceLanguage != null) UiExtendedLanguage.Known(sourceLanguage.toUiLanguageNotNull()) else null,
        targetLanguage = targetLanguage.toUiLanguageNotNull(),
        sourceTransliteration = null,
        translatedAtMillis = translatedAtMillis,
        alternativeTranslations = emptyList(),
        definitions = emptyList(),
        examples = emptyList()
    )
}

fun UiTranslation.toMissingTranslation(): MissingTranslation {
    if (!isMissingTranslation)
        throw IllegalStateException("This UiTranslation is not a MissingTranslation")

    val sourceLanguageEntry = if (sourceLanguage != null) {
        val knownLanguage = sourceLanguage as UiExtendedLanguage.Known
        knownLanguage.language.toLanguage()
    } else {
        null
    }
    return MissingTranslation(
        id = id,
        sourceText = sourceText,
        sourceLanguage = sourceLanguageEntry,
        targetLanguage = targetLanguage.toLanguageNotNull(),
        translatedAtMillis = translatedAtMillis
    )
}

private fun ExtendedLanguage.toUiTranslationSourceLanguage(): UiExtendedLanguage {
    return when (this) {
        is ExtendedLanguage.Known -> UiExtendedLanguage.Known(language.toUiLanguageNotNull())
        is ExtendedLanguage.Unknown -> UiExtendedLanguage.Unknown(languageCode)
    }
}

private fun UiExtendedLanguage.toTranslationSourceLanguage(): ExtendedLanguage {
    return when (this) {
        is UiExtendedLanguage.Known -> ExtendedLanguage.Known(language.toLanguageNotNull())
        is UiExtendedLanguage.Unknown -> ExtendedLanguage.Unknown(languageCode)
    }
}