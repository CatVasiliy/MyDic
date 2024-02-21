package com.catvasiliy.mydic.domain.model.translation

data class ExtendedTranslation(
    override val id: Long = 0,
    override val sourceText: String,
    override val sourceLanguage: Language,
    override val targetLanguage: Language,
    override val translatedAtMillis: Long,

    val translationText: String,
    val sourceTransliteration: String?,
    val alternativeTranslations: List<AlternativeTranslation>,
    val definitions: List<Definition>,
    val examples: List<Example>
) : Translation(id, sourceText, sourceLanguage, targetLanguage, translatedAtMillis)
