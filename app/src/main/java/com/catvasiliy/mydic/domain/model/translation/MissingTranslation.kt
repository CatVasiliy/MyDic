package com.catvasiliy.mydic.domain.model.translation

import java.util.*

data class MissingTranslation(
    override val id: Long = 0,
    override val sourceText: String,
    override val translatedAtMillis: Long
) : Translation(id, sourceText, translatedAtMillis) {

    companion object {
        fun fromSourceText(sourceText: String): MissingTranslation {
            return MissingTranslation(
                sourceText = sourceText,
                translatedAtMillis = Date().time
            )
        }
    }
}