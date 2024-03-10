package com.catvasiliy.mydic.domain.model.translation

import com.catvasiliy.mydic.domain.model.translation.language.Language

data class MissingTranslation(
    override val id: Long,
    override val sourceText: String,
    val sourceLanguage: Language?,
    override val targetLanguage: Language,
    override val translatedAtMillis: Long
) : Translation() {

    companion object {
        fun createNewMissingTranslation(
            sourceText: String,
            sourceLanguage: Language?,
            targetLanguage: Language,
            translatedAtMillis: Long
        ): MissingTranslation {
            return MissingTranslation(
                id = 0,
                sourceText = sourceText,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                translatedAtMillis = translatedAtMillis
            )
        }
    }
}
