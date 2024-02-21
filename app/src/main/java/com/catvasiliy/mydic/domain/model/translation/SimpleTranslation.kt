package com.catvasiliy.mydic.domain.model.translation

data class SimpleTranslation(
    override val id: Long = 0,
    override val sourceText: String,
    override val sourceLanguage: Language,
    override val targetLanguage: Language,
    override val translatedAtMillis: Long,

    val translationText: String
) : Translation(id, sourceText, sourceLanguage, targetLanguage, translatedAtMillis)