package com.catvasiliy.mydic.data.remote

import com.catvasiliy.mydic.data.remote.model.ApiAlternativeTranslation
import com.catvasiliy.mydic.data.remote.model.ApiDefinition
import com.catvasiliy.mydic.data.remote.model.ApiExample
import com.catvasiliy.mydic.data.remote.model.ApiTranslation
import com.catvasiliy.mydic.domain.model.translation.AlternativeTranslation
import com.catvasiliy.mydic.domain.model.translation.Definition
import com.catvasiliy.mydic.domain.model.translation.Example
import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.domain.model.translation.language.ExtendedLanguage
import com.catvasiliy.mydic.domain.model.translation.language.Language

fun ApiTranslation.toTranslation(
    targetLanguage: Language,
    isLanguageDetected: Boolean,
    translatedAtMillis: Long
): ExtendedTranslation {

    val language = Language.fromCode(sourceLanguageCode)

    val sourceLanguage = if (language != null) {
        ExtendedLanguage.Known(language, isLanguageDetected)
    } else {
        ExtendedLanguage.Unknown(sourceLanguageCode)
    }

    return ExtendedTranslation.createExtendedTranslation(
        sourceText = baseTranslation.sourceText,
        translationText = baseTranslation.translationText,
        sourceLanguage = sourceLanguage,
        targetLanguage = targetLanguage,
        sourceTransliteration = baseTranslation.sourceTransliteration,
        alternativeTranslations = alternativeTranslations.map(
            ApiAlternativeTranslation::toAlternativeTranslation
        ),
        definitions = definitions.map(ApiDefinition::toDefinition),
        examples = examples.map(ApiExample::toExample),
        translatedAtMillis = translatedAtMillis
    )
}

private fun ApiAlternativeTranslation.toAlternativeTranslation(): AlternativeTranslation {
    return AlternativeTranslation(
        translationText = translationText,
        partOfSpeech = partOfSpeech,
        synonyms = synonyms
    )
}

private fun ApiDefinition.toDefinition(): Definition {
    return Definition(
        definitionText = definitionText,
        partOfSpeech = partOfSpeech,
        exampleText = exampleText
    )
}

private fun ApiExample.toExample(): Example {
    return Example(exampleText = exampleText)
}