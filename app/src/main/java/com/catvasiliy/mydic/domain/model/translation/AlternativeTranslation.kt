package com.catvasiliy.mydic.domain.model.translation

data class AlternativeTranslation(
    val translationText: String,
    val partOfSpeech: String,
    val synonyms: List<String>
)
