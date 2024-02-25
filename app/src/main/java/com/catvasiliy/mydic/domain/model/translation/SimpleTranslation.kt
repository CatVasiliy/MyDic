package com.catvasiliy.mydic.domain.model.translation

import com.catvasiliy.mydic.domain.model.translation.language.TargetLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TranslationSourceLanguage

data class SimpleTranslation(
    override val id: Long = 0,
    override val sourceText: String,
    override val sourceLanguage: TranslationSourceLanguage,
    override val targetLanguage: TargetLanguage,
    override val translatedAtMillis: Long,

    val translationText: String
) : Translation(id, sourceText, sourceLanguage, targetLanguage, translatedAtMillis)