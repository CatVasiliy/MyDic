package com.catvasiliy.mydic.domain.model.translation

import java.util.*

class MissingTranslation(
    id: Long = 0,
    sourceText: String,
    translatedAtMillis: Long
) : Translation(id, sourceText, translatedAtMillis) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MissingTranslation) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result *= 31
        return result
    }

    companion object {
        fun fromSourceText(sourceText: String): MissingTranslation {
            return MissingTranslation(
                sourceText = sourceText,
                translatedAtMillis = Date().time
            )
        }
    }
}