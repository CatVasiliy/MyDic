package com.catvasiliy.mydic.data.remote

import com.catvasiliy.mydic.data.remote.model.ApiDefinition
import com.catvasiliy.mydic.data.remote.model.ApiExample
import com.catvasiliy.mydic.data.remote.model.ApiAlternativeTranslation
import com.catvasiliy.mydic.data.remote.model.ApiTranslation
import com.catvasiliy.mydic.domain.model.*
import java.util.*

fun ApiTranslation.toExtendedTranslation(): ExtendedTranslation {
    return ExtendedTranslation(
        translationText = primaryTranslation?.get(0)?.translationText ?: "",
        sourceText = primaryTranslation?.get(0)?.sourceText ?: "",
        sourceTransliteration = primaryTranslation?.get(1)?.sourceTransliteration ?: "",
        alternativeTranslations = alternativeTranslations?.flatMap { alternativeTranslation ->
            alternativeTranslation.toAlternativeTranslationsList()
        } ?: emptyList(),
        definitions = definitions?.flatMap { definitions ->
            definitions.toDefinitionsList()
        } ?: emptyList(),
        examples = examples?.toExamplesList() ?: emptyList(),
        translatedAtMillis = Date().time
    )
}

private fun ApiAlternativeTranslation.toAlternativeTranslationsList(): List<AlternativeTranslation> {
    return entries?.map { alternativeTranslation ->
        AlternativeTranslation(
            translationText = alternativeTranslation.translationText ?: "",
            partOfSpeech = partOfSpeech ?: "",
            synonyms = alternativeTranslation.synonyms ?: emptyList()
        )
    } ?: emptyList()
}

private fun ApiDefinition.toDefinitionsList(): List<Definition> {
    return entries?.map { definition ->
        Definition(
            definitionText = definition.definitionText ?: "",
            partOfSpeech = partOfSpeech ?: "",
            exampleText = definition.exampleText ?: ""
        )
    } ?: emptyList()
}

private fun ApiExample.toExamplesList(): List<Example> {
    return entries?.map { example ->
        Example(
            exampleText = example.exampleText ?: ""
        )
    } ?: emptyList()
}