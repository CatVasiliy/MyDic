package com.catvasiliy.mydic.domain.model.translation

import com.catvasiliy.mydic.domain.model.translation.language.TargetLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TranslationSourceLanguage

data class ExtendedTranslation(
    override val id: Long = 0,
    override val sourceText: String,
    override val sourceLanguage: TranslationSourceLanguage,
    override val targetLanguage: TargetLanguage,
    override val translatedAtMillis: Long,

    val translationText: String,
    val sourceTransliteration: String?,
    val alternativeTranslations: List<AlternativeTranslation>,
    val definitions: List<Definition>,
    val examples: List<Example>
) : Translation(id, sourceText, sourceLanguage, targetLanguage, translatedAtMillis)
