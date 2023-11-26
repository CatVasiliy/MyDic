package com.catvasiliy.mydic.domain.model.translation

open class SimpleTranslation(
    id: Long = 0,
    val translationText: String,
    sourceText: String,
    translatedAtMillis: Long
) : Translation(id, sourceText, translatedAtMillis) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SimpleTranslation) return false
        if (!super.equals(other)) return false

        if (translationText != other.translationText) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + translationText.hashCode()
        return result
    }
}