package com.catvasiliy.mydic.domain.model

class ExtendedTranslation(
    id: Long = 0,
    translationText: String,
    sourceText: String,
    val sourceTransliteration: String,
    val alternativeTranslations: List<AlternativeTranslation>,
    val definitions: List<Definition>,
    val examples: List<Example>,
    translatedAtMillis: Long
) : SimpleTranslation(id, translationText, sourceText, translatedAtMillis) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExtendedTranslation) return false
        if (!super.equals(other)) return false

        if (sourceTransliteration != other.sourceTransliteration) return false
        if (alternativeTranslations != other.alternativeTranslations) return false
        if (definitions != other.definitions) return false
        if (examples != other.examples) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + sourceTransliteration.hashCode()
        result = 31 * result + alternativeTranslations.hashCode()
        result = 31 * result + definitions.hashCode()
        result = 31 * result + examples.hashCode()
        return result
    }
}
