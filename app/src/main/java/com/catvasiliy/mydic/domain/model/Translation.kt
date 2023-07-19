package com.catvasiliy.mydic.domain.model

open class Translation(
    val id: Long = 0,
    val sourceText: String,
    val translatedAtMillis: Long
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Translation) return false

        if (id != other.id) return false
        if (sourceText != other.sourceText) return false
        if (translatedAtMillis != other.translatedAtMillis) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + sourceText.hashCode()
        result = 31 * result + translatedAtMillis.hashCode()
        return result
    }
}