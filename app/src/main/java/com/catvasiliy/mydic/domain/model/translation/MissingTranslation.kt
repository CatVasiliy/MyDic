package com.catvasiliy.mydic.domain.model.translation

import java.util.*

data class MissingTranslation(
    override val id: Long = 0,
    override val sourceText: String,
    override val sourceLanguage: Language,
    override val targetLanguage: Language,
    override val translatedAtMillis: Long
) : Translation(id, sourceText, sourceLanguage, targetLanguage, translatedAtMillis) {

    companion object {
        fun fromSourceText(
            sourceText: String,
            sourceLanguage: Language,
            targetLanguage: Language
        ): MissingTranslation {
            return MissingTranslation(
                sourceText = sourceText,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                translatedAtMillis = Date().time
            )
        }
    }
}