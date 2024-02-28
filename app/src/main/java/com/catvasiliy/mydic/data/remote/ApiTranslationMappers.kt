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
        sourceText = baseTranslation.sourceText,
        translationText = baseTranslation.translationText,
        sourceLanguage = TranslationSourceLanguage(
            language = SourceLanguage.fromCode(sourceLanguageCode) ?: SourceLanguage.AUTO,
            isDetected = isLanguageDetected
        ),
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