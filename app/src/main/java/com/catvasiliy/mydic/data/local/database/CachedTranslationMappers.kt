package com.catvasiliy.mydic.data.local.database

import com.catvasiliy.mydic.data.local.database.model.CachedAlternativeAggregate
import com.catvasiliy.mydic.data.local.database.model.CachedAlternativeTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedDefinition
import com.catvasiliy.mydic.data.local.database.model.CachedExample
import com.catvasiliy.mydic.data.local.database.model.CachedMissingTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedSynonym
import com.catvasiliy.mydic.data.local.database.model.CachedTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedTranslationAggregate
import com.catvasiliy.mydic.domain.model.translation.AlternativeTranslation
import com.catvasiliy.mydic.domain.model.translation.Definition
import com.catvasiliy.mydic.domain.model.translation.Example
import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.domain.model.translation.MissingTranslation
import com.catvasiliy.mydic.domain.model.translation.SimpleTranslation

fun CachedTranslationAggregate.toExtendedTranslation(): ExtendedTranslation {
    return ExtendedTranslation(
        id = translation.id,
        translationText = translation.translationText,
        sourceText = translation.sourceText,
        sourceTransliteration = translation.sourceTransliteration,
        alternativeTranslations = alternativeTranslations.map(
            CachedAlternativeAggregate::toAlternativeTranslation
        ),
        definitions = definitions.map(CachedDefinition::toDefinition),
        examples = examples.map(CachedExample::toExample),
        translatedAtMillis = translation.translatedAtMillis
    )
}

fun CachedTranslation.toSimpleTranslation(): SimpleTranslation {
    return SimpleTranslation(
        id = id,
        translationText = translationText,
        sourceText = sourceText,
        translatedAtMillis = translatedAtMillis
    )
}

fun ExtendedTranslation.toCachedTranslation(): CachedTranslationAggregate {
    val cachedTranslation = CachedTranslation(
        id = id,
        translationText = translationText,
        sourceText = sourceText,
        sourceTransliteration = sourceTransliteration,
        translatedAtMillis = translatedAtMillis
    )
    return CachedTranslationAggregate(
        translation = cachedTranslation,
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
        translatedAtMillis = translatedAtMillis
    )
}

fun MissingTranslation.toCachedMissingTranslation(): CachedMissingTranslation {
    return CachedMissingTranslation(
        id = id,
        sourceText = sourceText,
        translatedAtMillis = translatedAtMillis
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