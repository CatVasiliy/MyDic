package com.catvasiliy.mydic.data.remote

import com.catvasiliy.mydic.data.remote.model.ApiAlternativeTranslation
import com.catvasiliy.mydic.data.remote.model.ApiDefinition
import com.catvasiliy.mydic.data.remote.model.ApiExample
import com.catvasiliy.mydic.data.remote.model.ApiTranslation
import com.catvasiliy.mydic.domain.model.translation.AlternativeTranslation
import com.catvasiliy.mydic.domain.model.translation.Definition
import com.catvasiliy.mydic.domain.model.translation.Example
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.model.translation.language.SourceLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TargetLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TranslationSourceLanguage
import java.util.Date

fun ApiTranslation.toTranslation(
    targetLanguage: TargetLanguage,
    isLanguageDetected: Boolean,
    translatedAtMillis: Long = Date().time
): Translation {
    val sourceLanguage = SourceLanguage.fromCode(sourceLanguageCode) ?: SourceLanguage.AUTO

    return Translation.createExtendedTranslation(
        sourceText = baseTranslation.sourceText,
        translationText = baseTranslation.translationText,
        sourceLanguage = TranslationSourceLanguage(
            language = sourceLanguage,
            isDetected = isLanguageDetected,
            autoLanguageCode = if (sourceLanguage == SourceLanguage.AUTO) sourceLanguageCode else null
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