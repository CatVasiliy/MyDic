package com.catvasiliy.mydic.data.remote.model

import com.squareup.moshi.Json

data class ApiAlternativeTranslation(
    @field:Json(name = "pos") val partOfSpeech: String?,
    @field:Json(name = "entry") val entries: List<ApiAlternativeTranslationEntry>?
)

data class ApiAlternativeTranslationEntry(
    @field:Json(name = "word") val translationText: String?,
    @field:Json(name = "reverse_translation") val synonyms: List<String>?
)
