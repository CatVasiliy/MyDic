package com.catvasiliy.mydic.data.local.database

import com.catvasiliy.mydic.data.local.database.model.CachedAlternativeAggregate
import com.catvasiliy.mydic.data.local.database.model.CachedAlternativeTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedDefinition
import com.catvasiliy.mydic.data.local.database.model.CachedExample
import com.catvasiliy.mydic.data.local.database.model.CachedMissingTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedSynonym
import com.catvasiliy.mydic.data.local.database.model.CachedTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedTranslationAggregate
import com.catvasiliy.mydic.domain.model.preferences.TranslationForSending
import com.catvasiliy.mydic.domain.model.translation.AlternativeTranslation
import com.catvasiliy.mydic.domain.model.translation.Definition
import com.catvasiliy.mydic.domain.model.translation.Example
import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.domain.model.translation.MissingTranslation
import com.catvasiliy.mydic.domain.model.translation.language.ExtendedLanguage

fun CachedTranslationAggregate.toExtendedTranslation(): ExtendedTranslation {

    val sourceLanguage = if (baseTranslation.sourceLanguage != null) {
        ExtendedLanguage.Known(baseTranslation.sourceLanguage, baseTranslation.isLanguageDetected)
    } else {
        ExtendedLanguage.Unknown(baseTranslation.unknownSourceLanguageCode
            ?: throw IllegalStateException("If sourceLanguage is null, then unknownSourceLanguageCode cannot be null.")
        )
    }

    return ExtendedTranslation.createExtendedTranslation(
        id = baseTranslation.id,
        sourceText = baseTranslation.sourceText,
        translationText = baseTranslation.translationText,
        sourceLanguage = sourceLanguage,
        targetLanguage = baseTranslation.targetLanguage,
        sourceTransliteration = baseTranslation.sourceTransliteration,
        alternativeTranslations = alternativeTranslations.map(
            CachedAlternativeAggregate::toAlternativeTranslation
        ),
        definitions = definitions.map(CachedDefinition::toDefinition),
        examples = examples.map(CachedExample::toExample),
        translatedAtMillis = baseTranslation.translatedAtMillis
    )
}

fun CachedTranslation.toSimpleTranslation(): ExtendedTranslation {

    val sourceLanguage = if (sourceLanguage != null) {
        ExtendedLanguage.Known(sourceLanguage, isLanguageDetected)
    } else {
        ExtendedLanguage.Unknown(unknownSourceLanguageCode
            ?: throw IllegalStateException("If sourceLanguage is null, then unknownSourceLanguageCode cannot be null.")
        )
    }

    return ExtendedTranslation.createSimpleTranslation(
        id = id,
        sourceText = sourceText,
        translationText = translationText,
        sourceLanguage = sourceLanguage,
        targetLanguage = targetLanguage,
        translatedAtMillis = translatedAtMillis
    )
}

fun ExtendedTranslation.toCachedTranslation(): CachedTranslationAggregate {

    val sourceLanguageEntry = when (sourceLanguage) {
        is ExtendedLanguage.Known -> sourceLanguage.language
        is ExtendedLanguage.Unknown -> null
    }

    val isLanguageDetected = when (sourceLanguage) {
        is ExtendedLanguage.Known -> sourceLanguage.isDetected
        is ExtendedLanguage.Unknown -> true
    }

    val unknownLanguageCode = when (sourceLanguage) {
        is ExtendedLanguage.Known -> null
        is ExtendedLanguage.Unknown -> sourceLanguage.languageCode
    }

    val cachedTranslation = CachedTranslation(
        id = id,
        sourceText = sourceText,
        translationText = translationText,
        sourceLanguage = sourceLanguageEntry,
        isLanguageDetected = isLanguageDetected,
        unknownSourceLanguageCode = unknownLanguageCode,
        targetLanguage = targetLanguage,
        sourceTransliteration = sourceTransliteration,
        translatedAtMillis = translatedAtMillis
    )
    return CachedTranslationAggregate(
        baseTranslation = cachedTranslation,
        alternativeTranslations = alternativeTranslations.map { domainAlternativeTranslation ->
            domainAlternativeTranslation.toCachedAlternativeTranslation(
                translationId = id
            )
        },
        definitions = definitions.map { domainDefinition ->
            domainDefinition.toCachedDefinition(translationId = id)
        },
        examples = examples.map { domainExample ->
            domainExample.toCachedExample(translationId = id)
        }
    )
}

fun CachedMissingTranslation.toMissingTranslation(): MissingTranslation {
    return MissingTranslation(
        id = id,
        sourceText = sourceText,
        sourceLanguage = sourceLanguage,
        targetLanguage = targetLanguage,
        translatedAtMillis = translatedAtMillis
    )
}

fun MissingTranslation.toCachedMissingTranslation(): CachedMissingTranslation {
    return CachedMissingTranslation(
        id = id,
        sourceText = sourceText,
        sourceLanguage = sourceLanguage,
        targetLanguage = targetLanguage,
        translatedAtMillis = translatedAtMillis
    )
}

fun CachedTranslationAggregate.toTranslationForSending(): TranslationForSending {
    return TranslationForSending(
        id = baseTranslation.id,
        sourceText = baseTranslation.sourceText,
        translationText = baseTranslation.translationText
    )
}

private fun CachedAlternativeAggregate.toAlternativeTranslation(): AlternativeTranslation {
    return AlternativeTranslation(
        translationText = alternativeTranslation.translationText,
        partOfSpeech = alternativeTranslation.partOfSpeech,
        synonyms = synonyms.map { cachedSynonym -> cachedSynonym.synonymText }
    )
}

private fun AlternativeTranslation.toCachedAlternativeTranslation(
    translationId: Long
): CachedAlternativeAggregate {

    val cachedAlternativeTranslation = CachedAlternativeTranslation(
        translationId = translationId,
        translationText = translationText,
        partOfSpeech = partOfSpeech
    )
    val cachedSynonyms = synonyms.map { domainSynonym ->
        CachedSynonym(synonymText = domainSynonym)
    }
    return CachedAlternativeAggregate(
        alternativeTranslation = cachedAlternativeTranslation,
        synonyms = cachedSynonyms
    )
}

private fun CachedDefinition.toDefinition(): Definition {
    return Definition(
        definitionText = definitionText,
        partOfSpeech = partOfSpeech,
        exampleText = exampleText
    )
}

private fun Definition.toCachedDefinition(translationId: Long): CachedDefinition {
    return CachedDefinition(
        translationId = translationId,
        definitionText = definitionText,
        partOfSpeech = partOfSpeech,
        exampleText = exampleText
    )
}

private fun CachedExample.toExample(): Example {
    return Example(
        exampleText = exampleText
    )
}

private fun Example.toCachedExample(translationId: Long): CachedExample {
    return CachedExample(
        translationId = translationId,
        exampleText = exampleText
    )
}