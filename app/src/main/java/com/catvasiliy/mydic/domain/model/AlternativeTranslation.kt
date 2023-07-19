package com.catvasiliy.mydic.domain.model

data class AlternativeTranslation(
    val translationText: String,
    val partOfSpeech: String,
    val synonyms: List<String>
)
