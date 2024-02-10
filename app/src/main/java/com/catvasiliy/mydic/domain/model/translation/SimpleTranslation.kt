package com.catvasiliy.mydic.domain.model.translation

data class SimpleTranslation(
    override val id: Long = 0,
    override val sourceText: String,
    override val translatedAtMillis: Long,

    val translationText: String
) : Translation(id, sourceText, translatedAtMillis)