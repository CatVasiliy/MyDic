package com.catvasiliy.mydic.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiAlternativeTranslation(
    @SerialName("pos") val partOfSpeech: String = "",
    @SerialName("entry") val entries: List<ApiAlternativeTranslationEntry> = emptyList()
)

@Serializable
data class ApiAlternativeTranslationEntry(
    @SerialName("word") val translationText: String = "",
    @SerialName("reverse_translation") val synonyms: List<String> = emptyList()
)
