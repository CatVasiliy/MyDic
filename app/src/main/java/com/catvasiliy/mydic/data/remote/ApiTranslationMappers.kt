package com.catvasiliy.mydic.data.remote

import com.catvasiliy.mydic.data.remote.model.ApiDefinition
import com.catvasiliy.mydic.data.remote.model.ApiExample
import com.catvasiliy.mydic.data.remote.model.ApiAlternativeTranslation
import com.catvasiliy.mydic.data.remote.model.ApiTranslation
import com.catvasiliy.mydic.domain.model.translation.AlternativeTranslation
import com.catvasiliy.mydic.domain.model.translation.Definition
import com.catvasiliy.mydic.domain.model.translation.Example
import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.domain.model.translation.language.SourceLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TargetLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TranslationSourceLanguage
import java.util.*

fun ApiTranslation.toExtendedTranslation(
    targetLanguage: TargetLanguage,
    isLanguageDetected: Boolean,
    translatedAtMillis: Long = Date().time
): ExtendedTranslation {
    return ExtendedTranslation(
        sourceText = sourceText,
        translationText = translationText,
        sourceLanguage = TranslationSourceLanguage(
            language = SourceLanguage.fromCode(sourceLanguageCode) ?: SourceLanguage.AUTO,
            isDetected = isLanguageDetected
        ),
        targetLanguage = targetLanguage,
        sourceTransliteration = sourceTransliteration,
        alternativeTranslations = alternativeTranslations.flatMap { alternativeTranslation ->
            alternativeTranslation.toAlternativeTranslationsList()
        },
        definitions = definitions.flatMap { definitions ->
            definitions.toDefinitionsList()
        },
        examples = examples.toExamplesList(),
        translatedAtMillis = translatedAtMillis
    )
}

private fun ApiAlternativeTranslation.toAlternativeTranslationsList(): List<AlternativeTranslation> {
    return entries.map { alternativeTranslation ->
        AlternativeTranslation(
            translationText = alternativeTranslation.translationText,
            partOfSpeech = partOfSpeech,
            synonyms = alternativeTranslation.synonyms
        )
    }
}

private fun ApiDefinition.toDefinitionsList(): List<Definition> {
    return entries.map { definition ->
        Definition(
            definitionText = definition.definitionText,
            partOfSpeech = partOfSpeech,
            exampleText = definition.exampleText
        )
    }
}

private fun ApiExample.toExamplesList(): List<Example> {
    return entries.map { example ->
        Example(
            exampleText = example.exampleText
        )
    }
}