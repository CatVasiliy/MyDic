package com.catvasiliy.mydic.presentation.model

import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.model.translation.language.SourceLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TargetLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TranslationSourceLanguage
import com.catvasiliy.mydic.presentation.model.translation.UiSourceLanguage
import com.catvasiliy.mydic.presentation.model.translation.UiTargetLanguage
import com.catvasiliy.mydic.presentation.model.translation.UiTranslation
import com.catvasiliy.mydic.presentation.model.translation.UiTranslationListItem
import com.catvasiliy.mydic.presentation.model.translation.UiTranslationSourceLanguage

fun Translation.toUiTranslationListItem(): UiTranslationListItem {
    return UiTranslationListItem(
        id = id,
        sourceText = sourceText,
        translationText = translationText,
        sourceLanguage = sourceLanguage.toUiTranslationSourceLanguage(),
        targetLanguage = targetLanguage.toUiTargetLanguage(),
        translatedAtMillis = translatedAtMillis
    )
}

fun Translation.toUiTranslation(): UiTranslation {
    return UiTranslation(
        id = id,
        sourceText = sourceText,
        translationText = translationText,
        sourceLanguage = sourceLanguage.toUiTranslationSourceLanguage(),
        targetLanguage = targetLanguage.toUiTargetLanguage(),
        sourceTransliteration = sourceTransliteration,
        translatedAtMillis = translatedAtMillis,
        alternativeTranslations = alternativeTranslations,
        definitions = definitions,
        examples = examples
    )
}

fun UiTranslation.toTranslation(): Translation {
    return if (isMissingTranslation) {
        Translation.createMissingTranslation(
            id = id,
            sourceText = sourceText,
            sourceLanguage = sourceLanguage.toTranslationSourceLanguage(),
            targetLanguage = targetLanguage.toTargetLanguage(),
            translatedAtMillis = translatedAtMillis
        )
    } else {
        Translation.createExtendedTranslation(
            id = id,
            sourceText = sourceText,
            translationText = translationText ?: throw IllegalStateException("translationText cannot be null in Extended Translation."),
            sourceLanguage = sourceLanguage.toTranslationSourceLanguage(),
            targetLanguage = targetLanguage.toTargetLanguage(),
            sourceTransliteration = sourceTransliteration,
            translatedAtMillis = translatedAtMillis,
            alternativeTranslations = alternativeTranslations,
            definitions = definitions,
            examples = examples
        )
    }
}

fun SourceLanguage.toUiSourceLanguage(): UiSourceLanguage {
    return UiSourceLanguage.fromCode(code) ?: UiSourceLanguage.AUTO
}

fun UiSourceLanguage.toSourceLanguage(): SourceLanguage {
    return SourceLanguage.fromCode(code) ?: SourceLanguage.AUTO
}

fun TargetLanguage.toUiTargetLanguage(): UiTargetLanguage {
    return UiTargetLanguage.fromCode(code)
}

fun UiTargetLanguage.toTargetLanguage(): TargetLanguage {
    return TargetLanguage.fromCode(code)
}

private fun TranslationSourceLanguage.toUiTranslationSourceLanguage(): UiTranslationSourceLanguage {
    return UiTranslationSourceLanguage(
        language = language.toUiSourceLanguage(),
        isDetected = isDetected,
        autoLanguageCode = autoLanguageCode
    )
}

private fun UiTranslationSourceLanguage.toTranslationSourceLanguage(): TranslationSourceLanguage {
    return TranslationSourceLanguage(
        language = language.toSourceLanguage(),
        isDetected = isDetected,
        autoLanguageCode = autoLanguageCode
    )
}