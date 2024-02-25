package com.catvasiliy.mydic.domain.model.translation

import com.catvasiliy.mydic.domain.model.translation.language.SourceLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TargetLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TranslationSourceLanguage
import java.util.*

data class MissingTranslation(
    override val id: Long = 0,
    override val sourceText: String,
    override val sourceLanguage: TranslationSourceLanguage,
    override val targetLanguage: TargetLanguage,
    override val translatedAtMillis: Long
) : Translation(id, sourceText, sourceLanguage, targetLanguage, translatedAtMillis) {

    companion object {
        fun fromSourceText(
            sourceText: String,
            sourceLanguage: SourceLanguage,
            targetLanguage: TargetLanguage
        ): MissingTranslation {
            return MissingTranslation(
                sourceText = sourceText,
                sourceLanguage = TranslationSourceLanguage(sourceLanguage),
                targetLanguage = targetLanguage,
                translatedAtMillis = Date().time
            )
        }
    }
}