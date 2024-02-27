package com.catvasiliy.mydic.data.remote.model

data class ApiAlternativeTranslation(
    val translationText: String,
    val partOfSpeech: String,
    val synonyms: List<String>
)
